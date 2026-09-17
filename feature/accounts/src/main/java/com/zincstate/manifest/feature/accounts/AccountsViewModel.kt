package com.zincstate.manifest.feature.accounts

import androidx.lifecycle.ViewModel
import com.zincstate.manifest.core.common.CurrencyFormatter
import com.zincstate.manifest.core.database.dao.AccountDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

import androidx.compose.runtime.Immutable

@Immutable
data class AccountUiItem(
    val id: String,
    val name: String,
    val group: String,
    val balanceFormatted: String,
    val balance: Double
)

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountDao: AccountDao
) : ViewModel() {

    val accounts: StateFlow<List<AccountUiItem>> = accountDao.getAllAccounts().map { list ->
        list.map { entity ->
            AccountUiItem(
                id = entity.id,
                name = entity.name,
                group = entity.group,
                balanceFormatted = CurrencyFormatter.format(entity.balance),
                balance = entity.balance
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addAccount(name: String, group: String, balance: Double) {
        viewModelScope.launch {
            val entity = com.zincstate.manifest.core.database.entity.AccountEntity(
                id = java.util.UUID.randomUUID().toString(),
                name = name,
                group = group,
                balance = balance
            )
            accountDao.insert(entity)
        }
    }
}
