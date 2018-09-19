package com.blockchain.morph.dev

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.blockchain.morph.ui.homebrew.exchange.history.TradeHistoryActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchExchange(view: View) {
        val intent = Intent(this, TradeHistoryActivity::class.java)
        startActivity(intent)
    }
}