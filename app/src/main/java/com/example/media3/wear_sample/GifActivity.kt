package com.example.media3.wear_sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.view.isGone
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.media3.wear_sample.databinding.ActivityGifBinding

class GifActivity : ComponentActivity() {
    private lateinit var binding: ActivityGifBinding

    private lateinit var exoPlayer: ExoPlayer

    private val playerListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            Log.d(BuildConfig.BUILD_TYPE, "#onRenderedFirstFrame")
            binding.progress.isGone = true
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e(BuildConfig.BUILD_TYPE, "ERROR:", error)
        }
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exoPlayer = ExoPlayer.Builder(this)
            .build().apply {
                val mediaItem = MediaItem.Builder()
                    .setUri("https://video.twimg.com/tweet_video/DBMDLy_U0AAqUWP.mp4")
                    .setMimeType("video/mp4")
                    .build()
                setMediaItem(mediaItem)
                playWhenReady = true
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                addListener(playerListener)
            }

        binding.player.apply {
            player = exoPlayer

            setOnClickListener {
                Log.d(BuildConfig.BUILD_TYPE, "onClick")

                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.prepare()
        binding.player.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.player.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, GifActivity::class.java)
    }
}