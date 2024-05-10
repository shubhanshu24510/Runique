package com.shubhans.runique

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.shubhans.auth.presentation.intro.IntroScreenRoot
import com.shubhans.auth.presentation.logIn.LoginScreenRoot
import com.shubhans.auth.presentation.register.RegisterScreenRoot


@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController, startDestination = if(isLoggedIn) "run" else "auth"
    ) {
        authGraph(navController = navController)
        runGraph(navController = navController)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "intro", route = "auth"
    ) {
        composable(route = "intro") {
            IntroScreenRoot(onSignInClick = {
                navController.navigate("login")
            }, onSignUpClick = {
                navController.navigate("register")
            })
        }
        composable(route = "register") {
            RegisterScreenRoot(onSignInClick = {
                navController.navigate("login") {
                    popUpTo("register") {
                        inclusive = true
                        saveState = true
                    }
                    restoreState = true
                }
            }, onRegisterSucessfull = {
                navController.navigate("login")
            })
        }
        composable(route = "login") {
            LoginScreenRoot(onSignUpClick = {
                navController.navigate("register") {
                    popUpTo("login") {
                        inclusive = true
                        saveState = true
                    }
                    restoreState = true
                }
            }, onLoginSuccess = {
                navController.navigate("run") {
                    popUpTo("auth") {
                        inclusive = true
                    }
                }
            })
        }
    }
}

private fun NavGraphBuilder.runGraph(navController: NavHostController) {
    navigation(
        startDestination = "run_Overview", route = "run"
    ) {
        composable(route = "run_Overview") {
            Text(text = "Run Overview")
        }
    }
}
