package com.william.etanodv2.room.users

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User WHERE id=:idInput")
    suspend fun getUser2(idInput: Int) : List<User>

    @Query("SELECT * FROM User WHERE username=:inp_uname")
    suspend fun getUser(inp_uname: String) : List<User>
}