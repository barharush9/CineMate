package com.example.myfirstmovieapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_poster = findViewById<ImageView>(R.id.btn_poster)
        btn_poster.setOnClickListener {
            val MoveToPoster = Intent(this, PosterActivity::class.java)
            startActivity(MoveToPoster)
        }
        val btn_ticket = findViewById<ImageView>(R.id.btn_ticket)
        btn_ticket.setOnClickListener {
            val MoveToticket = Intent(this, TicketActivity::class.java)
            startActivity(MoveToticket)
        }
        val btn_pur = findViewById<ImageView>(R.id.btn_prucheseHistory)
        btn_pur.setOnClickListener {
            val pur_h  = Intent(this, PruchaseHistory::class.java)
            startActivity(pur_h)
        }
        val btn_info = findViewById<ImageView>(R.id.btn_info)
        btn_info.setOnClickListener {
            val info  = Intent(this, InfoActivity::class.java)
            startActivity(info)
        }





    }
}




