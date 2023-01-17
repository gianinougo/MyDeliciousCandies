package com.ugogianino.mydeliciouscandies.utils

import android.util.Patterns

class Utilidades {

    private fun isValidUrl(url: String): Boolean {
        val pattern = Patterns.WEB_URL
        val matcher = pattern.matcher(url)
        return matcher.matches()
    }



}