package com.joseluna.mymusic.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.joseluna.mymusic.Constants
import com.joseluna.mymusic.MusicModel
import com.joseluna.mymusic.R
import com.joseluna.mymusic.UserModel
import com.joseluna.mymusic.databinding.FragmentPlayerViewBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewFragment : Fragment() {
    private lateinit var currentView: View
    private lateinit var toolBar: MaterialToolbar
    private  var currentSong: MusicModel? = null
    private lateinit var currentTitle: TextView
    private lateinit var albumCover: ShapeableImageView
    private lateinit var player: MediaPlayer
    private lateinit var playActionButton: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_player_view, container, false)
        toolBar = currentView.findViewById(R.id.playerTopAppBar)
        currentTitle = currentView.findViewById(R.id.playerTitle)
        albumCover = currentView.findViewById(R.id.playerAlbumCover)
        playActionButton = currentView.findViewById(R.id.playActionButton)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)

        currentSong = arguments?.getSerializable("selectedSong", MusicModel::class.java)
        currentTitle.text = currentSong?.name

        val assetImage = Constants.STATIC_FILE(currentSong?.image!!)
        Picasso.get().load(assetImage).into(albumCover)

        lifecycleScope.launch {
            player = MediaPlayer()
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            val contentSourceSong = Constants.STATIC_FILE(currentSong?.location!!)
            player.setDataSource(contentSourceSong)


            withContext(Dispatchers.IO){
                player.prepare()

                player.start()
            }

        }


        playActionButton.setOnClickListener {
            if(player.isPlaying){
                player.pause()
            }else{
                player.start()
            }
        }

        return currentView
    }
}