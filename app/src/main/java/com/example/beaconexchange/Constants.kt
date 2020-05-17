package com.example.beaconexchange

class Constants {
    companion object {
        const val BEACON_UPDATE = "BaconUpdate"
        const val BEACON_MESSAGE = "BeaconMessage"

        const val PROTEGO_UUID = "38144dcd-4e27-4213-af7d-b80dd366cd96"
        const val DEVICE_ID = "DeviceId"

        const val REGION_ID = "ProtegoRegion"
        const val SERVICE_CHANNEL = "BackgroundService"

        const val ONBOARDING_KEY = "asdfd"

        const val PERMISSION_REQUEST_FINE_LOCATION = 1
        const val PERMISSION_REQUEST_BACKGROUND_LOCATION = 2
        const val ALARM_REQUEST = 25
        const val ONBOARDING_REQUEST = 24

        const val FOREGROUND_ID = 34

        const val ALTBEACON  =    "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"
        const val EDDYSTONE_TLM  = "x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"
        const val EDDYSTONE_UID =  "s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"
        const val EDDYSTONE_URL = "s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"
        const val IBEACON   =     "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
    }
}