package br.eti.rafaelcouto.temporada

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(pattern: String = "yyyy-MM-dd"): Date {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    try {
        dateFormat.parse(this)?.let {
            return it
        } ?: throw java.lang.IllegalArgumentException()
    } catch (e: Exception) {
        throw IllegalArgumentException("The string $this does not conform to the pattern $pattern.")
    }
}

fun Date.toDateString(format: String = "dd/MM/yyyy"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())

    return dateFormat.format(this)
}
