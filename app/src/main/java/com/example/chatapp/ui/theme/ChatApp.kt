package com.example.chatapp.ui.theme

import CustomTopBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapp.screens.LoginScreen
import com.example.chatapp.screens.MainMenuScreen
import com.example.chatapp.R
import com.example.chatapp.screens.AddFriendScreen
import com.example.chatapp.screens.ChatDestination
import com.example.chatapp.screens.ChatScreen
import com.example.chatapp.screens.FriendProfile
import com.example.chatapp.screens.ProfileScreen
import com.example.chatapp.screens.SignUpScreen
import com.example.chatapp.viewmodel.MainViewModel
import com.example.chatapp.general.Constants.Routes as routes

@Composable
fun ChatApp(
    mainViewModel: MainViewModel = viewModel(),
    navController: NavHostController = rememberNavController(), // NavController,
) {
    val uiState by mainViewModel.uiState.collectAsState()

    val backStackEntry by navController.currentBackStackEntryAsState()
    // Gets what screen is displayed
    val currentScreen =
        backStackEntry?.destination?.route ?: routes.REGISTRATION

    // TODO: тва да се махне изобщо(?) мисля че седи грозно
    var topAppBarTitle by rememberSaveable { mutableStateOf(R.string.main_menu) }
    if (uiState.showError) {
        AlertDialog(
            onDismissRequest = { mainViewModel.setErrorState(false, "") },
            title = { Text("Warning!") },
            text = { Text(uiState.error) },
            dismissButton = {},
            confirmButton = {}
        )
    }
    LaunchedEffect(currentScreen) {
        topAppBarTitle = when (currentScreen) {
            routes.MAIN_MENU -> R.string.main_menu
            routes.SIGN_UP -> R.string.sign_up
            routes.LOGIN -> R.string.login
            routes.ADD_FRIEND -> R.string.add_friend
            // todo
//            routes.REGISTRATION -> R.string.registration

            else -> {
                R.string.chat
//                R.string.error_null_data
            }
        }
    }
    Scaffold(
        topBar = {
//            CustomTopBar(
//                currentScreenName = stringResource(topAppBarTitle),
//            )


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
        //  Background (can set different color) todo Kris: Surface(modifier = Modifier.fillMaxSize(),color = todo )
        Surface(modifier = Modifier.fillMaxSize()) {

        }


        // Navigation between screens is controlled here
        NavHost(
            navController = navController,
            startDestination = routes.MAIN_MENU,
            modifier = Modifier.padding(innerPadding)
        ) {


            composable(route = routes.LOGIN) {
                LoginScreen(
                    navigateToMainMenu = { navController.navigate(routes.MAIN_MENU) },
                    navigateToSignUp = { navController.navigate(routes.SIGN_UP) })
            }
            composable(route = routes.MAIN_MENU) {
                MainMenuScreen(navigateToLogin = {
                    navController.navigate(routes.LOGIN) {
                        popUpTo(routes.MAIN_MENU) { inclusive = true }
                    }
                }, navigateToAddFriend = { navController.navigate(routes.ADD_FRIEND) },
                    navigateToChat = { navController.navigate("${ChatDestination.route}/${it}") }, // todo add args
                    navigateToProfile = { navController.navigate(routes.PROFILE_SETTINGS) })

            }
            composable(route = routes.SIGN_UP) {
                SignUpScreen(
                    toggleError = { bool, error -> mainViewModel.setErrorState(bool, error) },
                    navigateToLogin = { navController.navigate(routes.LOGIN) },
                    navigateToMainMenu = {
                        navController.navigate(routes.MAIN_MENU) {
                            popUpTo(routes.SIGN_UP) { inclusive = true }
                        }
                    })
            }
            composable(route = routes.ADD_FRIEND) {
                AddFriendScreen(navigateToMainMenu = { navController.navigate(routes.MAIN_MENU) })
            }
            composable(
                route = ChatDestination.routeWithArgs, arguments = listOf(
                    navArgument(ChatDestination.chatIdArg) {
                        type = NavType.StringType
                    }
                )
            ) {
                ChatScreen(
                    navigateToMainMenu = { navController.navigate(routes.MAIN_MENU) },
                    navigateToFriendProfile = { navController.navigate(routes.CHAT_SETTINGS) })
            }
            composable(route = routes.PROFILE_SETTINGS) {
                ProfileScreen(navigateToMainMenu = { navController.navigate(routes.MAIN_MENU) })
//                    navigateToLogin = {
//                        navController.navigate(routes.LOGIN,)})
            }
            composable(route = routes.CHAT_SETTINGS) {
                FriendProfile(navigateToChat = { navController.navigate(routes.CHAT) })
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatAppTheme {
        ChatApp()
    }
}