package com.shubhans.analytics.analytics_features

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.shubhans.analyticss.data.di.analyticsModule
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import com.shubhans.presentation.AnalyticsDashboardScreenRoot
import com.shubhans.presentation.di.analyticsPresentationModule
import com.shubhans.runique.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

@SuppressLint("RestrictedApi")
class AnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(listOf(analyticsModule, analyticsPresentationModule))
        SplitCompat.installActivity(this)

        setContent {
            val navController = rememberNavController()
            RuniqueTheme {
                NavHost(
                    navController = navController, startDestination = "analytics_dashboard"
                ) {
                    composable("analytics_dashboard") {
                        AnalyticsDashboardScreenRoot(onBackClick = {
                            onBackPressed()
                        })
                    }
                }
            }
        }
    }
}