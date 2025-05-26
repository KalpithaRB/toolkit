package com.example.emergencytoolkit.components

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.emergencytoolkit.R

class EmergencyNumbersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_numbers)

        findViewById<Button>(R.id.btnPolice).setOnClickListener {
            dialNumber("100")
        }

        findViewById<Button>(R.id.btnFire).setOnClickListener {
            dialNumber("101")
        }

        findViewById<Button>(R.id.btnAmbulance).setOnClickListener {
            dialNumber("108")
        }

        findViewById<Button>(R.id.btnChildHelp).setOnClickListener {
            dialNumber("1098")
        }
    }

    private fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }
}
