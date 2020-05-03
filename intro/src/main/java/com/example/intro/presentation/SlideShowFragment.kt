package com.example.intro.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.intro.databinding.FragmentSlideshowBinding

class SlideShowFragment : Fragment() {

    private var binding: FragmentSlideshowBinding? = null
    private val _binding get() = binding

    private val viewModel: SlideShowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViewPager()
        initUI()
    }

    private fun initUI() {
        binding?.quitSlideshowButton?.setOnClickListener {
            findNavController().navigate(SlideShowFragmentDirections.moveToPermissions())
        }
    }

    private fun setupViewPager() {
        var items = viewModel.slideShowData.size
        val viewPager = binding?.viewPager ?: return
        viewPager.adapter = SlideShowAdapter(this, viewModel.slideShowData)
        viewPager.setPageTransformer(MarginPageTransformer(VIEWPAGER_MARGIN))
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding?.indicators?.selection = position

                if (position+1 == items) {
                    binding?.quitSlideshowButton?.visibility = View.VISIBLE
                } else {
                    binding?.quitSlideshowButton?.visibility = View.GONE
                }
            }
        })
    }

    companion object {
        const val VIEWPAGER_MARGIN = 600
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}