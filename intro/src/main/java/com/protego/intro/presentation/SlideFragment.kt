package com.protego.intro.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.protego.intro.databinding.FragmentSlideBinding

class SlideFragment : Fragment() {

    private var binding: FragmentSlideBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlideBinding.inflate(inflater, container, false)
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { arguments ->
            val entity = SlideShowElement.fromString(arguments.getString(SLIDESHOW_DATA, ""))

            binding?.image?.setImageResource(entity.image)
            binding?.text?.text = entity.text
        }
    }

    companion object {
        fun newInstance(slideShowElement: SlideShowElement) = SlideFragment().apply {
            arguments = bundleOf(SLIDESHOW_DATA to slideShowElement.toString())
        }

        const val SLIDESHOW_DATA = "slideshowdata"
    }
}