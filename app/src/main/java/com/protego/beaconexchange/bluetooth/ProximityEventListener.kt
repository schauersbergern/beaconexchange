package com.protego.beaconexchange.bluetooth

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ProximityEventListener(
    /*private val repo: ContactRepository,*/
    /*private val notification: ProximityNotification,*/
    val clock: () -> LocalDateTime = LocalDateTime::now
): ScanEventListener {

    var secondsInProximity: Long = 0
        private set

    var secondsSinceLastInProximity: Long = 0

    private var startCount: Long = 0
    private var isNewContact = true
    private val todaysEvents = mutableListOf<ScanEvent>()
    private var proximityAlertSent = false
    private var isStarted = false

    fun startUp() {
        //repo.cleanupOldContacts()
        //todaysEvents.addAll(repo.todaysContactEvents().map { ScanEvent(it.deviceId, it.transmission_power, it.rssi, it.timestamp) })
        if (todaysEvents.isNotEmpty()) {
            updateTimeInProximity()
            startCount = secondsInProximity
        }
        isStarted = true
    }

    override fun handle(event: ScanEvent) {
        if (!isStarted) startUp()
        todaysEvents.add(event)
        //repo.addEvent(ContactEvent(0, event.device, event.rssi, event.txPower, event.timestamp))
        updateTimeInProximity()
        updateContactCounter()
        sendNotifications()
    }

    private fun updateTimeInProximity() {
        var total: Long = 0
        var earliestEvent: ScanEvent? = null
        var previousEvent: ScanEvent? = null
        todaysEvents.filter { it.isClose() }
            .forEach { event ->
                if (previousEvent?.timestamp?.until(event.timestamp, ChronoUnit.SECONDS) ?: 100 < 60) {
                    previousEvent = event
                } else {
                    total += earliestEvent?.timestamp?.until(previousEvent!!.timestamp, ChronoUnit.SECONDS) ?: 0
                    earliestEvent = event
                    previousEvent = event
                }
            }
        total += if (earliestEvent != null) earliestEvent!!.timestamp.until(previousEvent!!.timestamp, ChronoUnit.SECONDS) else 0
        secondsInProximity = total
        secondsSinceLastInProximity = previousEvent?.timestamp?.until(clock(), ChronoUnit.SECONDS) ?: 0
    }

    private fun updateContactCounter() {
        if (isNewContact && secondsInProximity - startCount  >= 300) {
            //repo.incrementContactCount()
            isNewContact = false
        } else if (secondsSinceLastInProximity >= 300) {
            isNewContact = true
            startCount = secondsInProximity
        }
    }

    private fun sendNotifications() {
        if (proximityAlertSent) {
            if (secondsSinceLastInProximity >= 120) {
                //notification.showProximityAllClear()
                proximityAlertSent = false
            }
        } else {
            val closeEventsInTheLast60Seconds = todaysEvents.filter { it.isClose() }
                .filter { it.timestamp.until(clock(), ChronoUnit.SECONDS) < 60 }
            if (closeEventsInTheLast60Seconds.size >= 2) {
                //notification.showProximityAlert()
                proximityAlertSent = true
            }
        }
    }
}

fun ScanEvent.isClose() = this.rssi >= -65