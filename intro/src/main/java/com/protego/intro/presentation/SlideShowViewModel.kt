package com.protego.intro.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.protego.intro.R

class SlideShowViewModel(application: Application) : AndroidViewModel(application) {

    val slideShowData by lazy {
        listOf(
            SlideShowElement(
                application.getString(R.string.slideshow_1),
                R.drawable.slideshow_img_1
            ),
            SlideShowElement(
                application.getString(R.string.slideshow_2),
                R.drawable.slideshow_img_2
            ),
            SlideShowElement(
                application.getString(R.string.slideshow_3),
                R.drawable.slideshow_img_3
            ),
            SlideShowElement(
                application.getString(R.string.slideshow_4),
                R.drawable.slideshow_img_4
            )
        )
    }
}