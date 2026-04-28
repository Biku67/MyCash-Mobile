package com.bily.mycash.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyFormatter {

    private val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val formatter = DecimalFormat("#,###", symbols)

    /**
     * Format angka menjadi format Rupiah: "Rp 1.500.000"
     */
    fun format(amount: Long): String {
        return "Rp ${formatter.format(amount)}"
    }

    /**
     * Format angka tanpa prefix "Rp": "1.500.000"
     */
    fun formatNumber(amount: Long): String {
        return formatter.format(amount)
    }

    /**
     * Format dengan tanda +/- : "+Rp 500.000" atau "-Rp 150.000"
     */
    fun formatSigned(amount: Long, isIncome: Boolean): String {
        val sign = if (isIncome) "+" else "-"
        return "${sign}Rp ${formatter.format(amount)}"
    }
}
