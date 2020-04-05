package com.example.beaconexchange

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.beaconexchange.beacon.BeaconContact
import com.example.beaconexchange.beacon.BeaconContactDao

@Database(entities = arrayOf(BeaconContact::class), version = 1, exportSchema = true)
abstract class BeaconContactDatabase : RoomDatabase() {
    abstract fun beaconContactDao(): BeaconContactDao

    companion object {
        @Volatile
        private var INSTANCE: BeaconContactDatabase? = null

        fun getDatabase(
            context: Context
        ): BeaconContactDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BeaconContactDatabase::class.java,
                        "beaconContact_database"
                    )
                    .enableMultiInstanceInvalidation()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
