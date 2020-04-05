package com.example.beaconexchange.beacon;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\b\u0010\u0006\u001a\u00020\u0003H\'J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\'J\u0014\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\nH\'J!\u0010\u000b\u001a\u00020\u00032\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u000eJ!\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\rH\'\u00a2\u0006\u0002\u0010\u0012J!\u0010\u0013\u001a\u00020\u00032\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u000eJ!\u0010\u0014\u001a\u00020\u00032\u0012\u0010\f\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\r\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\u0015"}, d2 = {"Lcom/example/beaconexchange/beacon/BeaconContactDao;", "", "delete", "", "beacon", "Lcom/example/beaconexchange/beacon/BeaconContact;", "deleteAll", "getAll", "", "getAllLiveOrdered", "Landroidx/lifecycle/LiveData;", "insert", "beacons", "", "([Lcom/example/beaconexchange/beacon/BeaconContact;)V", "loadByIds", "beaconIds", "", "([Ljava/lang/String;)Ljava/util/List;", "update", "upsert", "app_debug"})
public abstract interface BeaconContactDao {
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from beaconcontact")
    public abstract java.util.List<com.example.beaconexchange.beacon.BeaconContact> getAll();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from beaconcontact ORDER BY lastSeen DESC")
    public abstract androidx.lifecycle.LiveData<java.util.List<com.example.beaconexchange.beacon.BeaconContact>> getAllLiveOrdered();
    
    @org.jetbrains.annotations.NotNull()
    @androidx.room.Query(value = "SELECT * from beaconcontact WHERE beaconId IN (:beaconIds)")
    public abstract java.util.List<com.example.beaconexchange.beacon.BeaconContact> loadByIds(@org.jetbrains.annotations.NotNull()
    java.lang.String[] beaconIds);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.ABORT)
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact... beacons);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void upsert(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact... beacons);
    
    @androidx.room.Update()
    public abstract void update(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact... beacons);
    
    @androidx.room.Delete()
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    com.example.beaconexchange.beacon.BeaconContact beacon);
    
    @androidx.room.Query(value = "DELETE from beaconcontact")
    public abstract void deleteAll();
}