package com.example.myfirstmovieapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.Random

data class Order(
    val id: Int,
    val cinema: String,
    val items: List<OrderItem>
)

data class OrderItem(
    val category: String,
    val totalPrice: Int,
    val quantity: Int
)


class OrderActivity: AppCompatActivity() {
    private val SHARED_PREFS = "my_shared_prefs"
    private val ORDER_KEY = "order_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)



        val totalPrice1 = intent.getIntExtra("totalPrice1", 0)
        val totalPrice2 = intent.getIntExtra("totalPrice2", 0)
        val totalPrice3 = intent.getIntExtra("totalPrice3", 0)
        val quantity1 = intent.getIntExtra("quantity1", 0)
        val quantity2 = intent.getIntExtra("quantity2", 0)
        val quantity3 = intent.getIntExtra("quantity3", 0)
        val selectedCinema = intent.getStringExtra("selectedCinema")
        val selectedCategory1 = intent.getStringExtra("selectedCategory1")
        val selectedCategory2 = intent.getStringExtra("selectedCategory2")
        val selectedCategory3 = intent.getStringExtra("selectedCategory3")
        val money=getString(R.string.Money)
        val Price=getString(R.string.price)
        val Quantity=getString(R.string.quantity)
        val Cinema=getString(R.string.Cinema)
        val orderList = listOf(
            "$Cinema: $selectedCinema",
            "$selectedCategory1: $Price $totalPrice1$money, $Quantity $quantity1",
            "$selectedCategory2: $Price $totalPrice2$money, $Quantity $quantity2",
            "$selectedCategory3: $Price $totalPrice3$money, $Quantity $quantity3"
        )

        val listView: ListView = findViewById(R.id.cinemaListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, orderList)
        listView.adapter = adapter
        val T_priceString = getString(R.string.TotalPrice)
        val totalPriceTextView: TextView = findViewById(R.id.totalPriceTextView)
        val totalPriceSum = totalPrice1 + totalPrice2 + totalPrice3
        totalPriceTextView.text = "$T_priceString: $totalPriceSum$money"

        val saveBut: Button = findViewById(R.id.SaveOrder)
        saveBut.setOnClickListener {
            val orderId = generateOrderId()
            val order = Order(
                orderId,
                selectedCinema ?: "",
                createOrderItemList(
                    totalPrice1, quantity1, selectedCategory1,
                    totalPrice2, quantity2, selectedCategory2,
                    totalPrice3, quantity3, selectedCategory3
                )
            )


            saveOrderToSharedPreferences(order)


            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()


            val moveToMain = Intent(this, MainActivity::class.java)
            startActivity(moveToMain)
        }
    }

    private fun generateOrderId(): Int {
        return Random().nextInt(Int.MAX_VALUE)
    }

    private fun createOrderItemList(
        totalPrice1: Int, quantity1: Int, selectedCategory1: String?,
        totalPrice2: Int, quantity2: Int, selectedCategory2: String?,
        totalPrice3: Int, quantity3: Int, selectedCategory3: String?
    ): List<OrderItem> {
        val orderItemList = mutableListOf<OrderItem>()

        if (selectedCategory1 != null && totalPrice1 > 0 && quantity1 > 0) {
            orderItemList.add(OrderItem(selectedCategory1, totalPrice1, quantity1))
        }

        if (selectedCategory2 != null && totalPrice2 > 0 && quantity2 > 0) {
            orderItemList.add(OrderItem(selectedCategory2, totalPrice2, quantity2))
        }

        if (selectedCategory3 != null && totalPrice3 > 0 && quantity3 > 0) {
            orderItemList.add(OrderItem(selectedCategory3, totalPrice3, quantity3))
        }

        return orderItemList
    }

    private fun saveOrderToSharedPreferences(order: Order) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Convert Order object to JSON string and save in SharedPreferences
        val orderJson = convertOrderToJson(order)
        editor.putString(ORDER_KEY, orderJson)

        // Commit the changes
        editor.apply()
    }

    private fun convertOrderToJson(order: Order): String {
        val jsonObject = JSONObject()
        jsonObject.put("id", order.id)
        jsonObject.put("cinema", order.cinema)

        val itemsArray = JSONArray()
        for (item in order.items) {
            val itemObject = JSONObject()
            itemObject.put("category", item.category)
            itemObject.put("totalPrice", item.totalPrice)
            itemObject.put("quantity", item.quantity)
            itemsArray.put(itemObject)
        }

        jsonObject.put("items", itemsArray)

        return jsonObject.toString()
    }
}