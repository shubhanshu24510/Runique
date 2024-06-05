package com.shubhans.runique

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.shubhans.auth.presentation.intro.IntroScreenRoot
import com.shubhans.auth.presentation.logIn.LoginScreenRoot
import com.shubhans.auth.presentation.register.RegisterScreenRoot
import com.shubhans.run.presentation.run_active.ActiveRunScreenRoot
import com.shubhans.run.presentation.run_active.services.ActiveRunService
import com.shubhans.run.presentation.run_overView.RunOverViewScreenRoot


@Composable
fun NavigationRoot(
    navController: NavHostController, isLoggingIn: Boolean
) {
    NavHost(
        navController = navController, startDestination = if (isLoggingIn) "run" else "auth"
    ) {
        authGraph(navController)
        runGraph(navController)
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
            }, onRegisterSuccessful = {
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
        startDestination = "run_Overview",
        route = "run"
    ) {
        composable(route = "run_Overview") {
            RunOverViewScreenRoot(
                onStartRunClicked = {
                navController.navigate("active_run")
            },
                onLogOutClicked = {
                    navController.navigate("auth"){
                        popUpTo("run") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = "active_run",
            deepLinks = listOf(navDeepLink { uriPattern = "runique://active_run" })
        ) {
            val context = LocalContext.current
            ActiveRunScreenRoot(onFinish = {
                navController.navigateUp()
            }, onBack = {
                navController.navigateUp()
            }, onServiceToggle = { shouldServiceRun ->
                if (shouldServiceRun) {
                    context.startService(
                        ActiveRunService.createStartIntent(
                            context = context, activityClass = MainActivity::class.java
                        )
                    )
                } else {
                    context.startService(
                        ActiveRunService.createStopIntent(
                            context = context
                        )
                    )
                }
            }
            )
        }
    }
}
