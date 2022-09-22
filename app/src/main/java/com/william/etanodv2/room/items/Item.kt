package com.william.etanodv2.room.items

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val jumlah: Double,
    val durasi: String
)