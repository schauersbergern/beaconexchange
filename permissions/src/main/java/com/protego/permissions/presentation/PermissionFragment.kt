package com.protego.permissions.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protego.permissions.R
import com.protego.permissions.databinding.FragmentPermissionBinding
import com.protego.permissions.presentation.Constants.Companion.ONBOARDING_KEY
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class PermissionFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var binding: FragmentPermissionBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.grantButton?.setOnClickListener {
            getPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun finishHim() {
        requireActivity().getPreferences(Context.MODE_PRIVATE)?.edit()?.putBoolean(ONBOARDING_KEY, false)?.apply()
        findNavController().navigate(Uri.parse("https://www.protego.com/start"))
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        optimizeBattery()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        optimizeBattery()
    }

    private fun optimizeBattery() {
        if (ignoresBatteryOptimizations()) {
            finishHim()
        } else {
            activateBatteryOptimizations()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == BATTERY_OPTIMIZATIONS_REQUEST) {
            finishHim()
        }
    }

     override fun onRequestPermissionsResult(
         requestCode: Int,
         permissions: Array<out String>,
         grantResults: IntArray
     ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(LOCATION)
    private fun getPermissions() {
        if (!hasPermissions(requireContext())) {
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.Q){
                EasyPermissions.requestPermissions(this, getString(R.string.permission_needed), LOCATION, *permissionsQ)
            } else{
                EasyPermissions.requestPermissions(this, getString(R.string.permission_needed), LOCATION, *permissions)
            }
        }
    }

    companion object {
        const val LOCATION = 1
        const val BATTERY_OPTIMIZATIONS_REQUEST = 25
        val permissionsQ = arrayOf(
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WAKE_LOCK
        )
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WAKE_LOCK
        )
    }
}