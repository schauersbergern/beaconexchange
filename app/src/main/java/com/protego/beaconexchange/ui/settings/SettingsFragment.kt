package com.protego.beaconexchange.ui.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kevalpatel.ringtonepicker.RingtonePickerListener
import com.protego.beaconexchange.helper.Constants.Companion.LOG_DIR
import com.protego.beaconexchange.helper.Constants.Companion.LOG_FILE
import com.protego.beaconexchange.R
import com.protego.beaconexchange.databinding.FragmentSettingsBinding
import com.protego.beaconexchange.helper.getRingtoneName
import com.protego.beaconexchange.helper.getStandardSettings
import com.protego.beaconexchange.helper.showRingtonePicker

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
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
            binding?.alarmtoneLabel?.text =
                requireContext().getRingtoneName(Uri.parse(liveSettings.ringtone))

            binding?.arrow?.setOnClickListener {
                showRingtonePicker(Uri.parse(liveSettings.ringtone),
                    RingtonePickerListener { _, ringtoneUri ->
                        viewModel.updateAlarmUri(ringtoneUri.toString())
                    })
            }
        })
    }

    private fun initViews() {

        binding?.rssiSlider?.addOnChangeListener { _, value, _ ->
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

}