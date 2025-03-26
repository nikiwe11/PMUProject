package com.example.chatappsingle.ui.theme

import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatappsingle.R
import com.example.chatappsingle.general.Constants
import com.example.chatappsingle.general.TransparentSurfaceWithGradient
import com.example.chatappsingle.general.selectFromTheme
import com.example.chatappsingle.screens.LoginScreen
import com.example.chatappsingle.screens.MainMenuScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.chatappsingle.general.Constants.Routes as routes

@Composable
fun ChatApp(
    name: String, modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(), // NavController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Gets what screen is displayed
    val currentScreen =
        backStackEntry?.destination?.route ?: routes.REGISTRATION


    var topAppBarTitle by rememberSaveable { mutableStateOf(R.string.main_menu) }
    LaunchedEffect(currentScreen) {
        topAppBarTitle = when (currentScreen) {
            routes.MAIN_MENU -> R.string.main_menu
            // todo
//            routes.REGISTRATION -> R.string.registration

            else -> {
                R.string.chat
//                R.string.error_null_data
            }
        }
    }
    Text(
        text = "Hellon $name!",
        modifier = modifier
    )
    Scaffold(
        topBar = {
            CustomTopAppBar(
                currentScreenName = stringResource(topAppBarTitle),
                navigateToLogin = { navController.navigate(routes.LOGIN) })
        }) { innerPadding ->
        val shouldShowDialog = remember { mutableStateOf(false) }
        val postRequestValue = remember { mutableStateOf("") }


        // Backhandler for backwards compatibility without bottom navigation
//        BackHandler {
//            if (currentScreen != routes.MAIN_MENU) {
//                        if (currentScreen == routes.REGISTRATION) {
//                            showConfirmation = true
//                            confirmationFunction = { navController.navigate(routes.MAIN_MENU) }
//                            //show warning
//                        } else {
//                            navController.navigate(routes.MAIN_MENU)
//                        }
//                    }
//        }
        // Background
        Surface(modifier = Modifier.fillMaxSize()) {

        }
        // Displayed anywhere when there is no connection to the printer


        // Navigation between screens is controlled here
        NavHost(
            navController = navController,
            startDestination = routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {


            composable(route = routes.LOGIN) {
                LoginScreen(onClick = { navController.navigate(routes.MAIN_MENU) })
            }
            composable(route = routes.MAIN_MENU){
                MainMenuScreen()
            }
        }

    }
}

@Composable
fun CustomTopAppBar(
    currentScreenName: String,
    navigateToLogin: () -> Unit,
) {
    val loginString = "login" // todo

    TransparentSurfaceWithGradient(
        modifier = Modifier.padding(4.dp), // todo hardcoded
        alpha = 0.42f,
        brush = selectFromTheme(
            Brush.horizontalGradient(
                colors = Constants.Gradient.RED_TO_BLACK.reversed(),
            ), Brush.horizontalGradient(
                colors = Constants.Gradient.BLACK_TO_GRAY.reversed()
            )
        ),
        border = null
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    if (currentScreenName != loginString) {
                        navigateToLogin()
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val text = "TOP TITLE"
            Text(
                modifier = Modifier.alpha(1f), text = text, style = if (text.length < 23) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    MaterialTheme.typography.titleLarge
                }, textAlign = TextAlign.Center, color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatAppSingleTheme {
        ChatApp("Android")
    }
}