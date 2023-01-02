package com.erif.quicktooltips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    private var tooltips: QuickTooltipsBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tooltips = QuickTooltipsBuilder(this)

        val switch: SwitchCompat = findViewById(R.id.act_main_switch)
        val btn: Button = findViewById(R.id.act_main_btn)

        tooltips?.targetView(switch)
        tooltips?.show()

    }

}