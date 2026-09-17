package com.zincstate.manifest.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.ContactDao
import com.zincstate.manifest.core.database.dao.TransactionSplitDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContactDebt(
    val contactId: String,
    val contactName: String,
    val amountOwed: Double
)

data class DebtsUiState(
    val debts: List<ContactDebt> = emptyList()
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DebtsViewModel @Inject constructor(
    private val contactDao: ContactDao,
    private val splitDao: TransactionSplitDao
) : ViewModel() {

    val uiState: StateFlow<DebtsUiState> = contactDao.getAllContacts()
        .flatMapLatest { contacts ->
            val debtFlows = contacts.map { contact ->
                splitDao.getOwedAmountForContact(contact.id).map { amount ->
                    ContactDebt(contact.id, contact.name, amount ?: 0.0)
                }
            }
            if (debtFlows.isEmpty()) flowOf(emptyList<ContactDebt>())
            else combine(debtFlows) { it.toList() }
        }
        .map { allDebts ->
            DebtsUiState(debts = allDebts.filter { it.amountOwed > 0 })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DebtsUiState()
        )

    fun settleDebt(contactId: String) {
        viewModelScope.launch {
            // In a real app, this might create a transaction of type INCOME
            // but for simplicity we'll just mark existing splits as settled
            splitDao.getUnsettledSplitsForContact(contactId).first().forEach { split ->
                splitDao.settleSplit(split.id)
            }
        }
    }
}
