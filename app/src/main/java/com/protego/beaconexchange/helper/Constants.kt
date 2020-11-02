package com.protego.beaconexchange.helper

import android.content.ComponentName
import android.content.Intent

class Constants {
    companion object {
        const val BEACON_UPDATE = "BaconUpdate"
        const val BEACON_MESSAGE = "BeaconMessage"


        const val ANDROID_MANUFACTURE_SUBSTRING = "9b3"
        const val ANDROID_MANUFACTURE_SUBSTRING_MASK = "000"
        const val ANDROID_MANUFACTURE_ID = 1023
        const val APPLE_DEVICE_NAME = "SDAlarm"


        const val PROTEGO_ID = "65463"
        const val DEVICE_ID = "DeviceId"

        const val REGION_ID = "ProtegoRegion"
        const val SERVICE_CHANNEL = "BackgroundService"

        const val FIRST_NOTIFICATION_KEY = "f_n_key"

        const val PERMISSION_REQUEST_FINE_LOCATION = 1
        const val PERMISSION_REQUEST_BACKGROUND_LOCATION = 2
        const val ALARM_REQUEST = 25
        const val ONBOARDING_REQUEST = 24

        const val BACKGROUND_PERMISSION = 64

        const val FOREGROUND_ID = 34
        const val MANUFACTURER = 0x0118

        const val LOG_DIR = "/log"
        const val LOG_FILE = "protego%g.csv"


        const val POWERMANAGER_REQUEST = 321
        const val LOCATION_REQUEST_CODE = 322

        const val WAKELOCK_TIMEOUT = 500L

        val POWERMANAGER_INTENTS =
            arrayOf(
                Intent().setComponent(
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.startupapp.StartupAppListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.oppo.safe",
                        "com.oppo.safe.permission.startup.StartupAppListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.iqoo.secure",
                        "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.samsung.android.lool",
                        "com.samsung.android.sm.ui.battery.BatteryActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.htc.pitroad",
                        "com.htc.pitroad.landingpage.activity.LandingPageActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus.mobilemanager.MainActivity"
                    )
                )
            )
    }
}