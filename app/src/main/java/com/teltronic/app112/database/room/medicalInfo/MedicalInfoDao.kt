package com.teltronic.app112.database.room.medicalInfo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MedicalInfoDao {
    @Insert
    fun insert(medicalInfo: MedicalInfoEntity)

    @Update
    fun update(medicalInfo: MedicalInfoEntity)

    @Query("SELECT * FROM tb_medical_information LIMIT 1")
    fun get(): LiveData<MedicalInfoEntity?>
}