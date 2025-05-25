package com.example.emergencytoolkit.components

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.emergencytoolkit.R

class MedicalInfoWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            val prefs = context.getSharedPreferences("MedicalInfoPrefs", Context.MODE_PRIVATE)
            val name = prefs.getString("name", "John Doe")
            val bloodType = prefs.getString("blood_type", "O+")
            val allergies = prefs.getString("allergies", "None")

            val info = "Name: $name\nBlood Type: $bloodType\nAllergies: $allergies"

            val views = RemoteViews(context.packageName, R.layout.widget_medical_info)
            views.setTextViewText(R.id.widgetDetails, info)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }
}
