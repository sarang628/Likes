package com.sarang.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet

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
        Text(modifier = Modifier
            .layoutId("name")
            .clickable { onName.invoke() }, text = name
        )
        Button(
            onClick = { onFollow.invoke() }, modifier = Modifier
                .width(120.dp)
                .height(30.dp)
                .layoutId("follow"),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = if (!isFollow) "Follow" else "Following")
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