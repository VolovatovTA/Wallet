package com.example.wallet.ui.transactions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallet.data.ItemsType
import com.example.wallet.data.Repository
import com.example.wallet.data.Result
import com.example.wallet.data.findById
import com.example.wallet.data.placeholder.PlaceholderContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*

object TransactionsViewModel : ViewModel() {
    private val repository = Repository
    private val TAG_Tim = "Timofey"

    private val transactionForm_ = MutableLiveData<TransactionFormState>()
    val transactionFormState: LiveData<TransactionFormState> = transactionForm_

    private val transactionAddResult_ = MutableLiveData<TransactionAddResult>()
    val transactionAddResult: LiveData<TransactionAddResult> = transactionAddResult_

    private val transactionDeleteResult_ = MutableLiveData<TransactionDeleteResult>()
    val transactionDeleteResult: LiveData<TransactionDeleteResult> = transactionDeleteResult_

    init {
        getTransactionList()
    }

    private fun getTransactionList() {
        transactionForm_.value = TransactionFormState(isDataLoading = true)
        CoroutineScope(Main).launch {
            Repository.dataSource.getTransactions()
            if (Repository.dataSource.transactions.isEmpty()) {
                transactionForm_.value = TransactionFormState(isEmptyListTransactions = true)

            } else {
                PlaceholderContent.fillAfterEmptyList(Repository.dataSource.transactions)
                transactionForm_.value = TransactionFormState(allIsGood = true)

            }
        }

    }

    fun addTransaction(
        amount: Float,
        category: String,
        comment: String,
        currency: String,
        type: String
    ) {
        transactionForm_.value = TransactionFormState(isDataLoading = true)
        CoroutineScope(Main).launch {

            when (val result = repository.dataSource.createTransaction(
                comment,
                amount,
                currency,
                category,
                type
            )) {
                is Result.Success -> {
                    if (PlaceholderContent.transactionsWithSeparators.isEmpty()) {
                        PlaceholderContent.transactionsWithSeparators.add(
                            PlaceholderContent.PlaceholderItem(
                                null,
                                ItemsType().SEPARATOR_TYPE,
                                "Сегодня"
                            )
                        )
                    }
                    PlaceholderContent.transactionsWithSeparators.add(
                        PlaceholderContent.PlaceholderItem(
                            result.data.transaction,
                            ItemsType().TRANSACTION_TYPE,
                            null
                        )
                    )
                    transactionForm_.value = TransactionFormState(allIsGood = true)


                }
                else -> {
                    transactionForm_.value = TransactionFormState(isSomeError = true)
                }
            }


        }
    }

    fun deleteTransaction() {
        Log.d(TAG_Tim, "list = ${PlaceholderContent.listSelectedItems}")
        CoroutineScope(Main).launch {
            for (tranId in PlaceholderContent.listSelectedItems) {
                // remove from server
                val result = repository.dataSource.deleteTransaction(tranId)
                if (result.body() == "success") {
                    Log.d(TAG_Tim, "in for")
                    //remove from placeHolder
                    PlaceholderContent.transactionsWithSeparators.remove(
                        PlaceholderContent.PlaceholderItem(
                            Repository.dataSource.transactions.findById(tranId),
                            ItemsType().TRANSACTION_TYPE,
                            null
                        )
                    )
                    // remove from Repository
                    Repository.dataSource.transactions.remove(
                        Repository.dataSource.transactions.findById(
                            tranId
                        )
                    )


                }
                else {
                    transactionForm_.value = TransactionFormState(isTransactionsNotDeleted = true)
                }
            }
            // change state
            if (Repository.dataSource.transactions.isEmpty()) {
                PlaceholderContent.transactionsWithSeparators.clear()
                transactionForm_.value = TransactionFormState(isEmptyListTransactions = true)
            } else {
                transactionForm_.value = TransactionFormState(isTransactionsDeleted = true)
            }
            // clear for new ides
            PlaceholderContent.listSelectedItems.clear()


        }
    }

    fun setSelectionMode() {
        transactionForm_.value = TransactionFormState(isSelectionMode = true)
    }

}

class TransactionDeleteResult {

}

class TransactionAddResult {

}

data class TransactionFormState(
    val isDataLoading: Boolean = false,
    val allIsGood: Boolean = false,
    val isEmptyListTransactions: Boolean = false,
    val isSomeError: Boolean = false,
    val isSelectionMode: Boolean = false,
    val isTransactionsDeleted: Boolean = false,
    val isTransactionsNotDeleted: Boolean = false
)