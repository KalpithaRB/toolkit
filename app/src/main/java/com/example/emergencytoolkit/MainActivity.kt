package com.example.emergencytoolkit

//import EmergencyContactsActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.emergencytoolkit.components.*
import android.telephony.SmsManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices




class MainActivity : AppCompatActivity() {
    private val SMSPERMISSIONCODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayingAlert = false



    private val authorityNumber = "1234567890"  // Dummy number

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val medicalDetailsTextView = findViewById<TextView>(R.id.tvMedicalDetails)

        val sharedPrefs = getSharedPreferences("MedicalInfoPrefs", MODE_PRIVATE)
        val name = sharedPrefs.getString("name", "Not set")
        val bloodType = sharedPrefs.getString("blood_type", "Not set")
        val allergies = sharedPrefs.getString("allergies", "None")

        val medicalInfo = "Name: $name\nBlood Type: $bloodType\nAllergies: $allergies"
        medicalDetailsTextView.text = medicalInfo
        val medicalCard = findViewById<CardView>(R.id.cardMedicalInfo)
        medicalCard.setOnClickListener {
            startActivity(Intent(this, MedicalInfoActivity::class.java))
        }

        val btnAlertSound = findViewById<Button>(R.id.btnAlertSound)
        btnAlertSound.setOnClickListener {
            toggleAlertSound()
        }




        val sosBtn = findViewById<Button>(R.id.sosButton)
        val settingsIcon = findViewById<ImageView>(R.id.settingsIcon)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        sosBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION),
                    SMSPERMISSIONCODE
                )
            } else {
                sendSOSMessage()
            }}

        settingsIcon.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Example: Navigation buttons (you’ll create these activities later)
        findViewById<Button>(R.id.btnContacts).setOnClickListener {
            startActivity(Intent(this, EmergencyContactsActivity::class.java))

        }

//        findViewById<Button>(R.id.btnFlashlight).setOnClickListener {
//            startActivity(Intent(this, FlashlightActivity::class.java))
//        }

        findViewById<Button>(R.id.btnAlertSound).setOnClickListener {
            startActivity(Intent(this, AlertSoundActivity::class.java))
        }

        findViewById<Button>(R.id.btnFirstAid).setOnClickListener {
            val intent = Intent(this, FirstAidActivity::class.java)
            intent.putExtra("topic", "First Aid") // You can customize this topic later
            startActivity(intent)
        }

        val compassButton = findViewById<Button>(R.id.btnCompass)
        compassButton.setOnClickListener {
            val intent = Intent(this, CompassActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnMap).setOnClickListener {
            val inten = Intent(this, NearbyMapActivity::class.java)
            startActivity(inten)
        }



    }

    private fun sendSOSMessage() {
        val sharedPrefs = getSharedPreferences("EmergencyPrefs", MODE_PRIVATE)
        val number1 = sharedPrefs.getString("emergency_number_1", null)
        val number2 = sharedPrefs.getString("emergency_number_2", null)
        val number3 = sharedPrefs.getString("emergency_number_3", null)

        val contactsList = mutableListOf<String>()
        val labelsList = mutableListOf<String>()

        if (number1 != null) {
            contactsList.add(number1)
            labelsList.add("Contact 1: $number1")
        }
        if (number2 != null) {
            contactsList.add(number2)
            labelsList.add("Contact 2: $number2")
        }
        if (number3 != null) {
            contactsList.add(number3)
            labelsList.add("Contact 3: $number3")
        }

        if (contactsList.isEmpty()) {
            Toast.makeText(this, "No emergency contacts found!", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedItems = BooleanArray(contactsList.size)

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Select contacts to send SOS")

        builder.setMultiChoiceItems(labelsList.toTypedArray(), selectedItems) { _, which, isChecked ->
            selectedItems[which] = isChecked
        }

        builder.setPositiveButton("Send") { _, _ ->
            val selectedNumbers = contactsList.filterIndexed { index, _ -> selectedItems[index] }

            if (selectedNumbers.isEmpty()) {
                Toast.makeText(this, "No contacts selected!", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            // Check location permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            // Get location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val mapsLink = "https://maps.google.com/?q=$latitude,$longitude"

                    val message = "🚨 EMERGENCY! I need help. This is my location: $mapsLink"

                    val smsUri = Uri.parse("smsto:" + selectedNumbers.joinToString(";"))
                    val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                    smsIntent.putExtra("sms_body", message)

                    startActivity(smsIntent)
                } else {
                    Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMSPERMISSIONCODE) {
            if (grantResults.isNotEmpty() &&
                grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                sendSOSMessage()
            } else {
                Toast.makeText(this, "Required permissions denied", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loadMedicalInfo() {
        val prefs = getSharedPreferences("MedicalInfoPrefs", MODE_PRIVATE)
        val name = prefs.getString("name", "John Doe")
        val bloodType = prefs.getString("blood_type", "O+")
        val allergies = prefs.getString("allergies", "None")

        val medicalDetailsText = "Name: $name\nBlood Type: $bloodType\nAllergies: $allergies"

        val tvMedicalDetails = findViewById<TextView>(R.id.tvMedicalDetails)
        tvMedicalDetails.text = medicalDetailsText
    }
    override fun onResume() {
        super.onResume()
        loadMedicalInfo() // Refresh card when returning to home screen
    }

    private fun toggleAlertSound() {
        if (isPlayingAlert) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isPlayingAlert = false
            Toast.makeText(this, "Alert sound stopped", Toast.LENGTH_SHORT).show()
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.siren)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            isPlayingAlert = true
            Toast.makeText(this, "Alert sound playing", Toast.LENGTH_SHORT).show()
        }
    }



}
