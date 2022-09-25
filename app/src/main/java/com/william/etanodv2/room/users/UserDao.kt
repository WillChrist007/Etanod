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

    @Query("SELECT * FROM user WHERE username like :Username AND password like :Password")
    suspend fun checkUser(Username:String, Password: String): List<User>

    @Query("SELECT * FROM User WHERE id=:idInput")
    suspend fun getUser(idInput: Int) : List<User>
}