package com.example.beaconexchange.ui.query;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bJ\u0006\u0010\u0010\u001a\u00020\u000eJ\u001f\u0010\u0011\u001a\u00020\u000e2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0013\"\u00020\b\u00a2\u0006\u0002\u0010\u0014J\u001f\u0010\u0015\u001a\u00020\u000e2\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0013\"\u00020\b\u00a2\u0006\u0002\u0010\u0014R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/example/beaconexchange/ui/query/QueryViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "beacons", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/beaconexchange/beacon/BeaconContact;", "getBeacons", "()Landroidx/lifecycle/LiveData;", "repository", "Lcom/example/beaconexchange/BeaconDataRepository;", "delete", "", "beaconContact", "deleteAll", "insert", "beaconContacts", "", "([Lcom/example/beaconexchange/beacon/BeaconContact;)V", "upsert", "app_debug"})
public final class QueryViewModel extends androidx.lifecycle.AndroidViewModel {
    private final com.example.beaconexchange.BeaconDataRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.beaconexchange.beacon.BeaconContact>> beacons = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.beaconexchange.beacon.BeaconContact>> getBeacons() {
        return null;
    }
    
    public final void insert(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact... beaconContacts) {
    }
    
    public final void upsert(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact... beaconContacts) {
    }
    
    public final void deleteAll() {
    }
    
    public final void delete(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact beaconContact) {
    }
    
    public QueryViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
}