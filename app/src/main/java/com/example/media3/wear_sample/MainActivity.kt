package com.example.media3.wear_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.media3.common.util.UnstableApi
import com.example.media3.wear_sample.databinding.ActivityMainBinding

@UnstableApi
class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.video.setOnClickListener {
            startActivity(VideoActivity.createIntent(this))
        }
        binding.gif.setOnClickListener {
            startActivity(GifActivity.createIntent(this))
        }
    }
}