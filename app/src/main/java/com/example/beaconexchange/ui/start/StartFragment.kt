package com.example.beaconexchange.ui.start

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.beaconexchange.Constants.Companion.BEACON_MESSAGE
import com.example.beaconexchange.Constants.Companion.BEACON_UPDATE
import com.example.beaconexchange.R
import com.example.beaconexchange.beacon.BeaconSenderService
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment() {

    private lateinit var viewModel: StartViewModel

    private val serviceRuns : MutableLiveData<Boolean> = MutableLiveData(false)

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra(BEACON_MESSAGE)
            textView3.text = message
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver, IntentFilter(BEACON_UPDATE)
        )

        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)

        serviceRuns.observe(viewLifecycleOwner, Observer {
            if (it) {
                serviceButton.text = "Stop sending"
            } else {
                serviceButton.text = "Start sending "
            }
        })

        if (requireContext().isServiceRunning(BeaconSenderService::class.java)) {
            serviceRuns.value = true
        }

        serviceButton.setOnClickListener {
            if(serviceRuns.value == true) {
                activity?.stopService(Intent(requireContext(), BeaconSenderService::class.java))
                serviceRuns.value = false
            } else {
                activity?.startService(Intent(requireContext(), BeaconSenderService::class.java))
                serviceRuns.value = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

    @Suppress("DEPRECATION")
    fun <T> Context.isServiceRunning(service: Class<T>) = (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == service.name }

}
