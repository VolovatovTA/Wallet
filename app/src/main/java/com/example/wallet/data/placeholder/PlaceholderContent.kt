package com.example.wallet.data.placeholder

import android.util.Log
import com.example.wallet.data.ItemsType
import com.example.wallet.data.Repository
import com.example.wallet.data.myTransaction
import java.util.*
import kotlin.collections.ArrayList


object PlaceholderContent {

    val TAG = "Timofey"


    val transactionsWithSeparators: MutableList<PlaceholderItem> = ArrayList()

    val listSelectedItems = ArrayList<UUID>()

    private val currentDate: Date = Calendar.getInstance().time

    init {
        Log.d(TAG, "init Placeholder")

        if (Repository.dataSource.transactions.isNotEmpty()) {
            fillAfterEmptyList(Repository.dataSource.transactions)
        }
    }
    fun fillAfterEmptyList(transactions: ArrayList<myTransaction>) {

        when (transactions[0].dateOfUpdate.date){
            currentDate.date -> {
                Log.d(TAG, "[1]")
                transactionsWithSeparators.add(PlaceholderItem(null, ItemsType().SEPARATOR_TYPE, "Сегодня"))
            }
            currentDate.date - 1 -> {
                Log.d(TAG, "[2]")
                transactionsWithSeparators.add(PlaceholderItem(null, ItemsType().SEPARATOR_TYPE, "Вчера"))
            }
            currentDate.date - 2 -> {
                Log.d(TAG, "[3]")
                transactionsWithSeparators.add(PlaceholderItem(null, ItemsType().SEPARATOR_TYPE, "Раньше"))
            }
        }

        var previousTransaction = transactions[0]
        for (t in transactions) {
            if (t.dateOfUpdate.date < previousTransaction.dateOfUpdate.date){
                val str = t.dateOfUpdate.date.toString() + " " + nameOfMonth(t.dateOfUpdate.month)
                Log.d(TAG, "str = $str")

                transactionsWithSeparators.add(PlaceholderItem(null, ItemsType().SEPARATOR_TYPE, str))

            }
            transactionsWithSeparators.add(PlaceholderItem(t, ItemsType().TRANSACTION_TYPE, null))

            if (t != transactions[0]){
                previousTransaction = t
            }



        }
    }
    private fun nameOfMonth(month: Int): String {

        val listNameMonth = listOf("Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября","Декабря")
        val monthName = {number: Int   ->  listNameMonth[number]}
        return monthName(month)
    }


    data class PlaceholderItem(
        val transaction: myTransaction?,
        val itemType: Int,
        val dateOfSeparation: String?
    )
}