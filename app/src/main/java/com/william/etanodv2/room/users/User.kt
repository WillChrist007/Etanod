package com.william.etanodv2.room.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val username: String,
    val password: String,
    val email: String,
    val tanggalLahir: String,
    val telepon: String
)