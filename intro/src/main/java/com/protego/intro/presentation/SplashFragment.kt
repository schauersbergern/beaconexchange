package com.protego.intro.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protego.intro.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var binding: FragmentSplashBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.splashButton?.setOnClickListener {
            findNavController().navigate(SplashFragmentDirections.showIntroSlides())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}