package com.sarang.torang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.sarang.library.LikeScreen
import com.sarang.torang.di.image.provideTorangAsyncImage
import com.sarang.torang.repository.LoginRepository
import com.sarang.torang.repository.LoginRepositoryTest
import com.sarang.torang.ui.theme.LikesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var loginRepository: LoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LikesTheme {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Box(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp)) {
                        LikeScreen(
                            image = provideTorangAsyncImage(),
                            reviewId = 399,
                            onName = {},
                            onImage = {}
                        )
                    }
                    LoginRepositoryTest(loginRepository = loginRepository)
                }
            }
        }
    }
}