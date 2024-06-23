package com.main_screen.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.main_screen.domain.FilmCard
import com.main_screen.presentation.us_state.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: UiState,
    itemClick: (FilmCard) -> Unit,
    longItemClick: (FilmCard) -> Unit,
    navController: NavController,
    bottomPanel:  @Composable ()-> Unit
) {

    if (!state.internetAbility) {
        NoInternetScreen()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(state.label)) }
            )
        },
        content = { padding ->

            Column(modifier = Modifier.padding(padding)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(state.filmList) { film ->
                        FilmItem(
                            filmCard = film,
                            itemClick = itemClick,
                            longItemClick = longItemClick
                        )
                    }
                }
                bottomPanel()
            }
        }

    )

}

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilmItem(
    filmCard: FilmCard,
    itemClick: (FilmCard) -> Unit,
    longItemClick: (FilmCard) -> Unit,

    ) {
    val hapticFeedBack = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { itemClick(filmCard) },
                onLongClick = {
                    longItemClick(filmCard)
                    hapticFeedBack.performHapticFeedback(HapticFeedbackType.LongPress)
                },
            )
    ) {
        val showShimmer = remember { mutableStateOf(true) }
        SubcomposeAsyncImage(
            model = filmCard.posterUrl,
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(shimmerBrush(targetValue = 1300f, showShimmer = showShimmer.value))
                .width(150.dp)
                .heightIn(min = 220.dp)
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Success) {
                showShimmer.value = false
                state.result.drawable
                SubcomposeAsyncImageContent()
            }
        }

        Column {
            Text(filmCard.year)
            Text(filmCard.nameRu)
        }
    }
}

@Composable
private fun PanelWithButton(remote: () -> Unit, local: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { remote.invoke() }) {
            Text("Remote source", fontSize = 16.sp)
        }
        Button(onClick = {local.invoke() }) {
            Text("Local source", fontSize = 16.sp)
        }
    }
}
@Composable
fun LocalDataBottomPanel(click: () -> Unit){
    PanelWithButton(click, {})
}


@Composable
fun RemoteDataBottomPanel(click: () -> Unit){
    PanelWithButton({}, click)
}
@Composable
fun NoInternetScreen() {

}

@Preview
@Composable
private fun PanelWithButtonPreview() {

}