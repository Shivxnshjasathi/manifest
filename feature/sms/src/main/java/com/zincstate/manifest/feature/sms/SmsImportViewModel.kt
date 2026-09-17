package com.zincstate.manifest.feature.sms

import android.content.Context
import android.provider.Telephony
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.entity.TransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

data class ParsedSmsUiModel(
    val smsId: String,
    val originalText: String,
    val amount: String,
    val isExpense: Boolean,
    val accountName: String,
    val isSelected: Boolean = true,
    val isDuplicate: Boolean = false
)

data class SmsImportUiState(
    val isLoading: Boolean = false,
    val parsedMessages: List<ParsedSmsUiModel> = emptyList(),
    val error: String? = null,
    val importComplete: Boolean = false
)

@HiltViewModel
class SmsImportViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmsImportUiState())
    val uiState: StateFlow<SmsImportUiState> = _uiState.asStateFlow()

    fun scanSms(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val parsedList = withContext(Dispatchers.IO) {
                val list = mutableListOf<ParsedSmsUiModel>()
                try {
                    val cursor = context.contentResolver.query(
                        Telephony.Sms.Inbox.CONTENT_URI,
                        arrayOf(Telephony.Sms._ID, Telephony.Sms.BODY),
                        null,
                        null,
                        "${Telephony.Sms.DATE} DESC LIMIT 500" // Scan last 500 for demo
                    )

                    cursor?.use { c ->
                        val idIndex = c.getColumnIndex(Telephony.Sms._ID)
                        val bodyIndex = c.getColumnIndex(Telephony.Sms.BODY)
                        val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

                        while (c.moveToNext()) {
                            val id = c.getString(idIndex)
                            val body = c.getString(bodyIndex)

                            val parsed = SmsParser.parse(body)
                            if (parsed != null) {
                                val amountDouble = parsed.amount
                                val isDup = transactionDao.checkFuzzyDuplicate(
                                    date = todayStr, 
                                    amount = amountDouble, 
                                    keyword = "Imported from SMS"
                                ) > 0

                                list.add(
                                    ParsedSmsUiModel(
                                        smsId = id,
                                        originalText = parsed.originalText,
                                        amount = parsed.amount.toString(),
                                        isExpense = parsed.isExpense,
                                        accountName = parsed.accountName,
                                        isSelected = !isDup, // auto-deselect if duplicate
                                        isDuplicate = isDup
                                    )
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                list
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                parsedMessages = parsedList
            )
        }
    }

    fun toggleSelection(smsId: String) {
        val current = _uiState.value.parsedMessages
        val updated = current.map {
            if (it.smsId == smsId) it.copy(isSelected = !it.isSelected) else it
        }
        _uiState.value = _uiState.value.copy(parsedMessages = updated)
    }

    fun importSelected() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val selected = _uiState.value.parsedMessages.filter { it.isSelected }
            
            if (selected.isNotEmpty()) {
                val accounts = accountDao.getAllAccounts().first()
                val categories = categoryDao.getAllCategories().first()
                
                val defaultAccount = accounts.firstOrNull()
                val defaultCategory = categories.firstOrNull()
                
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

                val entities = selected.map {
                    TransactionEntity(
                        id = UUID.randomUUID().toString(),
                        type = if (it.isExpense) "EXPENSE" else "INCOME",
                        amount = it.amount.toDoubleOrNull() ?: 0.0,
                        categoryId = defaultCategory?.id ?: "unknown",
                        accountId = defaultAccount?.id ?: "unknown",
                        note = "Imported from SMS",
                        description = it.accountName, // Storing detected bank in description
                        date = today,
                        createdAt = System.currentTimeMillis()
                    )
                }

                transactionDao.insertAll(entities)
            }
            
            _uiState.value = _uiState.value.copy(isLoading = false, importComplete = true)
        }
    }
}
