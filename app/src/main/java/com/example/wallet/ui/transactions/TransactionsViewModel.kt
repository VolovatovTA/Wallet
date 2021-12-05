package com.example.wallet.ui.transactions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wallet.data.ItemsType
import com.example.wallet.data.Repository
import com.example.wallet.data.Result
import com.example.wallet.data.placeholder.PlaceholderContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

object TransactionsViewModel : ViewModel() {
    private val repository = Repository
    private val TAG_Tim = "Timofey"

    private val _transactionForm = MutableLiveData<TransactionFormState>()
    val transactionFormState: LiveData<TransactionFormState> = _transactionForm

    private val _transactionAddResult = MutableLiveData<TransactionAddResult>()
    val transactionAddResult: LiveData<TransactionAddResult> = _transactionAddResult

    private val _transactionDeleteResult = MutableLiveData<TransactionDeleteResult>()
    val transactionDeleteResult: LiveData<TransactionDeleteResult> = _transactionDeleteResult


    init {
        getTransactionList()
    }
    private fun getTransactionList(){
        _transactionForm.value = TransactionFormState(isDataLoading = true)
        CoroutineScope(Main).launch {
            Repository.dataSource.getTransactions()
            _transactionForm.value = TransactionFormState(allIsGood = true)
        }

    }
    fun addTransaction(amount: Float, category: String, comment: String, currency: String, type: String) {
        _transactionForm.value = TransactionFormState(isDataLoading = true,)
        CoroutineScope(Main).launch {

            when (val result = repository.dataSource.createTransaction(comment, amount, currency, category, type)){
                is Result.Success -> {
                    PlaceholderContent.transactionsWithSeparators.add(
                        1,
                        PlaceholderContent.PlaceholderItem(
                            result.data.transaction,
                            ItemsType().TRANSACTION_TYPE,
                            null)
                    )
                    _transactionForm.value = TransactionFormState(allIsGood = true)


                }
                else -> {_transactionForm.value = TransactionFormState(isSomeError = true)
                }
            }




        }
    }

}

class TransactionDeleteResult {

}

class TransactionAddResult {

}

data class TransactionFormState(
    val isDataLoading: Boolean = true,
    val allIsGood: Boolean = false,
    val isEmptyListTransactions: Boolean = false,
    val isSomeError: Boolean = false,
    val isSelectionMode: Boolean = false
)