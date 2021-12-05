package com.example.wallet.ui.transactions

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.wallet.R
import com.example.wallet.data.MyTransactionItemRecyclerViewAdapter
import com.example.wallet.data.placeholder.PlaceholderContent
import com.example.wallet.databinding.FragmentItemListBinding


class TransactionItemFragment : Fragment() {

    private var columnCount = 1
    private val TAG = "Timofey"
    lateinit var binding: FragmentItemListBinding
    private val transactionViewModel: TransactionsViewModel = TransactionsViewModel
    var isSelctionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "TransactionItemFragment onCreate")

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemListBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        if (isSelctionMode){
            inflater.inflate(R.menu.menu_edit_delete, menu)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated itemsFragment")




        transactionViewModel.transactionFormState.observe(viewLifecycleOwner,
            Observer { transactionFormState ->
                if (transactionFormState != null) {
                    when {
                        transactionFormState.allIsGood -> {
                            binding.ProgressBar.visibility = View.GONE
                            binding.list.visibility = View.VISIBLE
                            with(binding.list) {
                                layoutManager = when {

                                    columnCount <= 1 -> LinearLayoutManager(context)
                                    else -> GridLayoutManager(context, columnCount)
                                }
                                val myTransactionItemRecyclerViewAdapter = MyTransactionItemRecyclerViewAdapter(PlaceholderContent.transactionsWithSeparators)

                                myTransactionItemRecyclerViewAdapter.setOnItemLongClick {

                                    true
                                }
                                adapter = myTransactionItemRecyclerViewAdapter
                            }
                        }
                        transactionFormState.isEmptyListTransactions -> {
                            binding.ProgressBar.visibility = View.GONE
                            binding.listWhenTransactionsIsNull.visibility = View.GONE
                            binding.listWhenTransactionsIsNull.visibility = View.VISIBLE
                        }
                        transactionFormState.isDataLoading -> {
                            binding.ProgressBar.visibility = View.VISIBLE
                            binding.list.visibility = View.GONE
                        }
                        transactionFormState.isSelectionMode -> {

                        }
                    }
                }
                else {

                    return@Observer
                }


            })

        binding.addTransactionFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.blankForCreateTransactionFragment)
        }
        Log.d(TAG, "onCreateView itemsFragment")

//        observeAuthenticationState()
    }

    //    private fun observeAuthenticationState() {
//        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
//            when (authenticationState) {
//                LoginViewModel.AuthenticationState.AUTHENTICATED -> {findNavController().navigate(R.id.transactionItemFragment)}
//                else -> {findNavController().navigate(R.id.loginFragment)}
//            }
//        })
//    }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            TransactionItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}