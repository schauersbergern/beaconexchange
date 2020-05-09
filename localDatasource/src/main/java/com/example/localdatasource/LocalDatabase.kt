package com.example.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.localdatasource.entities.AppData
import com.example.localdatasource.entities.Device
import com.example.localdatasource.daos.AppDataDao
import com.example.localdatasource.daos.DeviceDao

@Database(entities = [
    AppData::class,
    Device::class
], version = 1)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun appDataDao() : AppDataDao
    abstract fun deviceDao(): DeviceDao

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
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

