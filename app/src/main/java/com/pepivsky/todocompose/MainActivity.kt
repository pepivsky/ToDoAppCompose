package com.pepivsky.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.pepivsky.todocompose.navigation.SetupNavigation
import com.pepivsky.todocompose.ui.theme.ToDoComposeTheme
import com.pepivsky.todocompose.ui.viewmodels.SharedViewModel
import com.pepivsky.todocompose.util.RequestState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    // initialize viewModels
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // for splashScreen
        setupSplashScreen()
        /*installSplashScreen().apply {
            setKeepOnScreenCondition {
                //sharedViewModel.isLoading.value
                //sharedViewModel.allTasks.value is RequestState.Loading && sharedViewModel.sortState.value is RequestState.Loading
                sharedViewModel.sortState.value is RequestState.Loading
            }
        }*/
        setContent {
            ToDoComposeTheme {
                navController = rememberNavController()
                // call setup navigation to show ListScreen
                SetupNavigation(
                    navHostController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }

    private fun setupSplashScreen() {
        var keepSplashScreenOn = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.showSplash.collect {
                    keepSplashScreenOn = it
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoComposeTheme {
        Greeting("Android")
    }
}