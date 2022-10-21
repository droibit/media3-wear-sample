package com.example.media3.wear_sample

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.media3.wear_sample.databinding.ActivityMainBinding

@UnstableApi
class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var exoPlayer: ExoPlayer

    private val playerListener = object: Player.Listener {
        override fun onRenderedFirstFrame() {
            Log.d(BuildConfig.BUILD_TYPE, "#onRenderedFirstFrame")
            binding.progress.isGone = true
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e(BuildConfig.BUILD_TYPE, "ERROR:", error)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exoPlayer = ExoPlayer.Builder(this)
            .build().apply {
                val mediaItem = MediaItem.Builder()
                    // For tweet with native video.
                    .setUri(Uri.parse("https://video.twimg.com/ext_tw_video/869317980307415040/pu/pl/wcJQJ2nxiFU4ZZng.m3u8"))
                    .setMimeType("application/x-mpegURL")//(MimeTypes.APPLICATION_M3U8)
                    // For tweet with an animated GIF.
//                    .setUri("https://video.twimg.com/tweet_video/DBMDLy_U0AAqUWP.mp4")
//                    .setMimeType("video/mp4z")
                    .build()
                setMediaItem(mediaItem)
                playWhenReady = true
//                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                addListener(playerListener)
            }

        binding.player.apply {
            setShowNextButton(false)
            setShowPreviousButton(false)
            setShowVrButton(false)
            isControllerFullyVisible
            player = exoPlayer
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
}