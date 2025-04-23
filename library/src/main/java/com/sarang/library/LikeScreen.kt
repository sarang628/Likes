package com.sarang.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


data class Like(
    val url: String,
    val name: String,
    val isFollow: Boolean,
    val followerId: Int,
)

sealed interface LikeUiState {
    data class Success(val list: List<Like>) : LikeUiState

    object CheckLogin : LikeUiState
    object Loading : LikeUiState
    object Error : LikeUiState
    object NeedLogin : LikeUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikeScreen(
    viewModel: LikeViewModel = hiltViewModel(),
    image: @Composable (Modifier, String, Dp?, Dp?, ContentScale?) -> Unit,
    reviewId: Int,
    onImage: (Int) -> Unit,
    onName: (Int) -> Unit,
    onBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Likes", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
        {
            when (uiState) {
                is LikeUiState.Error -> {
                    Text("error", modifier = Modifier.align(Alignment.Center))
                }

                is LikeUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                    LaunchedEffect(key1 = reviewId) {
                        viewModel.loadLikes(reviewId)
                    }
                }

                is LikeUiState.NeedLogin -> {
                    Text("로그인을 해주세요.", modifier = Modifier.align(Alignment.Center))
                }

                is LikeUiState.Success -> {
                    val list = (uiState as LikeUiState.Success).list
                    LazyColumn(Modifier.padding(paddingValues = it)) {
                        items(list.size) {
                            LikeRow(
                                image = image,
                                url = list[it].url,
                                name = list[it].name,
                                isFollow = list[it].isFollow,
                                onImage = { onImage.invoke(list[it].followerId) },
                                onFollow = { viewModel.follow(list[it].followerId) },
                                onName = { onName.invoke(list[it].followerId) }
                            )
                        }
                    }
                }

                LikeUiState.CheckLogin -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
