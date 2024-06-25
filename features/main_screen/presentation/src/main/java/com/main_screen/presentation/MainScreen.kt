package com.main_screen.presentation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.main_screen.domain.FilmCard
import com.main_screen.presentation.us_state.UiItem
import com.main_screen.presentation.us_state.UiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: UiState,
    itemClick: (FilmCard, NavController) -> Unit,
    longItemClick: (FilmCard) -> Unit,
    deleteClick: (FilmCard, Boolean) -> Unit,
    navController: NavController,
    bottomPanel: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(state.label)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        content = { padding ->
            Column {
                Column(modifier = Modifier.weight(1f)) {
                    if (!state.internetAbility) {
                        NoInternetScreen()
                    } else {
                        Column(modifier = Modifier.padding(padding)) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                items(state.filmList) { film ->
                                    FilmItem(
                                        uiItem = film,
                                        itemClick = itemClick,
                                        longItemClick = longItemClick,
                                        deleteClick = deleteClick,
                                        navController = navController
                                    )
                                }
                            }
                        }
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
    uiItem: UiItem,
    itemClick: (FilmCard, NavController) -> Unit,
    longItemClick: (FilmCard) -> Unit,
    deleteClick:(FilmCard, Boolean) -> Unit,
    navController: NavController
) {
    val hapticFeedBack = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    itemClick(uiItem.filmCard, navController)
                          },
                onLongClick = {
                    longItemClick(uiItem.filmCard)
                    hapticFeedBack.performHapticFeedback(HapticFeedbackType.LongPress)
                },
            )
    ) {
        val showShimmer = remember { mutableStateOf(true) }
        SubcomposeAsyncImage(
            model = uiItem.filmCard.posterUrl,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(uiItem.filmCard.year)
                Text(uiItem.filmCard.nameRu)
            }

            Checkbox(
                checked = uiItem.isFavorites,
                onCheckedChange = {
                    deleteClick(uiItem.filmCard, uiItem.isFavorites)
                },

            )
        }
    }
}

@Composable
private fun PanelWithButton(remote: () -> Unit, local: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { remote.invoke() }) {
            Text(stringResource(R.string.remote_source), fontSize = 16.sp)
        }
        Button(onClick = { local.invoke() }) {
            Text(stringResource(R.string.local_source), fontSize = 16.sp)
        }
    }
}

@Composable
fun LocalDataBottomPanel(click: () -> Unit) {
    PanelWithButton(click, {})
}


@Composable
fun RemoteDataBottomPanel(click: () -> Unit) {
    PanelWithButton({}, click)
}

@Composable
fun NoInternetScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.no_internet),
            contentDescription = "No Internet",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.no_internet_connection),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 20.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.check_internet_connection),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = Color.Gray
            ),
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PanelWithButtonPreview() {

}