package com.joseluna.mymusic

import androidx.annotation.Keep
import org.json.JSONObject
import java.io.Serializable

@Keep
data class UserModel(val userName: String, val userImage: String): Serializable {
    companion object {
        fun getUserFromJson(str: String): UserModel{
            val value = JSONObject(str)
            val userName = value.getString("user_name")
            val userImage = value.getString("user_image")
            return UserModel(userName, userImage)
        }
    }
}
