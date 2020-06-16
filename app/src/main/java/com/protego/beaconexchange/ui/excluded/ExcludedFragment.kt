package com.protego.beaconexchange.ui.excluded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.protego.beaconexchange.databinding.FragmentExcludedBinding

class ExcludedFragment : Fragment() {

    private lateinit var viewModel : ExcludedViewModel
    private var binding: FragmentExcludedBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExcludedBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ExcludedViewModel::class.java)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.excludedList?.setHasFixedSize(true)
        binding?.excludedList?.layoutManager = LinearLayoutManager(requireContext())

        viewModel.excludedLive.observe(viewLifecycleOwner, Observer { devices ->
            if (devices.isNotEmpty()) {
                binding?.noEntries?.visibility = GONE
            } else {
                binding?.noEntries?.visibility = VISIBLE
            }
            binding?.excludedList?.adapter = ExcludedAdapter(devices) {
                viewModel.deleteFromExcluded(it)
            }
        })

    }
}