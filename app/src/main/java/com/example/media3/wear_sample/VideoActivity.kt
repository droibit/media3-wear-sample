package com.example.media3.wear_sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.media3.wear_sample.databinding.ActivityVideoBinding
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level

@OptIn(UnstableApi::class)
class VideoActivity : ComponentActivity() {
    private lateinit var binding: ActivityVideoBinding

    private lateinit var exoPlayer: ExoPlayer

    private val playerListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            Log.d(BuildConfig.BUILD_TYPE, "#onRenderedFirstFrame")
            binding.progress.isGone = true
            binding.player.controllerAutoShow = true

            Log.d(BuildConfig.BUILD_TYPE, "volume=${exoPlayer.volume}, deviceVolume=${exoPlayer.deviceVolume}")
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e(BuildConfig.BUILD_TYPE, "ERROR:", error)
        }
    }

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = Level.BASIC
                }
            )
            .build()
    }

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(
                    OkHttpDataSource.Factory(okHttp)
                )
            )
            .build().apply {
                val mediaItem = MediaItem.Builder()
                    // For tweet with native video.
                    .setUri("https://video.twimg.com/ext_tw_video/869317980307415040/pu/pl/wcJQJ2nxiFU4ZZng.m3u8".toUri())
                    .setMimeType("application/x-mpegURL")//(MimeTypes.APPLICATION_M3U8)
                    .build()
                setMediaItem(mediaItem)
                playWhenReady = true
                addListener(playerListener)
            }

        binding.player.apply {
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

    companion object {
        fun createIntent(context: Context) = Intent(context, VideoActivity::class.java)
    }
}