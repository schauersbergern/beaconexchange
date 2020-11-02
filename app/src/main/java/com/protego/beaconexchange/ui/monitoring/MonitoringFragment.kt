package com.protego.beaconexchange.ui.monitoring

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.protego.beaconexchange.AlarmManager
import com.protego.beaconexchange.AlarmManager.Companion.SEVERITY_MEDIUM
import com.protego.beaconexchange.AlarmManager.Companion.SEVERITY_SEVERE
import com.protego.beaconexchange.MainActivity
import com.protego.beaconexchange.R
import com.protego.beaconexchange.databinding.FragmentMonitoringBinding
import com.protego.beaconexchange.domain.BluetoothMessage
import com.protego.beaconexchange.helper.Constants
import com.protego.beaconexchange.helper.Constants.Companion.BACKGROUND_PERMISSION
import com.protego.beaconexchange.helper.Constants.Companion.BEACON_MESSAGE
import com.protego.beaconexchange.helper.Constants.Companion.BEACON_UPDATE
import com.protego.beaconexchange.helper.Constants.Companion.FIRST_NOTIFICATION_KEY
import com.protego.beaconexchange.helper.alertDialog
import com.protego.beaconexchange.helper.name
import com.protego.beaconexchange.helper.showNotification
import com.protego.beaconexchange.ui.excluded.ExcludedViewModel
import com.protego.permissions.presentation.activateBatteryOptimizations
import com.protego.permissions.presentation.hasBackgroundLocationPermission
import com.protego.permissions.presentation.hasPermissions
import com.protego.permissions.presentation.ignoresBatteryOptimizations
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.concurrent.schedule

class MonitoringFragment : Fragment(), KoinComponent {

    private lateinit var viewModel: MonitoringViewModel
    private lateinit var excludedViewModel : ExcludedViewModel
    private var binding: FragmentMonitoringBinding? = null
    private val _binding get() = binding

    private var timer: TimerTask? = null

    private val alarmManager : AlarmManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonitoringBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MonitoringViewModel::class.java)
        excludedViewModel = ViewModelProvider(this).get(ExcludedViewModel::class.java)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            if (state.deviceId != "" && state.serviceShouldRun && !state.alarmState) {
                (requireActivity() as MainActivity).startServices(state.deviceId)
                setSurveillanceState()
            } else if (state.serviceShouldRun && state.alarmState) {
                setAlarmState()
            } else if (!state.serviceShouldRun) {
                setOffState()
                (requireActivity() as MainActivity).stopServices()
            }
        })

        excludedViewModel.excludedLive.observe(viewLifecycleOwner, Observer { excluded ->
            if (excluded != null) {
                excludedViewModel.addExcluded(excluded)
            }
        })

        binding?.mainSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkPreConditions()
                viewModel.startServiceIfAllEnabledOrStop()
                checkAndShowFirstNotification()
            } else {
                viewModel.setOff()
            }
        }

        binding?.mainAccessExcluded?.setOnClickListener {
            findNavController().navigate(MonitoringFragmentDirections.showExcluded())
        }

        binding?.showSettings?.setOnClickListener {
            findNavController().navigate(MonitoringFragmentDirections.showSettings())
        }
    }

    private fun checkAndShowFirstNotification() {

        val firstNotification = requireActivity().getPreferences(Context.MODE_PRIVATE)
            .getBoolean(FIRST_NOTIFICATION_KEY, true)

        if (firstNotification) {
            requireContext().showNotification(
                null,
                getString(R.string.first_use_notification),
                true
            )
            requireActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean(
                FIRST_NOTIFICATION_KEY,
                false
            ).apply()
        }
    }

    override fun onStart() {
        super.onStart()

        if (shouldShowOnboarding()) {
            findNavController().navigate(MonitoringFragmentDirections.showIntro())
        }
    }

    private fun shouldShowOnboarding() =
        requireActivity().getPreferences(Context.MODE_PRIVATE)
            .getBoolean(com.protego.permissions.presentation.Constants.ONBOARDING_KEY, true)

    private fun setExcludedText(msg: BluetoothMessage) {
        val txt = getString(R.string.add_device_to_excluded_q).replace("%s", msg.deviceId)
        binding?.addToExcludedText?.text = txt
        binding?.addToExcludedText?.setOnClickListener {
            addToExcluded(msg.deviceId)
        }
    }

    private fun setAlarmState() {
        binding?.mainSwitch?.isChecked = true
        binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_alert))
        binding?.mainDistanceSave?.text = getString(R.string.distance_close)
        initSwitchToSurveillance()
    }

    private fun setSurveillanceState() {
        binding?.mainSwitch?.isChecked = true
        binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_on))
        binding?.mainDistanceSave?.text = getString(R.string.distance_save)
        binding?.mainTrackerState?.text = getText(R.string.tracker_on)
        binding?.addToExcludedText?.text = ""
        binding?.trackerOn?.typeface = Typeface.DEFAULT_BOLD
        binding?.trackerOff?.typeface = Typeface.DEFAULT
    }

    private fun setOffState() {
        binding?.mainSwitch?.isChecked = false
        binding?.alarmImage?.setImageDrawable(requireActivity().getDrawable(R.drawable.img_off))
        binding?.mainDistanceSave?.text = getString(R.string.distance_off)
        binding?.mainTrackerState?.text = getText(R.string.tracker_off)
        binding?.addToExcludedText?.text = ""
        binding?.trackerOn?.typeface = Typeface.DEFAULT
        binding?.trackerOff?.typeface = Typeface.DEFAULT_BOLD
    }

    private fun initSwitchToSurveillance() {
        timer?.cancel()
        timer = Timer(name(), false).schedule(1500) {
            viewModel.setSurveilance()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver, IntentFilter(BEACON_UPDATE)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

    override fun onResume() {
        super.onResume()
        viewModel.startServiceIfAllEnabledOrStop()
    }

    private fun checkPreConditions() {
        verifyBluetooth()
        verifyPermissions()
        verifyLocationEnabled()
        verifyBatteryOptimizations()
        verifyBackgroundPermission()
    }

    private fun verifyBackgroundPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            if (!hasBackgroundLocationPermission(requireContext()) ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    BACKGROUND_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == BACKGROUND_PERMISSION) {
            if (permissions.isNotEmpty() && (grantResults[0] == -1)) {
                requireContext().alertDialog(
                    getString(R.string.bg_location_title),
                    getString(R.string.bg_location_message),
                    {
                        verifyBackgroundPermission()
                    },
                    {
                    }
                ).show()
            }
        }
    }

    private fun verifyLocationEnabled() {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requireContext().alertDialog(
                getString(R.string.not_enabled_location),
                getString(R.string.prompt_enable_location),
                {
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        Constants.LOCATION_REQUEST_CODE
                    )
                },
                {
                    viewModel.disableLocation()
                }
            ).show()
        } else {
            viewModel.enableLocation()
        }
    }

    private fun verifyBluetooth() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            requireContext().alertDialog(
                getString(R.string.not_available_ble),
                getString(R.string.not_supported_ble),
                { viewModel.disableBlueTooth() },
                { viewModel.disableBlueTooth() }
            ).show()
        } else if (!mBluetoothAdapter.isEnabled) {
            requireContext().alertDialog(
                getString(R.string.not_enabled_ble),
                getString(R.string.prompt_enable_ble),
                {
                    BluetoothAdapter.getDefaultAdapter().enable()
                    viewModel.enableBlueTooth()
                },
                { viewModel.disableBlueTooth() }
            ).show()
        } else {
            viewModel.enableBlueTooth()
        }
    }

    private fun verifyPermissions() {
        if (!hasPermissions(requireContext())) {
            findNavController().navigate(MonitoringFragmentDirections.showPermissions())
        }
    }

    private fun verifyBatteryOptimizations() {
        if (!ignoresBatteryOptimizations()) {
            activateBatteryOptimizations()
        } else {
            viewModel.enableBackGround()
        }
    }

    fun addToExcluded(deviceId: String) {
        excludedViewModel.addToExcluded(deviceId)
        Toast.makeText(
            requireContext(),
            getString(R.string.add_device_to_excluded).replace("%s", deviceId),
            Toast.LENGTH_LONG
        ).show()
    }

    fun deviceExcluded(deviceId: String) = excludedViewModel.isInExcluded(deviceId)

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val message = intent.getParcelableExtra<BluetoothMessage>(BEACON_MESSAGE)

            message?.let {
                if (deviceExcluded(it.deviceId)) {
                    return
                }

                setExcludedText(it)
                alarmManager.checkRssiDistance(it.rssi)

                when ((activity as MainActivity).alarmManager.getRssiSeverity(it.rssi)) {
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
    }
}
