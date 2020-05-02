package com.example.intro.presentation

import androidx.lifecycle.ViewModel
import com.example.intro.R

class SlideShowViewModel :ViewModel() {

    val slideShowData by lazy {
        listOf(
            SlideShowElement(
                "Download the App and turn on your Bluetooth",
                R.drawable.slideshow_img_1
            ),
            SlideShowElement(
                "Allow always access to location and notifications",
                R.drawable.slideshow_img_2
            ),
            SlideShowElement(
                "If you keep less than 2m distance to someone else, Protego will alert you with vibration and sound.",
                R.drawable.slideshow_img_3
            ),
            SlideShowElement(
                "Your phone might alert you with someone or something you decide to be close to. Whitelist it and  don’t get alerts with them again.",
                R.drawable.slideshow_img_4
            )
        )
    }
}