package com.example.beaconexchange.beacon;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u001c\u0010\u0005\u001a\u00020\u00038FX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\u0004\u00a8\u0006\t"}, d2 = {"Lcom/example/beaconexchange/beacon/BeaconServiceBinder;", "Landroid/os/Binder;", "consumerService", "Lcom/example/beaconexchange/beacon/BeaconConsumerService;", "(Lcom/example/beaconexchange/beacon/BeaconConsumerService;)V", "service", "getService", "()Lcom/example/beaconexchange/beacon/BeaconConsumerService;", "setService", "app_debug"})
public final class BeaconServiceBinder extends android.os.Binder {
    @org.jetbrains.annotations.NotNull()
    private com.example.beaconexchange.beacon.BeaconConsumerService service;
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.beaconexchange.beacon.BeaconConsumerService getService() {
        return null;
    }
    
    public final void setService(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconConsumerService p0) {
    }
    
    public BeaconServiceBinder(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconConsumerService consumerService) {
        super();
    }
}