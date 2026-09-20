package com.zincstate.manifest.feature.addedit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.common.DateUtils
import com.zincstate.manifest.core.common.NlpParser
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.CategoryDao
import com.zincstate.manifest.core.database.dao.ContactDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.dao.TransactionSplitDao
import com.zincstate.manifest.core.database.entity.AccountEntity
import com.zincstate.manifest.core.database.entity.CategoryEntity
import com.zincstate.manifest.core.database.entity.ContactEntity
import com.zincstate.manifest.core.database.entity.TransactionEntity
import com.zincstate.manifest.core.database.entity.TransactionSplitEntity
import com.zincstate.manifest.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class AddEditUiState(
    val isEditMode: Boolean = false,
    val transactionId: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val date: String = DateUtils.today(),
    val selectedCategoryId: String? = "",
    val selectedAccountId: String = "",
    val selectedToAccountId: String = "",
    val note: String = "",
    val description: String = "",
    val smartAddInput: String = "",
    val categories: List<CategoryEntity> = emptyList(),
    val accounts: List<AccountEntity> = emptyList(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val showMoreDetails: Boolean = false,
    val noteSuggestions: List<String> = emptyList(),
    val contacts: List<ContactEntity> = emptyList(),
    val selectedContactIds: Set<String> = emptySet(),
    val isSplitEnabled: Boolean = false,
    val isValid: Boolean = false,
    val attachmentUri: Uri? = null,
    val attachmentPath: String? = null
)

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao,
    private val contactDao: ContactDao,
    private val splitDao: TransactionSplitDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val editId: String? = savedStateHandle["transactionId"]
    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    private val _amount = MutableStateFlow("")
    private val _date = MutableStateFlow(DateUtils.today())
    private val _selectedCategoryId = MutableStateFlow<String?>("")
    private val _selectedAccountId = MutableStateFlow("")
    private val _selectedToAccountId = MutableStateFlow("")
    private val _note = MutableStateFlow("")
    private val _description = MutableStateFlow("")
    private val _smartAddInput = MutableStateFlow("")
    private val _isSaving = MutableStateFlow(false)
    private val _isSaved = MutableStateFlow(false)
    private val _showMoreDetails = MutableStateFlow(false)
    private val _noteSuggestions = MutableStateFlow<List<String>>(emptyList())
    private val _isSplitEnabled = MutableStateFlow(false)
    private val _selectedContactIds = MutableStateFlow<Set<String>>(emptySet())
    private val _attachmentUri = MutableStateFlow<Uri?>(null)
    private val _attachmentPath = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AddEditUiState> = combine(
        _type, _amount, _date, _selectedCategoryId,
        _selectedAccountId, _selectedToAccountId, _note, _description,
        _smartAddInput, _isSaving, _isSaved, _showMoreDetails,
        categoryDao.getAllCategories(),
        accountDao.getAllAccounts(),
        _noteSuggestions,
        contactDao.getAllContacts(),
        _selectedContactIds,
        _isSplitEnabled,
        _attachmentUri,
        _attachmentPath
    ) { args ->
        @Suppress("UNCHECKED_CAST")
        val type = args[0] as TransactionType
        val amount = args[1] as String
        val catId = args[3] as String?
        val accId = args[4] as String
        val toAccId = args[5] as String
        val note = args[6] as String
        val selectedContactIds = args[16] as Set<String>

        val amountVal = amount.toDoubleOrNull() ?: 0.0
        val isAmountValid = amountVal > 0
        val isNoteValid = note.isNotBlank()
        val isCategoryValid = if (type == TransactionType.TRANSFER) true else !catId.isNullOrBlank()
        val isAccountValid = accId.isNotBlank()
        val isTransferValid = if (type == TransactionType.TRANSFER) {
            toAccId.isNotBlank() && toAccId != accId
        } else true

        AddEditUiState(
            isEditMode = editId != null,
            transactionId = editId ?: "",
            type = type,
            amount = amount,
            date = args[2] as String,
            selectedCategoryId = catId,
            selectedAccountId = accId,
            selectedToAccountId = toAccId,
            note = note,
            description = args[7] as String,
            smartAddInput = args[8] as String,
            isSaving = args[9] as Boolean,
            isSaved = args[10] as Boolean,
            showMoreDetails = args[11] as Boolean,
            categories = args[12] as List<CategoryEntity>,
            accounts = args[13] as List<AccountEntity>,
            noteSuggestions = args[14] as List<String>,
            contacts = args[15] as List<ContactEntity>,
            selectedContactIds = selectedContactIds,
            isSplitEnabled = args[17] as Boolean,
            isValid = isAmountValid && isNoteValid && isCategoryValid && isAccountValid && isTransferValid,
            attachmentUri = args[18] as Uri?,
            attachmentPath = args[19] as String?
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddEditUiState()
    )

    init {
        if (editId != null) {
            loadTransaction(editId)
        }
    }

    private fun loadTransaction(id: String) {
        viewModelScope.launch {
            val tx = transactionDao.getTransactionById(id) ?: return@launch
            _type.value = TransactionType.valueOf(tx.type)
            _amount.value = tx.amount.toString()
            _date.value = tx.date
            _selectedCategoryId.value = tx.categoryId
            _selectedAccountId.value = tx.accountId
            _selectedToAccountId.value = tx.toAccountId ?: ""
            _note.value = tx.note
            _description.value = tx.description
        }
    }

    fun setType(type: TransactionType) { _type.value = type }
    fun setAmount(amount: String) { _amount.value = amount }
    fun setDate(date: String) { _date.value = date }
    fun setCategory(categoryId: String?) { _selectedCategoryId.value = categoryId }
    fun setAccount(accountId: String) { _selectedAccountId.value = accountId }
    fun setToAccount(accountId: String) { _selectedToAccountId.value = accountId }
    fun setNote(note: String) { 
        _note.value = note 
        if (note.isNotBlank() && note.length > 1) {
            viewModelScope.launch {
                _noteSuggestions.value = transactionDao.getNoteSuggestions(note)
            }
        } else {
            _noteSuggestions.value = emptyList()
        }
    }
    fun setDescription(description: String) { _description.value = description }
    fun toggleMoreDetails() { _showMoreDetails.value = !_showMoreDetails.value }

    fun setAttachmentUri(uri: Uri?) {
        _attachmentUri.value = uri
    }

    fun removeAttachment() {
        _attachmentUri.value = null
        _attachmentPath.value = null
    }

    fun toggleSplit(enabled: Boolean) { _isSplitEnabled.value = enabled }

    fun toggleContactSelection(contactId: String) {
        val current = _selectedContactIds.value.toMutableSet()
        if (current.contains(contactId)) current.remove(contactId) else current.add(contactId)
        _selectedContactIds.value = current
    }

    fun addContact(name: String) {
        viewModelScope.launch {
            val contact = ContactEntity(id = UUID.randomUUID().toString(), name = name)
            contactDao.insert(contact)
            toggleContactSelection(contact.id)
        }
    }

    fun processSmartAdd(input: String) {
        _smartAddInput.value = input
        val parsed = NlpParser.parse(input)
        parsed.amount?.let { _amount.value = it.toString() }
        _type.value = parsed.type
        if (parsed.note.isNotBlank()) _note.value = parsed.note

        // Auto-categorize based on parsed category name or note suggestions
        viewModelScope.launch {
            val allCategories = categoryDao.getAllCategories().first()
            val allAccounts = accountDao.getAllAccounts().first()

            // Set Category
            if (parsed.categoryName != null) {
                val category = allCategories.firstOrNull { it.name.equals(parsed.categoryName, ignoreCase = true) }
                if (category != null) {
                    _selectedCategoryId.value = category.id
                }
            } else if (parsed.note.isNotBlank()) {
                val suggestions = transactionDao.getCategorySuggestionsForNote(parsed.note)
                suggestions.firstOrNull()?.let { suggestion ->
                    _selectedCategoryId.value = suggestion.categoryId
                }
            }

            // Set Account
            val parsedAccountName = parsed.accountName
            if (parsedAccountName != null) {
                val account = allAccounts.firstOrNull { it.name.contains(parsedAccountName, ignoreCase = true) }
                if (account != null) {
                    _selectedAccountId.value = account.id
                }
            }
        }
    }

    fun save(context: Context) {
        viewModelScope.launch {
            val amountValue = _amount.value.toDoubleOrNull() ?: return@launch
            if (amountValue <= 0) return@launch
            if (_note.value.isBlank()) return@launch
            if (_type.value != TransactionType.TRANSFER && _selectedCategoryId.value.isNullOrBlank()) return@launch
            if (_selectedAccountId.value.isBlank()) return@launch

            _isSaving.value = true

            // Handle attachment saving
            var savedPath = _attachmentPath.value
            _attachmentUri.value?.let { uri ->
                savedPath = saveImageToInternalStorage(context, uri)
            }

            // 1. Reverse old impact if editing
            if (editId != null) {
                transactionDao.getTransactionById(editId)?.let { oldTx ->
                    reverseTransactionImpact(oldTx)
                }
            }

            val entity = TransactionEntity(
                id = editId ?: UUID.randomUUID().toString(),
                type = _type.value.name,
                amount = amountValue,
                date = _date.value,
                categoryId = if (_type.value == TransactionType.TRANSFER) null else _selectedCategoryId.value,
                accountId = _selectedAccountId.value,
                toAccountId = _selectedToAccountId.value.ifBlank { null },
                note = _note.value,
                description = _description.value,
                attachmentPath = savedPath,
                updatedAt = System.currentTimeMillis()
            )

            // 2. Save transaction
            if (editId != null) {
                transactionDao.update(entity)
            } else {
                transactionDao.insert(entity)
            }

            // 3. Apply new impact to account balances
            applyTransactionImpact(entity)

            // 4. Save splits if enabled
            if (_isSplitEnabled.value && _selectedContactIds.value.isNotEmpty()) {
                val splitAmount = amountValue / (_selectedContactIds.value.size + 1)
                val splits = _selectedContactIds.value.map { contactId ->
                    TransactionSplitEntity(
                        id = UUID.randomUUID().toString(),
                        transactionId = entity.id,
                        contactId = contactId,
                        amount = splitAmount,
                        isSettled = false
                    )
                }
                splitDao.insertSplits(splits)
            }

            _isSaving.value = false
            _isSaved.value = true
        }
    }

    private suspend fun reverseTransactionImpact(tx: TransactionEntity) {
        when (tx.type) {
            "INCOME" -> accountDao.updateBalance(tx.accountId, -tx.amount)
            "EXPENSE" -> accountDao.updateBalance(tx.accountId, tx.amount)
            "TRANSFER" -> {
                accountDao.updateBalance(tx.accountId, tx.amount)
                tx.toAccountId?.let { accountDao.updateBalance(it, -tx.amount) }
            }
        }
    }

    private suspend fun applyTransactionImpact(tx: TransactionEntity) {
        when (tx.type) {
            "INCOME" -> accountDao.updateBalance(tx.accountId, tx.amount)
            "EXPENSE" -> accountDao.updateBalance(tx.accountId, -tx.amount)
            "TRANSFER" -> {
                accountDao.updateBalance(tx.accountId, -tx.amount)
                tx.toAccountId?.let { accountDao.updateBalance(it, tx.amount) }
            }
        }
    }

    private fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val fileName = "receipt_${UUID.randomUUID()}.jpg"
            val directory = context.getDir("receipts", Context.MODE_PRIVATE)
            val file = java.io.File(directory, fileName)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
