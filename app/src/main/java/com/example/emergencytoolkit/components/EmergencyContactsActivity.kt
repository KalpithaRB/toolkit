package com.example.emergencytoolkit

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmergencyContactsActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    private val PICK_CONTACT_1 = 1
    private val PICK_CONTACT_2 = 2
    private val PICK_CONTACT_3 = 3

    private lateinit var contact1Text: TextView
    private lateinit var contact2Text: TextView
    private lateinit var contact3Text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contact)

        sharedPrefs = getSharedPreferences("EmergencyPrefs", MODE_PRIVATE)

        contact1Text = findViewById(R.id.contact1Text)
        contact2Text = findViewById(R.id.contact2Text)
        contact3Text = findViewById(R.id.contact3Text)

        findViewById<Button>(R.id.selectContact1).setOnClickListener {
            pickContact(PICK_CONTACT_1)
        }
        findViewById<Button>(R.id.selectContact2).setOnClickListener {
            pickContact(PICK_CONTACT_2)
        }
        findViewById<Button>(R.id.selectContact3).setOnClickListener {
            pickContact(PICK_CONTACT_3)
        }

        loadSavedContacts()
    }

    private fun pickContact(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val contactUri: Uri = data.data ?: return
            val cursor = contentResolver.query(contactUri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val number = it.getString(numberIndex)

                    when (requestCode) {
                        PICK_CONTACT_1 -> saveContact("emergency_number_1", number, contact1Text)
                        PICK_CONTACT_2 -> saveContact("emergency_number_2", number, contact2Text)
                        PICK_CONTACT_3 -> saveContact("emergency_number_3", number, contact3Text)
                    }
                }
            }
        }
    }

    private fun saveContact(key: String, number: String, view: TextView) {
        sharedPrefs.edit().putString(key, number).apply()
        view.text = number
        Toast.makeText(this, "Contact saved!", Toast.LENGTH_SHORT).show()
    }

    private fun loadSavedContacts() {
        contact1Text.text = sharedPrefs.getString("emergency_number_1", "Not Set")
        contact2Text.text = sharedPrefs.getString("emergency_number_2", "Not Set")
        contact3Text.text = sharedPrefs.getString("emergency_number_3", "Not Set")
    }
}
