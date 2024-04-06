package com.example.myfirstmovieapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class PruchaseHistory : AppCompatActivity() {
    private val SHARED_PREFS = "my_shared_prefs"
    private val ORDER_KEY = "order_key"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pruchase_history)

        try {

            val purchaseHistoryListView: ListView = findViewById(R.id.PruchaseListView)
            val orders = readPurchaseHistoryFromSharedPreferences()
            val Order_ID=getString(R.string.OrderId)
            val Price=getString(R.string.price)
            val Quntity=getString(R.string.quantity)
            val Cinema=getString(R.string.Cinema)
            val Items=getString(R.string.items)
            val Category=getString(R.string.Category)
            val money=getString(R.string.Money)
            val orderStrings = orders.map { order ->
                val itemsString = order.items.joinToString("\n") { item ->
                    "$Category: ${item.category}, $Price: ${item.totalPrice}$money, $Quntity: ${item.quantity}"
                }
                "$Order_ID: ${order.id}, $Cinema: ${order.cinema}\n$Items:\n$itemsString"
            }.toTypedArray()

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderStrings)
            purchaseHistoryListView.adapter = adapter
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PruchaseHistory", "Error in onCreate: ${e.message}")
        }
        val btn_main=findViewById<Button>(R.id.main_return)
        btn_main.setOnClickListener {
            val main_return = Intent(this, MainActivity::class.java)
            startActivity(main_return)

        }
    }

    private fun readPurchaseHistoryFromSharedPreferences(): List<Order> {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val orderJson = sharedPreferences.getString(ORDER_KEY, "")
        return if (orderJson.isNullOrEmpty()) {
            emptyList()

        } else {
            listOf(convertJsonToOrder(orderJson))
        }
    }

    private fun convertJsonToOrder(json: String): Order {
        val jsonObject = JSONObject(json)
        val orderId = jsonObject.getInt("id")
        val cinema = jsonObject.getString("cinema")

        val itemsArray = jsonObject.getJSONArray("items")
        val items = mutableListOf<OrderItem>()
        for (i in 0 until itemsArray.length()) {
            val itemObject = itemsArray.getJSONObject(i)
            val category = itemObject.getString("category")
            val totalPrice = itemObject.getInt("totalPrice")
            val quantity = itemObject.getInt("quantity")
            items.add(OrderItem(category, totalPrice, quantity))
        }

        return Order(orderId, cinema, items)
    }
}