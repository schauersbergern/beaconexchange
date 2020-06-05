package com.protego.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.protego.localdatasource.entities.AppData
import com.protego.localdatasource.entities.Device
import com.protego.localdatasource.daos.AppDataDao
import com.protego.localdatasource.daos.DeviceDao
import com.protego.localdatasource.daos.SettingsDao
import com.protego.localdatasource.entities.Settings

@Database(entities = [
    AppData::class,
    Device::class,
    Settings::class
], version = 7)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun appDataDao() : AppDataDao
    abstract fun deviceDao(): DeviceDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(
            context: Context
        ): LocalDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "protego_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

