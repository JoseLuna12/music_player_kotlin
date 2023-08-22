package com.joseluna.mymusic

public class Constants {
    companion object {
        val BASE_URL = "https://e43d-179-49-61-59.ngrok-free.app"
        val ALL_SONGS_URL = "$BASE_URL/song"
        fun ONE_SONG_URL(id: String) = "$ALL_SONGS_URL/$id"
        fun PLAY_SONG_BY_ID(id: String) = "$BASE_URL/play/$id"

        val ALL_USERS_URL = "$BASE_URL/user"
        fun ONE_USER_URL(username: String) = "$ALL_USERS_URL/$username"
        fun STATIC_FILE(filePath: String) = "$BASE_URL/static/static/$filePath"
    }
}