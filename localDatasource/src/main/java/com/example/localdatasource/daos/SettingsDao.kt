package com.example.localdatasource.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localdatasource.entities.Settings

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings LIMIT 1")
    fun getLiveSettings(): LiveData<Settings>

    //@Query("SELECT * FROM settings LIMIT 1")
    //fun get(): Settings

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: Settings)

}