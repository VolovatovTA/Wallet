package com.example.wallet.ui.transactions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.wallet.MainActivity
import com.example.wallet.R
import com.example.wallet.data.MyTransactionItemRecyclerViewAdapter
import com.example.wallet.data.placeholder.PlaceholderContent
import com.example.wallet.databinding.FragmentItemListBinding


class TransactionItemFragment : Fragment() {

    private var columnCount = 1
    private val TAG = "Timofey"
    lateinit var binding: FragmentItemListBinding
    private val transactionViewModel: TransactionsViewModel = TransactionsViewModel
    private val adapterListTransactions = MyTransactionItemRecyclerViewAdapter

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
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)


        return binding.root
    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.list) {
            layoutManager = when {

                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            adapterListTransactions.setOnItemLongClick {

                true
            }
            adapter = adapterListTransactions
        }
        transactionViewModel.transactionFormState.observe(viewLifecycleOwner,
            Observer { transactionFormState ->
                if (transactionFormState != null) {
                    when {
                        transactionFormState.allIsGood -> {
                            binding.ProgressBar.visibility = View.GONE
                            binding.list.visibility = View.VISIBLE

                        }
                        transactionFormState.isEmptyListTransactions -> {
                            (requireActivity() as MainActivity).isSelectionMode = false
                            requireActivity().invalidateOptionsMenu()
                            binding.ProgressBar.visibility = View.GONE
                            binding.list.visibility = View.GONE
                            binding.listWhenTransactionsIsNull.visibility = View.VISIBLE
                        }
                        transactionFormState.isDataLoading -> {
                            binding.ProgressBar.visibility = View.VISIBLE
                            binding.list.visibility = View.GONE
                        }
                        transactionFormState.isSelectionMode -> {
                            (requireActivity() as MainActivity).isSelectionMode = true
                            requireActivity().invalidateOptionsMenu()
                            Log.d(TAG, "placeholder = ${PlaceholderContent.transactionsWithSeparators}")

                        }
                        transactionFormState.isTransactionsDeleted -> {
                            adapterListTransactions.notifyDataSetChanged()
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