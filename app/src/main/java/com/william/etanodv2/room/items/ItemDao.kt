package com.william.etanodv2.room.items

import androidx.room.*
import com.william.etanodv2.room.items.Item

@Dao
interface ItemDao {
    @Insert
    suspend fun addItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM item")
    suspend fun getItems() : List<Item>

    @Query("SELECT * FROM item WHERE id =:item_id")
    suspend fun getItem(item_id: Int) : List<Item>
}