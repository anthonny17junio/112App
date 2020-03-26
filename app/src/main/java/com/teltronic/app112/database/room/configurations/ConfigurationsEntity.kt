package com.teltronic.app112.database.room.configurations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_configurations")
data class ConfigurationsEntity(
    @PrimaryKey
    val id: Int = 0,
    var id_rethink: String = ""
)