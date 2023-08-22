package com.joseluna.mymusic.home

import com.joseluna.mymusic.MusicModel

interface RecyclerViewInterface {
    fun onItemClick(song: MusicModel, songs: ArrayList<MusicModel>);
}