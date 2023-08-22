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

class MusicListCustomAdapter(private val music: ArrayList<MusicModel>): RecyclerView.Adapter<MusicListCustomAdapter.ViewHolder>() {

    private lateinit var onClickListener: RecyclerViewInterface

    class ViewHolder(view: View, listener: RecyclerViewInterface, music: ArrayList<MusicModel>): RecyclerView.ViewHolder(view) {
        val image: ImageView
        val imageTitle: TextView
        init {
            image = view.findViewById(R.id.carouselImageContainer)
            imageTitle = view.findViewById(R.id.titleView)
            view.setOnClickListener {
                val song = music[bindingAdapterPosition]
                listener.onItemClick(song, music)
            }
        }

    }

    fun setOnClickListener(listener: RecyclerViewInterface){
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item_recycler_view, parent, false)

        return ViewHolder(view, onClickListener, music)
    }

    override fun getItemCount(): Int {
        return music.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageResource = Constants.STATIC_FILE(music[position].image)
        val uriValue = Uri.parse(imageResource)
        println("URL FROM IMAGE")
        println(imageResource)
        Picasso.get().load(uriValue).into(holder.image)
        holder.imageTitle.text = music[position].name
    }

}