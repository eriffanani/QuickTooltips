package com.erif.quicktooltips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    private var tooltips: QuickTooltips? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tooltips = QuickTooltips(this)

        val switch: SwitchCompat = findViewById(R.id.act_main_switch)
        val btn: Button = findViewById(R.id.act_main_btn)

        tooltips?.targetView(switch, true)
        tooltips?.setTitle("Title of My Tooltips")
        tooltips?.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.")

        /*val item = QuickTooltips.Item(
            btn, "Google Meet, now in Gmail",
            "Ini adalah deskripsi Ini adalah deskripsi Ini adalah deskripsi",
            "Learn More",
            "Got It!"
        )

        tooltips?.addItem(item)*/
        tooltips?.setImage(R.mipmap.illustration_example)
        tooltips?.setClosable(true)
        tooltips?.setShape(QuickTooltips.SHAPE_CIRCLE)
        tooltips?.onClickListener(tooltipsLauncher)

        btn.setOnClickListener {
            tooltips?.show()
        }

    }

    private val tooltipsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val id = QuickTooltips.getId(result)
        val buttonSecondary = QuickTooltips.isButtonSecondary(result)
        val buttonPrimary = QuickTooltips.isButtonPrimary(result)
        if (buttonSecondary) {
            Toast.makeText(this, "Secondary: $id", Toast.LENGTH_SHORT).show()
        } else if (buttonPrimary) {
            Toast.makeText(this, "Primary: $id", Toast.LENGTH_SHORT).show()
        }
    }

}