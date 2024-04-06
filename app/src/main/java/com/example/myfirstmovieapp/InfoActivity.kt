package com.example.myfirstmovieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        val btn_main = findViewById<Button>(R.id.main_return)
        btn_main.setOnClickListener {
            val main_return = Intent(this, MainActivity::class.java)
            startActivity(main_return)

            }
            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            showRatingDialog(rating)
        }



    }

    private   fun showRatingDialog(rating: Float) {
        val thankyou=getString(R.string.Thankyouforthe)
        val stars=getString(R.string.stars)
        val ratingToInt:Int= rating.toInt()

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.rating)
        alertDialogBuilder.setMessage("$thankyou $ratingToInt $stars ! ")

        alertDialogBuilder.setPositiveButton(R.string.Ok) { _, _ ->
            finish()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }



}

