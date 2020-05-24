package com.example.beaconexchange.ui.whitelist

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
import com.example.beaconexchange.databinding.FragmentWhitelistBinding

class WhiteListFragment : Fragment() {

    private lateinit var viewModel : WhiteListViewModel
    private var binding: FragmentWhitelistBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWhitelistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(WhiteListViewModel::class.java)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.whitelist?.setHasFixedSize(true)
        binding?.whitelist?.layoutManager = LinearLayoutManager(requireContext())

        viewModel.devices.observe(viewLifecycleOwner, Observer { devices ->
            if (devices.isNotEmpty()) {
                binding?.whitelist?.adapter = WhiteListAdapter(devices)
                binding?.noEntries?.visibility = GONE
            } else {
                binding?.noEntries?.visibility = VISIBLE
            }
        })

    }
}