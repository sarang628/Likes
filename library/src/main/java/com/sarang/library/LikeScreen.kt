package com.sarang.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel


data class Like(
    val url: String,
    val name: String,
    val isFollow: Boolean,
    val followerId: Int,
)

sealed interface LikeUiState {
    data class Success(val list: List<Like>) : LikeUiState

    object Loading : LikeUiState
    object Error : LikeUiState
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

    LaunchedEffect(key1 = reviewId) {
        viewModel.loadLikes(reviewId)
    }

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
        Box(modifier = Modifier.fillMaxSize())
        {
            if (uiState is LikeUiState.Success) {
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

            if (uiState is LikeUiState.Loading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun LikeRow(
    url: String,
    name: String,
    isFollow: Boolean,
    onImage: () -> Unit,
    onName: () -> Unit,
    onFollow: () -> Unit,
    image: @Composable (Modifier, String, Dp?, Dp?, ContentScale?) -> Unit,
) {
    ConstraintLayout(
        constraintSet = likeRowConstraintSet(),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        image.invoke(
            Modifier
                .layoutId("image")
                .size(50.dp)
                .clip(CircleShape)
                .clickable {
                    onImage.invoke()
                }, url, 30.dp, 30.dp, ContentScale.Crop
        )
        Text(modifier = Modifier.layoutId("name"), text = name)
        Button(
            onClick = { onFollow.invoke() }, modifier = Modifier
                .width(120.dp)
                .height(30.dp)
                .layoutId("follow"),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = if (!isFollow) "Follow" else "Following",
                modifier = Modifier.clickable { onName.invoke() })
        }
    }
}

fun likeRowConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val image = createRefFor("image")
        val name = createRefFor("name")
        val id = createRefFor("id")
        val follow = createRefFor("follow")

        constrain(image) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
        }

        constrain(name) {
            start.linkTo(image.end, margin = 18.dp)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }

        constrain(follow) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
    }
}

@Preview
@Composable
fun test() {
    LikeRow(/*Preview*/
        image = { _, _, _, _, _ -> },
        url = "",
        name = "aaa",
        isFollow = true,
        onFollow = {},
        onImage = {},
        onName = {})
}