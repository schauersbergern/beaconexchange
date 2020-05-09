package com.example.beaconexchange

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intro.presentation.IntroActivity
import org.altbeacon.beacon.BeaconManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (shouldShowOnboarding()) {
            startIntro()
            return
        }

        init()
    }

    private fun init() {
        setContentView(R.layout.activity_main)
        verifyBluetooth()

        if (!bluetoothLeIsAvailable()) {
            Toast.makeText(this, "Blueooth is not supported", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startIntro() {
        Intent(this, IntroActivity::class.java).apply {
            startActivityForResult(this, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            getPreferences(Context.MODE_PRIVATE)?.edit()?.putBoolean("SHOWOB", false)?.commit()
            init()
        }
    }

    private fun shouldShowOnboarding() : Boolean{
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean("SHOWOB", true)
    }

    private fun bluetoothLeIsAvailable() : Boolean {
        val man = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = man.adapter
        return adapter != null
    }

    private fun verifyBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Bluetooth not enabled")
                builder.setMessage("Please enable bluetooth in settings and restart this application.")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.show()
            }
        } catch (e: RuntimeException) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Bluetooth LE not available")
            builder.setMessage("Sorry, this device does not support Bluetooth LE.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.show()
        }
    }
}
