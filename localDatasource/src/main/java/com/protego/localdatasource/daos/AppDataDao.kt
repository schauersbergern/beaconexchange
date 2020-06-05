package com.protego.localdatasource.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.protego.localdatasource.entities.AppData

@Dao
interface AppDataDao {
    @Delete
    fun delete(appData: AppData)

    @Insert
    fun insert(appData: AppData)

    @Query("SELECT * FROM appdata LIMIT 1")
    fun getAppData(): AppData
}