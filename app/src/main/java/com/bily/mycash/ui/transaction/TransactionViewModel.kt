package com.bily.mycash.ui.transaction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bily.mycash.MyCashApp
import com.bily.mycash.data.db.entity.TransactionEntity
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as MyCashApp
    private val repo = app.transactionRepository

    val allTransactions: LiveData<List<TransactionEntity>> = repo.allTransactions

    fun insert(transaction: TransactionEntity) {
        viewModelScope.launch {
            repo.insert(transaction)
        }
    }

    fun delete(transaction: TransactionEntity) {
        viewModelScope.launch {
            repo.delete(transaction)
        }
    }

    fun searchTransactions(query: String): LiveData<List<TransactionEntity>> {
        return repo.searchTransactions(query)
    }
}
