package com.example.emergencytoolkit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MedicalInfoActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_info)

        val etName = findViewById<EditText>(R.id.etName)
        val etBloodType = findViewById<EditText>(R.id.etBloodType)
        val etAllergies = findViewById<EditText>(R.id.etAllergies)
        val btnSave = findViewById<Button>(R.id.btnSaveMedical)

        // Load existing info if available
        val prefs = getSharedPreferences("MedicalInfoPrefs", Context.MODE_PRIVATE)
        etName.setText(prefs.getString("name", ""))
        etBloodType.setText(prefs.getString("blood_type", ""))
        etAllergies.setText(prefs.getString("allergies", ""))

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val bloodType = etBloodType.text.toString().trim()
            val allergies = etAllergies.text.toString().trim()

            prefs.edit().apply {
                putString("name", name)
                putString("blood_type", bloodType)
                putString("allergies", allergies)
                apply()
            }

            Toast.makeText(this, "Medical Info Saved!", Toast.LENGTH_SHORT).show()
            finish()  // Close the screen
        }
    }
}
