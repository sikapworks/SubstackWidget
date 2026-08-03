package uk.ac.tees.mad.substackwidget

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(rawDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MMM d", Locale.ENGLISH)
        val date = inputFormat.parse(rawDate)
        outputFormat.format(date ?: return rawDate)
    } catch (e: Exception) {
        rawDate
    }
}