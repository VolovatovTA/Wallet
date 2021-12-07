package com.example.wallet.data

import android.text.Html
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.wallet.R
import com.example.wallet.data.placeholder.PlaceholderContent

import com.example.wallet.data.placeholder.PlaceholderContent.PlaceholderItem
import com.example.wallet.databinding.FragmentItemBinding
import com.example.wallet.databinding.ItemOfDayBinding
import com.example.wallet.ui.transactions.TransactionsViewModel
import java.util.*
import kotlin.collections.ArrayList


object MyTransactionItemRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    lateinit var listenerLongClick: (position: Int) -> Boolean
    val TAG = "Timofey"

    init {
        Log.d(TAG, "init adapter")
        PlaceholderContent.listSelectedItems.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == ItemsType().TRANSACTION_TYPE) {
            return ViewHolderForTransaction(
                FragmentItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else
            return ViewHolderForSeparateTransactions(
                ItemOfDayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val item = PlaceholderContent.transactionsWithSeparators[position]
        when (holder) {
            is ViewHolderForTransaction -> {
                with(holder) {
                    comment.text = item.transaction!!.commentTransaction
                    category.text = if (item.transaction.categoryId == null) {
                        ""
                    } else {
                        item.transaction.categoryId.toString()
                    }
                    amount.text =
                        getBeautifulAmount(item.transaction.amount, item.transaction.currency)
                    date.text =
                        item.transaction.dateOfUpdate.hours.toString() + ":" + item.transaction.dateOfUpdate.minutes.toString()
                    if (item.transaction.type == "in") {
                        holder.image.setImageResource(R.drawable.ic_baseline_attach_money_24)
                        holder.image.setBackgroundResource(R.drawable.icons_background_green)

                    } else if (item.transaction.type == "out") {
                        holder.image.setImageResource(R.drawable.ic_baseline_money_off_24)

                        holder.image.setBackgroundResource(R.drawable.icons_background_red)

                    }
                    if (PlaceholderContent.listSelectedItems.contains(PlaceholderContent.transactionsWithSeparators[position].transaction!!.transactionId)){
                        holder.itemView.setBackgroundResource(R.color.teal_700)
                    }
                    else {
                        holder.itemView.setBackgroundResource(R.color.white)
                    }
                }

            }
            is ViewHolderForSeparateTransactions -> {
                holder.date.text = item.dateOfSeparation
            }

        }


    }


    private fun getBeautifulAmount(amount: Float, currency: String): String {
        val intAmount = if (amount >= 1) {
            amount.toInt().toString().reversed().chunked(3)
        } else {
            listOf("")
        }
        var goodString = ""

        for (parts in intAmount) {
            goodString += " $parts"
        }
        val afterPoint = String.format("%.2f", (amount - amount.toInt()))
        return goodString.drop(1).reversed() + if (amount >= 1) afterPoint.drop(1) else {
            afterPoint
        } + " " + if (currency == "RUR") {
            Html.fromHtml(" &#x20bd")
        } else {
            ""
        }
    }


    override fun getItemViewType(position: Int): Int {

        return PlaceholderContent.transactionsWithSeparators[position].itemType

    }


    override fun getItemCount(): Int = PlaceholderContent.transactionsWithSeparators.size

    fun setOnItemLongClick(listener: (position: Int) -> (Boolean)){
        this.listenerLongClick = listener
    }

    var isMultiSelectedOn = false

    class ViewHolderForTransaction(binding: FragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnLongClickListener{
                isMultiSelectedOn = true
                binding.root.setBackgroundResource(R.color.teal_700)
                PlaceholderContent.listSelectedItems.add(
                    PlaceholderContent.transactionsWithSeparators[
                            absoluteAdapterPosition
                    ].transaction!!.transactionId
                )
                TransactionsViewModel.setSelectionMode()
                listenerLongClick.invoke(absoluteAdapterPosition)

            }
            binding.root.setOnClickListener {
                if(isMultiSelectedOn){
                    binding.root.setBackgroundResource(R.color.teal_700)
                    PlaceholderContent.listSelectedItems.add(
                        PlaceholderContent.transactionsWithSeparators[
                                absoluteAdapterPosition
                        ].transaction!!.transactionId
                    )
                    listenerLongClick.invoke(absoluteAdapterPosition)
                }

            }
        }
        val comment: TextView = binding.itemComment
        val category: TextView = binding.itemCategory
        val amount: TextView = binding.itemAmount
        val date: TextView = binding.itemDate
        val image: ImageView = binding.imageView


    }

    class ViewHolderForSeparateTransactions(binding: ItemOfDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date: TextView = binding.separate


    }


}

data class ItemsType(
    val TRANSACTION_TYPE: Int = 0,
    val SEPARATOR_TYPE: Int = 1

)