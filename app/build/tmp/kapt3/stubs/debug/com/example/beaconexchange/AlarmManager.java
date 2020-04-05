package com.example.beaconexchange;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000  2\u00020\u0001:\u0001 B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001d\u001a\u00020\u0018H\u0002J\b\u0010\u001e\u001a\u00020\u0018H\u0002J\u0006\u0010\u001f\u001a\u00020\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016\u00a8\u0006!"}, d2 = {"Lcom/example/beaconexchange/AlarmManager;", "", "ctx", "Landroid/content/Context;", "(Landroid/content/Context;)V", "mPlayer", "Landroid/media/MediaPlayer;", "getMPlayer", "()Landroid/media/MediaPlayer;", "setMPlayer", "(Landroid/media/MediaPlayer;)V", "notification", "Landroid/net/Uri;", "getNotification", "()Landroid/net/Uri;", "setNotification", "(Landroid/net/Uri;)V", "r", "Landroid/media/Ringtone;", "getR", "()Landroid/media/Ringtone;", "setR", "(Landroid/media/Ringtone;)V", "alarmbutton", "", "checkDistance", "distance", "", "getSeverity", "startAlarm", "stopAlarm", "vibrate", "Companion", "app_debug"})
public final class AlarmManager {
    @org.jetbrains.annotations.NotNull()
    private android.net.Uri notification;
    @org.jetbrains.annotations.NotNull()
    private android.media.Ringtone r;
    @org.jetbrains.annotations.NotNull()
    private android.media.MediaPlayer mPlayer;
    private final android.content.Context ctx = null;
    public static final int SEVERITY_NO = 0;
    public static final int SEVERITY_MEDIUM = 2;
    public static final int SEVERITY_SEVERE = 3;
    public static final com.example.beaconexchange.AlarmManager.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final android.net.Uri getNotification() {
        return null;
    }
    
    public final void setNotification(@org.jetbrains.annotations.NotNull()
    android.net.Uri p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.media.Ringtone getR() {
        return null;
    }
    
    public final void setR(@org.jetbrains.annotations.NotNull()
    android.media.Ringtone p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.media.MediaPlayer getMPlayer() {
        return null;
    }
    
    public final void setMPlayer(@org.jetbrains.annotations.NotNull()
    android.media.MediaPlayer p0) {
    }
    
    public final void checkDistance(int distance) {
    }
    
    public final void alarmbutton() {
    }
    
    private final int getSeverity(int distance) {
        return 0;
    }
    
    public final void vibrate() {
    }
    
    private final void startAlarm() {
    }
    
    private final void stopAlarm() {
    }
    
    public AlarmManager(@org.jetbrains.annotations.NotNull()
    android.content.Context ctx) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/example/beaconexchange/AlarmManager$Companion;", "", "()V", "SEVERITY_MEDIUM", "", "SEVERITY_NO", "SEVERITY_SEVERE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}