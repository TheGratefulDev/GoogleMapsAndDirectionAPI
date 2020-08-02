package com.example.ka.googlemapapi

import android.app.Application
import com.example.ka.googlemapapi.model.User

class UserClient : Application() {

    private var user: User? = null

    fun getUser(): User? {
        return user
    }

    fun setUser(user: User?) {
        this.user = user
    }

}