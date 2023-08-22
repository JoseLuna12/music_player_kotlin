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
import com.google.android.material.slider.Slider
import com.joseluna.mymusic.Constants
import com.joseluna.mymusic.MusicModel
import com.joseluna.mymusic.R
import com.joseluna.mymusic.UserModel
import com.joseluna.mymusic.databinding.FragmentPlayerViewBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class PlayerViewFragment : Fragment() {
    private lateinit var currentView: View
    private lateinit var toolBar: MaterialToolbar
    private  var currentSong: MusicModel? = null
    private lateinit var currentTitle: TextView
    private lateinit var albumCover: ShapeableImageView
    private lateinit var player: MediaPlayer
    private lateinit var playActionButton: FloatingActionButton
    private  lateinit var previousActionButton: FloatingActionButton
    private  lateinit var nextActionButton: FloatingActionButton
    private lateinit var progressBar: Slider
    private lateinit var timeElapsedView: TextView
    private var loading = false
    private val dateFormatter = SimpleDateFormat("mm:ss")

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
        previousActionButton = currentView.findViewById(R.id.backActionButton)
        nextActionButton = currentView.findViewById(R.id.nextActionButton)
        progressBar = currentView.findViewById(R.id.songProgress)
        timeElapsedView = currentView.findViewById(R.id.timeElapsedView)

        currentSong = arguments?.getSerializable("selectedSong", MusicModel::class.java)
        currentTitle.text = currentSong?.name

        prepareArt()
        prepareSong()


        playActionButton.setOnClickListener{
            if(player.isPlaying){
                pauseMedia()
            }else{
                playMedia()
            }
        }

        previousActionButton.setOnClickListener{
            if(player.isPlaying){
                player.seekTo(0)
            }
        }

        progressBar.addOnSliderTouchListener(object: Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
                pauseMedia()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                if(!player.isPlaying){
                    player.seekTo(slider.value.toInt())
                    playMedia()
                }
            }

        })

        player.setOnBufferingUpdateListener { mp, percent ->
//            progressBar.value = percent.toFloat()
        }



        return currentView
    }

    private fun checkProgress() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {

                while (player.isPlaying) {
                    activity?.runOnUiThread{
                        updateTime(player.currentPosition.toLong())
                        progressBar.value = player.currentPosition.toFloat()
                    }
                }
            }
        }
    }

    private fun updateTime(time: Long){
//        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
//        var seconds = TimeUnit.MILLISECONDS.toSeconds(time)


        val date = dateFormatter.format(Date(time))

//        var secondsToshow = seconds
//        var timesMinutes: Int = if(minutes.toInt() == 0){
//            1
//        }else{
//            minutes.toInt()
//        }
//
//        if(seconds > 60){
//            secondsToshow /= (60 * timesMinutes)
//        }

        timeElapsedView.text = date
    }
    private fun prepareArt() {
        val assetImage = Constants.STATIC_FILE(currentSong?.image!!)
        Picasso.get().load(assetImage).into(albumCover)
    }

    private fun prepareSong() {
        loading = true
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
                progressBar.valueTo = player.duration.toFloat()
                loading = false
                playMedia()
            }

        }
    }

    private fun playMedia(){
        if(!player.isPlaying){
            player.start()
            checkProgress()
            playActionButton.setImageResource(R.drawable.baseline_pause_24)
        }
    }

    private fun pauseMedia(){
        if(player.isPlaying){
            player.pause()
            playActionButton.setImageResource(R.drawable.baseline_play_arrow_24)
        }
    }
}