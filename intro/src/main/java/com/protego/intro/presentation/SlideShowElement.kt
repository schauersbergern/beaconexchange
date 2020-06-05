package com.protego.intro.presentation

import com.google.gson.Gson

data class SlideShowElement(val text: String, val image: Int) {
    override fun toString() = Gson().toJson(this)

    companion object {
        fun fromString(string: String) = Gson().fromJson(string, SlideShowElement::class.java)
    }
}