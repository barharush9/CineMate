package com.example.myfirstmovieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TicketActivity : AppCompatActivity() {



    var selectedCinema: String? = null
    var chooseCategorySpinner1: Spinner? = null
    var quantityTextView1: TextView? = null
    var quantitySeekBar1: SeekBar? = null
    var totalPriceTextView1: TextView? = null

    var chooseCategorySpinner2: Spinner? = null
    var quantityTextView2: TextView? = null
    var quantitySeekBar2: SeekBar? = null
    var totalPriceTextView2: TextView? = null
    var cinemaSelection: Button? = null

    var chooseCategorySpinner3: Spinner? = null
    var quantityTextView3: TextView? = null
    var quantitySeekBar3: SeekBar? = null
    var totalPriceTextView3: TextView? = null

    private var purchaseButton: Button? = null
    private fun getPricePerTicket(category: String): Int {
        return when (category) {

            getString(R.string.category_child) -> 15
            getString(R.string.Adult) -> 22
            getString(R.string.category_student) -> 18
            else -> 0
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)
        initializeViews()
        setListeners()
    }
    fun initializeViews() {
        chooseCategorySpinner1 = findViewById(R.id.chooseCategorySpinner1)
        quantityTextView1 = findViewById(R.id.quantityTextView1)
        quantitySeekBar1 = findViewById(R.id.quantitySeekBar1)
        totalPriceTextView1 = findViewById(R.id.totalPriceTextView1)
        cinemaSelection = findViewById(R.id.btn_cinemaSelection)

        chooseCategorySpinner2 = findViewById(R.id.chooseCategorySpinner2)
        quantityTextView2 = findViewById(R.id.quantityTextView2)
        quantitySeekBar2 = findViewById(R.id.quantitySeekBar2)
        totalPriceTextView2 = findViewById(R.id.totalPriceTextView2)

        chooseCategorySpinner3 = findViewById(R.id.chooseCategorySpinner3)
        quantityTextView3 = findViewById(R.id.quantityTextView3)
        quantitySeekBar3 = findViewById(R.id.quantitySeekBar3)
        totalPriceTextView3 = findViewById(R.id.totalPriceTextView3)

        purchaseButton = findViewById(R.id.orderButton)
    }

    private fun setListeners() {
        cinemaSelection?.setOnClickListener {
            showListDialogSelectCinema()
        }

        val seekBars = arrayOf(quantitySeekBar1, quantitySeekBar2, quantitySeekBar3)

        seekBars.forEachIndexed { index, seekBar ->
            seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    calculateAndUpdateTotalPrice()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    calculateAndUpdateTotalPrice()
                }
            })
        }

        purchaseButton?.setOnClickListener {
          val flag=(quantitySeekBar1?.progress ?: 0) +
                  (quantitySeekBar2?.progress ?: 0) +
                  (quantitySeekBar3?.progress ?: 0)
            val flag2=selectedCinema
            if (flag2==null)
            {   showDialogNoCinemaSelected()  }
          else if (flag==0)
          {showDialogNoTicketsSelected()

          }
          else {

              val intent = Intent(this, OrderActivity::class.java).apply {
                  putExtra(
                      "totalPrice1", calculateTotalPriceForSingleItem(
                          chooseCategorySpinner1?.selectedItem?.toString(),
                          quantitySeekBar1?.progress ?: 0
                      )
                  )
                  putExtra(
                      "totalPrice2", calculateTotalPriceForSingleItem(
                          chooseCategorySpinner2?.selectedItem?.toString(),
                          quantitySeekBar2?.progress ?: 0
                      )
                  )
                  putExtra(
                      "totalPrice3", calculateTotalPriceForSingleItem(
                          chooseCategorySpinner3?.selectedItem?.toString(),
                          quantitySeekBar3?.progress ?: 0
                      )
                  )
                  putExtra("quantity1", quantitySeekBar1?.progress ?: 0)
                  putExtra("quantity2", quantitySeekBar2?.progress ?: 0)
                  putExtra("quantity3", quantitySeekBar3?.progress ?: 0)
                  putExtra("selectedCinema", selectedCinema)
                  putExtra("selectedCategory1", chooseCategorySpinner1?.selectedItem?.toString())
                  putExtra("selectedCategory2", chooseCategorySpinner2?.selectedItem?.toString())
                  putExtra("selectedCategory3", chooseCategorySpinner3?.selectedItem?.toString())

              }

              startActivity(intent)
          }
        }
    }



    fun calculateAndUpdateTotalPrice() {
        val totalPrice1 = calculateTotalPriceForSingleItem(
            chooseCategorySpinner1?.selectedItem?.toString(),
            quantitySeekBar1?.progress ?: 0
        )

        val totalPrice2 = calculateTotalPriceForSingleItem(
            chooseCategorySpinner2?.selectedItem?.toString(),
            quantitySeekBar2?.progress ?: 0
        )

        val totalPrice3 = calculateTotalPriceForSingleItem(
            chooseCategorySpinner3?.selectedItem?.toString(),
            quantitySeekBar3?.progress ?: 0
        )

        val totalPricesSum = totalPrice1 + totalPrice2 + totalPrice3


        val totalSelectedTickets = getTotalSelectedTickets()

        if (totalSelectedTickets == 0 || totalPricesSum == 0) {
            showDialogNoTicketsSelected()
            return
        }

        quantityTextView1?.text = "${getString(R.string.quantity)} ${quantitySeekBar1?.progress ?: 0}"
        quantityTextView2?.text = "${getString(R.string.quantity)} ${quantitySeekBar2?.progress ?: 0}"
        quantityTextView3?.text = "${getString(R.string.quantity)} ${quantitySeekBar3?.progress ?: 0}"

        totalPriceTextView1?.text = "${getString(R.string.price)} $totalPrice1"
        totalPriceTextView2?.text = "${getString(R.string.price)} $totalPrice2"
        totalPriceTextView3?.text = "${getString(R.string.price)} $totalPrice3"
    }

    private fun calculateTotalPriceForSingleItem(category: String?, quantity: Int): Int {
        val pricePerTicket = getPricePerTicket(category ?: "")
        return quantity * pricePerTicket
    }

    private fun getTotalSelectedTickets(): Int {
        return (quantitySeekBar1?.progress ?: 0) +
                (quantitySeekBar2?.progress ?: 0) +
                (quantitySeekBar3?.progress ?: 0)
    }

    private fun showDialogNoTicketsSelected() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.DialogTitle)
            .setMessage(getString(R.string.warning_amount))
            .setPositiveButton(getString(R.string.Ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun showListDialogSelectCinema() {
        val items = resources.getStringArray(R.array.selectcinema)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.SelectCinema))
            .setItems(items) { _, which ->
                selectedCinema = items[which]
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showDialogNoCinemaSelected() {

            val alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.DialogTitle)
                .setMessage(getString(R.string.Choose_Cinema))
                .setPositiveButton(getString(R.string.Ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()

    }
}









