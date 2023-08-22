package com.joseluna.mymusic.home

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.joseluna.mymusic.BackendConnection
import com.joseluna.mymusic.Constants
import com.joseluna.mymusic.MusicModel
import com.joseluna.mymusic.R
import com.joseluna.mymusic.UserModel
import com.squareup.picasso.Picasso

class homeViewFragment : Fragment() {

    private lateinit var currentView: View
    private var user: UserModel? = null
    private lateinit var popularSongsRecyclerView: RecyclerView
    private lateinit var newSongsRecyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        user = arguments?.getSerializable("user", UserModel::class.java)

        currentView = inflater.inflate(R.layout.fragment_home_view, container, false)
        popularSongsRecyclerView = currentView.findViewById(R.id.popularMusicRV)
        newSongsRecyclerView = currentView.findViewById<RecyclerView>(R.id.newMusicRV)

        loadMusic()
        return  currentView
    }


    private val itemClickManager = object : RecyclerViewInterface {
        override fun onItemClick(song: MusicModel, songs: ArrayList<MusicModel>) {
            val action = homeViewFragmentDirections
                .actionHomeViewFragmentToPlayerViewFragment(song)
            findNavController().navigate(action)
        }
    }

    private fun showSnackBar(text: String){
        val snackBar = Snackbar.make(currentView, text, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(
            ContextCompat.getColor(requireContext(), com.google.android.material.R.color.m3_ref_palette_error95)
        )
        snackBar.setTextColor(
            ContextCompat.getColor(requireContext(), com.google.android.material.R.color.m3_ref_palette_error20)
        )
        snackBar.show()
    }

    private fun loadMusic(){
        val client = BackendConnection(requireContext()) {error ->
            showSnackBar(error.msg)
        }

        val musicUrl = Constants.ALL_SONGS_URL
        client.get(musicUrl) {result ->
            val songs = MusicModel.fromMultipleString(result)
            loadPopularSongs(songs)
            loadFeaturedSong(songs[3], songs)
            loadNewSongList(ArrayList(songs.drop(0)))
        }
    }

    private fun loadPopularSongs(popular: ArrayList<MusicModel>){
        val adapter = MusicListCustomAdapter(popular)
        adapter.setOnClickListener(itemClickManager)
        popularSongsRecyclerView.adapter = adapter
    }

    private fun loadFeaturedSong(featured: MusicModel, songs: ArrayList<MusicModel>){
        val featuredImageView = currentView.findViewById<ImageView>(R.id.featuredImageView)
        val textViewFeatured =  currentView.findViewById<TextView>(R.id.featuredSongTitleTextView)
        val imageUrl = Constants.STATIC_FILE(featured.image)
        val uriValue = Uri.parse(imageUrl)
        Picasso.get().load(uriValue).into(featuredImageView)
        textViewFeatured.text = featured.author
        featuredImageView.setOnClickListener{
            itemClickManager.onItemClick(featured, songs)
        }
    }

    private fun loadNewSongList(songList: ArrayList<MusicModel>){
        val adapter = NewMusicListCustomAdapter(songList)
        adapter.setValueOnClickListener(itemClickManager)
        newSongsRecyclerView.adapter = adapter
    }
}