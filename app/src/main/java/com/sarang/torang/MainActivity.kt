package com.sarang.torang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sarang.library.LikeScreen
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.ui.theme.LikesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LikesTheme {
                LikeScreen(
                    image = provideTorangAsyncImage(),
                    reviewId = 399,
                    onName = {},
                    onImage = {}
                )
            }
        }
    }
}