package com.example.wallet.ui.transactions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wallet.R
import com.example.wallet.databinding.BlankForCreateTransactionFragmentBinding

class BlankForCreateTransactionFragment : Fragment() {

    lateinit var binding: BlankForCreateTransactionFragmentBinding
    lateinit var transactionsViewModel: TransactionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BlankForCreateTransactionFragmentBinding.inflate(inflater, container, false)
        transactionsViewModel = TransactionsViewModel

        binding.okButton.setOnClickListener {
            transactionsViewModel.addTransaction(
                amount = if (binding.amountEdit.text.isNotEmpty()) binding.amountEdit.text.toString().toFloat() else 0f,
                category = if(binding.spinnerCategory.isSelected) binding.spinnerCategory.selectedItemPosition.toString() else "",
                comment = if(binding.editTextComment.text.isNotEmpty()) binding.editTextComment.text.toString() else "",
                currency = binding.spinnerCurrency.selectedItem.toString(),
                type = if(binding.inOutSwitch.isChecked) {"in"} else {"out"})
            findNavController().popBackStack()
            findNavController().navigate(R.id.transactionItemFragment)
        }
        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.transactionItemFragment)
        }
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                arguments = bundleOf(
                    "amount" to binding.amountEdit.text.toString(),
                    "category" to binding.spinnerCategory.selectedItemPosition.toString(),
                    "comment" to binding.editTextComment.text.toString(),
                    "currency" to binding.spinnerCurrency.selectedItemPosition.toString()
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        binding.spinnerCategory.onItemSelectedListener = listener

        val watcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {


            }

        }
        binding.editTextComment.addTextChangedListener(watcher)
        binding.amountEdit.addTextChangedListener(watcher)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}