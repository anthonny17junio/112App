package com.teltronic.app112.database.room.configurations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_configurations")
data class ConfigurationsEntity(
    @PrimaryKey
    val id: Int = 0,
    val lang_code: String = "",
    val lat_notices: Double = 0.0,
    val long_notices: Double = 0.0,
    val distance_code_notices: Int = 0
)