package com.example.beaconexchange;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001a\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016J\u0012\u0010\u000e\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\rH\u0016J\u0012\u0010\u000f\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\rH\u0016J\b\u0010\u0010\u001a\u00020\tH\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/example/beaconexchange/BeaconExchangeApplication;", "Landroid/app/Application;", "Lorg/altbeacon/beacon/startup/BootstrapNotifier;", "()V", "backgroundPowerSaver", "Lorg/altbeacon/beacon/powersave/BackgroundPowerSaver;", "regionBootstrap", "Lorg/altbeacon/beacon/startup/RegionBootstrap;", "didDetermineStateForRegion", "", "p0", "", "p1", "Lorg/altbeacon/beacon/Region;", "didEnterRegion", "didExitRegion", "onCreate", "app_debug"})
public final class BeaconExchangeApplication extends android.app.Application implements org.altbeacon.beacon.startup.BootstrapNotifier {
    private org.altbeacon.beacon.startup.RegionBootstrap regionBootstrap;
    private org.altbeacon.beacon.powersave.BackgroundPowerSaver backgroundPowerSaver;
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public void didDetermineStateForRegion(int p0, @org.jetbrains.annotations.Nullable()
    org.altbeacon.beacon.Region p1) {
    }
    
    @java.lang.Override()
    public void didEnterRegion(@org.jetbrains.annotations.Nullable()
    org.altbeacon.beacon.Region p0) {
    }
    
    @java.lang.Override()
    public void didExitRegion(@org.jetbrains.annotations.Nullable()
    org.altbeacon.beacon.Region p0) {
    }
    
    public BeaconExchangeApplication() {
        super();
    }
}