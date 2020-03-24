package com.teltronic.app112.database.room.medicalInfo

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tb_medical_information")
data class MedicalInfoEntity(
    @PrimaryKey
    val id: Int = 0,
    var allergies: String = "",
    var diseases: String = "",
    var disabilities: String = "",
    var medications: String = ""
)