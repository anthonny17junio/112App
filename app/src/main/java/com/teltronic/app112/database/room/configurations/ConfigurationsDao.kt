package com.teltronic.app112.database.room.configurations

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ConfigurationsDao {
    @Insert
    fun insert(configurations: ConfigurationsEntity)

    @Update
    fun update(configurations: ConfigurationsEntity)

    @Query("SELECT * FROM tb_configurations LIMIT 1")
    fun get(): ConfigurationsEntity?

    @Query("SELECT * FROM tb_configurations LIMIT 1")
    fun getLiveData(): LiveData<ConfigurationsEntity?>

    @Query("DELETE FROM tb_configurations")
    fun deleteAll()
}