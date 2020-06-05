package com.protego.intro.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SlideShowAdapter(fragment: Fragment, private val data: List<SlideShowElement>) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int) = SlideFragment.newInstance(data[position])
    override fun getItemCount() = data.size
}