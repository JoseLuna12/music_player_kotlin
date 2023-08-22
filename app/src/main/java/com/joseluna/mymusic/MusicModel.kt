package com.joseluna.mymusic

import androidx.annotation.Keep
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

@Keep
data class MusicModel(val id: String, val name: String, val location: String, val image: String, val author: String): Serializable{
    companion object {
        fun fromOneString(str: String): MusicModel {
            val value = JSONObject(str)
            val id = value.getString("id")
            val name = value.getString("name")
            val location = value.getString("location")
            val image = value.getString("image")
            val author = value.getString("author")

            return MusicModel(id, name, location, image, author)
        }

        fun fromMultipleString(str: String): ArrayList<MusicModel>{
            val value = JSONArray(str)
            val newSongs = ArrayList<MusicModel>()
            for (song in 0 until value.length()){
                val newSong = fromOneString(value[song].toString())
                newSongs.add(newSong)
            }
            return newSongs
        }
    }
}
