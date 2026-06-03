package com.example.loginmvvpapp.data.repository

import com.example.loginmvvpapp.data.local.dao.UserDao
import com.example.loginmvvpapp.data.local.entity.User

class UserRepository(private val dao: UserDao) {

    suspend fun insert(user: User) {
        dao.insert(user)
    }

    suspend fun login(username: String, password: String): User? {
        return dao.login(username, password)
    }
}