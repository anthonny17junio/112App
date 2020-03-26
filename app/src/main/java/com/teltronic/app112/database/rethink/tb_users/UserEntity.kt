package com.teltronic.app112.database.rethink.tb_users

data class UserEntity(
    val id: String = "",
    val idGoogle: String = "",
    val timeCreated:String="",
    val lastAccess:String=""
)