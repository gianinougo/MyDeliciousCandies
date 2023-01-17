package com.ugogianino.mydeliciouscandies.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast

class Utilidades {

    private val applicationContext: Context? = null

    private fun isValidUrl(url: String): Boolean {
        val pattern = Patterns.WEB_URL
        val matcher = pattern.matcher(url)
        return matcher.matches()
    }


    fun validateInputs(
        name: String,
        manufacturer: String,
        candyType: String,
        format: String,
        sweetness: String,
        url: String
    ): Boolean {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(manufacturer) || TextUtils.isEmpty(
                candyType
            ) || TextUtils.isEmpty(format) || TextUtils.isEmpty(sweetness) || TextUtils.isEmpty(url)
        ) {
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_SHORT).show()

            return false
        }

        val sweetnessRegex = "^[1-5]$".toRegex()
        if (!sweetnessRegex.matches(sweetness)) {
            Toast.makeText(
                applicationContext,
                "Sweetness must be a number between 1 and 5",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val urlRegex =
            "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?\$".toRegex()
        if (!urlRegex.matches(url)) {
            Toast.makeText(applicationContext, "Invalid url", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }


}