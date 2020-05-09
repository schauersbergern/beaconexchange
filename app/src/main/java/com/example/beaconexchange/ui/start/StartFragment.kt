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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.beaconexchange.AlarmManager.Companion.SEVERITY_MEDIUM
import com.example.beaconexchange.AlarmManager.Companion.SEVERITY_SEVERE
import com.example.beaconexchange.AlarmManager.Companion.getSeverity
import com.example.beaconexchange.BluetoothMessage
import com.example.beaconexchange.Constants
import com.example.beaconexchange.Constants.Companion.BEACON_MESSAGE
import com.example.beaconexchange.Constants.Companion.BEACON_UPDATE
import com.example.beaconexchange.R
import com.example.beaconexchange.beacon.BeaconConsumerService
import com.example.beaconexchange.beacon.BeaconSenderService
import com.example.beaconexchange.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private lateinit var viewModel : StartViewModel

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val message = intent.getParcelableExtra<BluetoothMessage>(BEACON_MESSAGE)
            setWhitelistText(message.deviceId)

            val data = "The beacon with Address ${message.blueToothAddress} has the name " +
                    "${message.blueToothName} is about ${message.distCentimeters} centimeters away " +
                    "and has rssi of ${message.rssi}"


            when (getSeverity(message.distCentimeters)) {
                SEVERITY_MEDIUM -> {
                    viewModel.setAlarm()
                }
                SEVERITY_SEVERE -> {
                    viewModel.setAlarm()
                }
                else -> {
                    viewModel.setSurveilance()
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
        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            if (state.deviceId != "" && state.serviceShouldRun && !state.alarmState) {
                startServices(state.deviceId)
                setSurveillanceState()
            } else if (state.serviceShouldRun && state.alarmState) {
                setAlarmState()
            } else if (!state.serviceShouldRun) {
                setOffState()
                stopServices()
            }
        })

        binding?.mainSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setSurveilance()
            } else {
                viewModel.setOff()
            }
        }
    }

    private fun setWhitelistText(deviceId: String) {
        binding?.addToWhitelistText?.text = "Add $deviceId to whitelist?"
        binding?.addToWhitelist?.setOnClickListener {
            //viewmodel.addToWhitelist()
        }
    }

    private fun setAlarmState() {
        binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_alert))
        binding?.mainDistanceSave?.text = getString(R.string.distance_close)
    }

    private fun setSurveillanceState() {
        binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_on))
        binding?.mainDistanceSave?.text = getString(R.string.distance_save)
        binding?.mainTrackerState?.text = getText(R.string.tracker_on)
        binding?.trackerOn?.typeface = Typeface.DEFAULT_BOLD
        binding?.trackerOff?.typeface = Typeface.DEFAULT
    }

    private fun setOffState() {
        binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_off))
        binding?.mainDistanceSave?.text = getString(R.string.distance_off)
        binding?.mainTrackerState?.text = getText(R.string.tracker_off)
        binding?.trackerOn?.typeface = Typeface.DEFAULT
        binding?.trackerOff?.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver, IntentFilter(BEACON_UPDATE)
        )
    }

    private fun getSenderServiceIntent(deviceId : String) : Intent {
        return Intent(requireContext(), BeaconSenderService::class.java).apply {
            putExtra(Constants.DEVICE_ID, deviceId)
        }
    }

    private fun startServices(deviceId : String) {
        activity?.startService(getSenderServiceIntent(deviceId))
        activity?.startService(Intent(requireContext(), BeaconConsumerService::class.java))
    }

    private fun stopServices() {
        activity?.stopService(Intent(requireContext(), BeaconSenderService::class.java))
        activity?.stopService(Intent(requireContext(), BeaconConsumerService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

}
