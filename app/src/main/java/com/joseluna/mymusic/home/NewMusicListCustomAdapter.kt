package com.joseluna.mymusic.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joseluna.mymusic.Constants
import com.joseluna.mymusic.MusicModel
import com.joseluna.mymusic.R
import com.squareup.picasso.Picasso

class NewMusicListCustomAdapter(private var songList: ArrayList<MusicModel>): RecyclerView.Adapter<NewMusicListCustomAdapter.ViewHolder>() {

    private lateinit var onClickListener: RecyclerViewInterface

    class ViewHolder(view: View, listener: RecyclerViewInterface,music: ArrayList<MusicModel>): RecyclerView.ViewHolder(view) {
        val image: ImageView
        val title: TextView
        init {
            image = view.findViewById(R.id.small_list_imageView)
            title = view.findViewById(R.id.small_list_textView)
            view.setOnClickListener{
                val song = music[bindingAdapterPosition]
                listener.onItemClick(song, music)
            }
        }
    }

    fun setValueOnClickListener(listener: RecyclerViewInterface){
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.small_card_item_recycler_view, parent, false)

        return ViewHolder(view, onClickListener, songList)
    }

    override fun getItemCount(): Int {
        return songList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = songList[position].author
        val imageUrl = Constants.STATIC_FILE(songList[position].image)
        val url = Uri.parse(imageUrl)
        Picasso.get().load(url).into(holder.image)
    }

}