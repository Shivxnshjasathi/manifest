package com.zincstate.manifest.feature.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zincstate.manifest.core.database.dao.RecurringTransactionDao
import com.zincstate.manifest.core.database.entity.RecurringTransactionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val recurringDao: RecurringTransactionDao
) : ViewModel() {

    val subscriptions: StateFlow<List<RecurringTransactionEntity>> = recurringDao.getAllRecurring()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteSubscription(id: String) {
        viewModelScope.launch {
            recurringDao.deleteById(id)
        }
    }
}
