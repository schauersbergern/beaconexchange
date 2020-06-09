package com.protego.permissions.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.protego.permissions.R
import com.protego.permissions.databinding.FragmentPermissionBinding
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

    private fun goToMain() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        optimizeBattery()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        optimizeBattery()
    }

    private fun optimizeBattery() {
        if (ignoresBatteryOptimizations()) {
            goToMain()
        } else {
            activateBatteryOptimizations()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == BATTERY_OPTIMIZATIONS_REQUEST) {
            goToMain()
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
            EasyPermissions.requestPermissions(this, getString(R.string.permission_needed), LOCATION, *permissions)
        } else {
            goToMain()
        }
    }

    companion object {
        const val LOCATION = 1
        const val BATTERY_OPTIMIZATIONS_REQUEST = 25
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.WAKE_LOCK
        )
    }
}