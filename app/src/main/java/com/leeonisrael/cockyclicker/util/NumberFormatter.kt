package com.leeonisrael.cockyclicker.util

import java.text.DecimalFormat

object NumberFormatter {
    private val decimalFormat = DecimalFormat("#.##")

    fun format(number: Long): String {
        return when {
            number >= 1_000_000_000_000 -> "${decimalFormat.format(number / 1_000_000_000_000.0)}T"
            number >= 1_000_000_000 -> "${decimalFormat.format(number / 1_000_000_000.0)}B"
            number >= 1_000_000 -> "${decimalFormat.format(number / 1_000_000.0)}M"
            number >= 1_000 -> "${decimalFormat.format(number / 1_000.0)}K"
            else -> number.toString()
        }
    }
}
