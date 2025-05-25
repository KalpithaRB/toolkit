package com.example.emergencytoolkit

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FirstAidActivity : AppCompatActivity() {

    private val topics = listOf(
        "Snake Bite",
        "Knife Cut",
        "Burns",
        "Nose Bleed",
        "Fainting"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid)

        val listView = findViewById<ListView>(R.id.firstAidList)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topics)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, FirstAidDetailActivity::class.java)
            intent.putExtra("topic", topics[position])
            startActivity(intent)
        }
    }
}
