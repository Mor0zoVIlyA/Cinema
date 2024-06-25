package com.description_screen.presentation

import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.description_screen.domain.models.FilmDescription
import com.description_screen.presentation.view_model.DescriptionLocalViewModel
import com.description_screen.presentation.view_model.DescriptionRemoteViewModel
import dagger.hilt.android.lifecycle.withCreationCallback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionScreen(
    descriptionState: FilmDescription,
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = descriptionState.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                SubcomposeAsyncImage(
                    model = descriptionState.url,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.widthIn(350.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(16.dp)),
                ) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Success) {
                        state.result.drawable
                        SubcomposeAsyncImageContent()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = descriptionState.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = descriptionState.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 18.sp,
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Жанры: ${descriptionState.genre}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Страна: ${descriptionState.country}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                )
            }
        }
    )
}
@Composable
fun DescriptionScreenFromLocal(
    id: Int,
    navController: NavController
){
    Log.d("TAG", "MyApp: receive ${id}")
    val remoteViewModel = hiltViewModel<DescriptionLocalViewModel, DescriptionLocalViewModel.ViewModelFactory> { factory ->
        factory.create(id)
    }
    val descriptionState by remoteViewModel.getUiState().collectAsState()
    DescriptionScreen(descriptionState, navController)
}

@Composable
fun DescriptionScreenFromRemote(
    id: Int,
    navController: NavController
){
    Log.d("TAG", "MyApp: receive ${id}")
    val remoteViewModel = hiltViewModel<DescriptionRemoteViewModel, DescriptionRemoteViewModel.ViewModelFactory> { factory ->
        factory.create(id)
    }
    val descriptionState by remoteViewModel.getUiState().collectAsState()
    DescriptionScreen(descriptionState, navController)
}