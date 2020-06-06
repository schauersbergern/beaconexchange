package com.protego.beaconexchange.ui.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.protego.beaconexchange.Constants.Companion.ALARM_REQUEST
import com.protego.beaconexchange.Constants.Companion.LOG_DIR
import com.protego.beaconexchange.Constants.Companion.LOG_FILE
import com.protego.beaconexchange.R
import com.protego.beaconexchange.databinding.FragmentSettingsBinding
import com.protego.beaconexchange.getStandardSettings

class SettingsFragment : Fragment() {

    private lateinit var viewModel : SettingsViewModel
    private var binding: FragmentSettingsBinding? = null
    private val _binding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.settings.observe(this.viewLifecycleOwner, Observer { settings ->

            var liveSettings = settings
            if (settings == null) {
                val standardSettings = getStandardSettings(requireContext())
                viewModel.saveAll(standardSettings)
                liveSettings = standardSettings
            }

            binding?.alarmSwitch?.isChecked = liveSettings.isAlarmActive
            binding?.vibrationSwitch?.isChecked = liveSettings.isVibrationActive

            val rssiVal = liveSettings.rssi.toFloat()
            binding?.rssiSlider?.value = rssiVal
            binding?.rssiMonitor?.text = rssiVal.toString()
            binding?.ringtone?.text = liveSettings.ringtone

        })
    }

    private fun initViews() {
        binding?.setAlarmtoneButton?.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
            this.startActivityForResult(intent, ALARM_REQUEST)
        }

        binding?.rssiSlider?.addOnChangeListener { _, value, _ ->
            binding?.rssiMonitor?.text = value.toString()
            viewModel.setRssi(value.toInt())
        }

        binding?.alarmSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleAlarm(isChecked)
        }

        binding?.vibrationSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleVibration(isChecked)
        }

        binding?.loggingSwitch?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleLogging(isChecked)

            if (isChecked) {
                binding?.logdir?.text = "${getString(R.string.log_dir)}   $LOG_DIR/$LOG_FILE"
            } else {
                binding?.logdir?.text = ""
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == ALARM_REQUEST && resultCode == RESULT_OK) {
            intent?.apply {
                val uri : Uri? = getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                if (uri != null) {
                    viewModel.updateAlarmUri(uri.toString())
                    binding?.ringtone?.text = uri.toString()
                }
            }
        }
    }

}