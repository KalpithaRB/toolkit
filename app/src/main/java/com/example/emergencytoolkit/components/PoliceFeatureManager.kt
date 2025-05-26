package com.example.emergencytoolkit.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class PoliceFeatureManager(private val activity: Activity) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun fetchNearestPoliceStation(tvPoliceStation: TextView) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(activity, Locale.getDefault())
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val city = address?.get(0)?.locality ?: "your area"
                tvPoliceStation.text = "Nearest Police Station: $city Police Station"
            } else {
                tvPoliceStation.text = "Unable to fetch location"
            }
        }
    }

    fun setupPoliceCallButton(button: ImageButton) {
        button.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:100")

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    1002
                )
                return@setOnClickListener
            }

            Toast.makeText(activity, "Calling Police...", Toast.LENGTH_SHORT).show()
            activity.startActivity(callIntent)
        }
    }


}
