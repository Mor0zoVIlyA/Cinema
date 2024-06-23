package com.example.cinemafinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.description_screen.presentation.view_model.DescriptionRemoteViewModel
import com.example.cinemafinder.ui.theme.CinemaFinderTheme
import com.main_screen.presentation.LocalDataBottomPanel
import com.main_screen.presentation.MainScreen
import com.main_screen.presentation.RemoteDataBottomPanel
import com.main_screen.presentation.view_models.LocalMainViewModel
import com.main_screen.presentation.view_models.RemoteMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import androidx.activity.viewModels
import com.description_screen.presentation.DescriptionScreen
import com.description_screen.presentation.view_model.DescriptionLocalViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(this)
                }
            }
        }
    }
}

@Composable
fun MyApp(activity: MainActivity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "remoteMainScreen") {
        composable("remoteMainScreen") {
            val mainViewModel = hiltViewModel<RemoteMainViewModel>()
            val uiState by mainViewModel.getUiState().collectAsState()
            MainScreen(
                state = uiState,
                itemClick = mainViewModel::itemClick,
                longItemClick = mainViewModel::longClick,
                navController=navController,
                bottomPanel =  {
                    RemoteDataBottomPanel {
                        navController.navigate("localMainScreen")
                    }
                }
            )
        }
        composable("localMainScreen"){
            val mainViewModel = hiltViewModel<LocalMainViewModel>()
            val uiState by mainViewModel.getUiState().collectAsState()
            MainScreen(
                state = uiState,
                itemClick = mainViewModel::itemClick,
                longItemClick = mainViewModel::longClick,
                navController=navController,
                bottomPanel =  {
                    LocalDataBottomPanel {
                        navController.navigate("remoteMainScreen")
                    }
                }
            )
        }
        composable("remoteDetails/{itemId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("itemId") ?: "-1"
            val remoteViewModel by activity.viewModels<DescriptionRemoteViewModel> (
                extrasProducer = {
                    activity.defaultViewModelCreationExtras.withCreationCallback<DescriptionRemoteViewModel.ViewModelFactory> { factory ->
                        factory.create(id.toInt())
                    }
                }
            )
            val descriptionState by remoteViewModel.getUiState().collectAsState()
            DescriptionScreen(descriptionState, navController)

        }
        composable("localDetails/{itemId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("itemId") ?: "-1"
            val localViewModel by activity.viewModels<DescriptionLocalViewModel> (
                extrasProducer = {
                    activity.defaultViewModelCreationExtras.withCreationCallback<DescriptionLocalViewModel.ViewModelFactory> { factory ->
                        factory.create(id.toInt())
                    }
                }
            )
            val descriptionState by localViewModel.getUiState().collectAsState()
            DescriptionScreen(descriptionState, navController)
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CinemaFinderTheme {
        Greeting("Android")
    }
}