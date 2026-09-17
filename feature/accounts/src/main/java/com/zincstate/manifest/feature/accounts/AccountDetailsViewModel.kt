package com.zincstate.manifest.feature.accounts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.AccountDao
import com.zincstate.manifest.core.database.dao.TransactionDao
import com.zincstate.manifest.core.database.entity.AccountEntity
import com.zincstate.manifest.core.database.entity.TransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val accountId: String = checkNotNull(savedStateHandle["accountId"])

    val account: StateFlow<AccountEntity?> = accountDao.getAccountByIdFlow(accountId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val transactions: StateFlow<List<TransactionEntity>> = transactionDao.getTransactionsByAccount(accountId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteAccount() {
        viewModelScope.launch {
            account.value?.let { acc ->
                accountDao.delete(acc)
            }
        }
    }
}
