package com.example.emergencytoolkit

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FirstAidDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid_detail)

        val topic = intent.getStringExtra("topic") ?: "General"
        val firstAidTextView: TextView = findViewById(R.id.firstAidText)
        firstAidTextView.text = getStepsForTopic(topic)
    }

    private fun getStepsForTopic(topic: String): String {
        return when (topic) {
            "Snake Bite" -> """
                Snake Bite First Aid:
                • Keep the person calm and still.
                • Immobilize the bitten area.
                • Do NOT suck out the venom.
                • Get emergency help ASAP.
                • Monitor breathing and pulse.
            """.trimIndent()

            "Knife Cut" -> """
                Knife Cut First Aid:
                • Apply pressure to stop bleeding.
                • Clean the wound with clean water.
                • Use a clean bandage.
                • Seek help if the cut is deep.
                • Watch for infection signs.
            """.trimIndent()

            else -> "No specific instructions found for $topic."
        }
    }
}