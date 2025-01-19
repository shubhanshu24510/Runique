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
    onAnalyticsClicked: () -> Unit, navController: NavHostController, isLoggingIn: Boolean
) {
    NavHost(
        navController = navController, startDestination = if (isLoggingIn) Route.Run else Route.Auth
    ) {
        authGraph(navController)
        runGraph(
            navController, onAnalyticsClicked = onAnalyticsClicked
        )
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<Route.Auth>(
        startDestination = Route.Intro
    ) {
        composable<Route.Intro> {
            IntroScreenRoot(onSignInClick = {
                navController.navigate(Route.Login)
            }, onSignUpClick = {
                navController.navigate(Route.Register)
            })
        }
        composable<Route.Register> {
            RegisterScreenRoot(onSignInClick = {
                navController.navigate(Route.Login) {
                    popUpTo(Route.Register) {
                        inclusive = true
                        saveState = true
                    }
                    restoreState = true
                }
            }, onRegisterSuccessful = {
                navController.navigate(Route.Login)
            })
        }
        composable<Route.Login> {
            LoginScreenRoot(onSignUpClick = {
                navController.navigate(Route.Register) {
                    popUpTo(Route.Login) {
                        inclusive = true
                        saveState = true
                    }
                    restoreState = true
                }
            }, onLoginSuccess = {
                navController.navigate(Route.Run) {
                    popUpTo(Route.Auth) {
                        inclusive = true
                    }
                }
            })
        }
    }
}

private fun NavGraphBuilder.runGraph(
    navController: NavHostController,
    onAnalyticsClicked: () -> Unit,
) {
    navigation<Route.Run>(
        startDestination = Route.RunOverview
    ) {
        composable<Route.RunOverview> {
            RunOverViewScreenRoot(onAnalyticsClicked = onAnalyticsClicked, onStartRunClicked = {
                navController.navigate(Route.ActiveRun)
            }, onLogOutClicked = {
                navController.navigate(Route.Auth) {
                    popUpTo(Route.Run) {
                        inclusive = true
                    }
                }
            })
        }
        composable<Route.ActiveRun>(
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
            })
        }
    }
}
