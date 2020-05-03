package com.example.beaconexchange.ui.start

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.beaconexchange.AlarmManager.Companion.SEVERITY_MEDIUM
import com.example.beaconexchange.AlarmManager.Companion.SEVERITY_SEVERE
import com.example.beaconexchange.AlarmManager.Companion.getSeverity
import com.example.beaconexchange.BluetoothMessage
import com.example.beaconexchange.Constants.Companion.BEACON_MESSAGE
import com.example.beaconexchange.Constants.Companion.BEACON_UPDATE
import com.example.beaconexchange.R
import com.example.beaconexchange.beacon.BeaconSenderService
import com.example.beaconexchange.databinding.FragmentStartBinding
import com.example.beaconexchange.isServiceRunning

class StartFragment : Fragment() {

    private val serviceShouldRun : MutableLiveData<Boolean> = MutableLiveData(false)

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val message = intent.getParcelableExtra<BluetoothMessage>(BEACON_MESSAGE)

            val data = "The beacon with Address ${message.blueToothAddress} has the name " +
                    "${message.blueToothName} is about ${message.distCentimeters} centimeters away " +
                    "and has rssi of ${message.rssi}"


            when (getSeverity(message.distCentimeters)) {
                SEVERITY_MEDIUM -> {
                    binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_alert))
                    binding?.mainDistanceSave?.visibility = View.INVISIBLE
                }
                SEVERITY_SEVERE -> {
                    binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_alert))
                    binding?.mainDistanceSave?.visibility = View.INVISIBLE
                }
                else -> {
                    binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_on))
                    binding?.mainDistanceSave?.visibility = View.VISIBLE
                }

            }
        }
    }

    private var binding: FragmentStartBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver, IntentFilter(BEACON_UPDATE)
        )

        serviceShouldRun.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_on))
                binding?.mainDistanceSave?.visibility = View.VISIBLE
                binding?.mainDistanceSave?.text = getString(R.string.distance_save)
                binding?.mainTrackerState?.text = getText(R.string.tracker_on)
                startService()
            } else {
                binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_off))
                binding?.mainDistanceSave?.visibility = View.VISIBLE
                binding?.mainDistanceSave?.text = getString(R.string.distance_off)
                binding?.mainTrackerState?.text = getText(R.string.tracker_off)
                stopService()
            }
        })

        if (requireContext().isServiceRunning(BeaconSenderService::class.java)) {
            serviceShouldRun.postValue(true)
            toggleTextState(true)
        }

        binding?.mainSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                serviceShouldRun.postValue(true)
            } else {
                serviceShouldRun.postValue(false)
            }
            toggleTextState(isChecked)
        }
    }

    private fun toggleTextState(serviceRuns: Boolean) {
        if (serviceRuns) {
            binding?.trackerOn?.typeface = Typeface.DEFAULT_BOLD
            binding?.trackerOff?.typeface = Typeface.DEFAULT
        } else {
            binding?.trackerOn?.typeface = Typeface.DEFAULT
            binding?.trackerOff?.typeface = Typeface.DEFAULT_BOLD
        }
    }

    private fun startService() {
        activity?.startService(Intent(requireContext(), BeaconSenderService::class.java))
    }

    private fun stopService() {
        activity?.stopService(Intent(requireContext(), BeaconSenderService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

}
