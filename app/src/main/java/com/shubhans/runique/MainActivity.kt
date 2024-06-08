package com.shubhans.runique

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private lateinit var splitInstallManager: SplitInstallManager
    private val split_installListener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.INSTALLED -> {
                viewModel.setAnalyticsVisibility(false)
                Toast.makeText(
                    applicationContext,
                    R.string.installed_analytics_features,
                    Toast.LENGTH_LONG
                ).show()
            }

            SplitInstallSessionStatus.INSTALLING -> {
                viewModel.setAnalyticsVisibility(true)
            }

            SplitInstallSessionStatus.DOWNLOADING -> {
                viewModel.setAnalyticsVisibility(true)
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                splitInstallManager.startConfirmationDialogForResult(state, this, 0)
            }

            SplitInstallSessionStatus.FAILED -> {
                viewModel.setAnalyticsVisibility(false)
                Toast.makeText(
                    applicationContext,
                    R.string.error_installing_analytics_features,
                    Toast.LENGTH_LONG).show()
            }
        }

    }
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }
        enableEdgeToEdge()
        splitInstallManager = SplitInstallManagerFactory.create(application)
        setContent {
            RuniqueTheme {
                if (!viewModel.state.isCheckingAuth) {
                    val navController = rememberNavController()
                    NavigationRoot(
                        navController = navController,
                        isLoggingIn = viewModel.state.isLoggedIn,
                        onAnalyticsClicked = {
                            installOrStartAnalyticsFeatures()
                        }
                    )
                    if(viewModel.state.showAnalyticsInstallDialog) {
                        Dialog(onDismissRequest = { /*TODO*/ }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = stringResource(id = R.string.installing_analytics_features),
                                    color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager.registerListener(split_installListener)
    }

    override fun onPause() {
        super.onPause()
        splitInstallManager.unregisterListener(split_installListener)
    }

    private fun installOrStartAnalyticsFeatures() {
        if (splitInstallManager.installedModules.contains("analytics_features")) {
            Intent()
                .setClassName(
                    packageName,
                    "com.shubhans.analytics.analytics_features.AnalyticsActivity"
                )
                .also(::startActivity)
            return
        }

        val request = SplitInstallRequest.newBuilder()
            .addModule("analytics_features")
            .build()

        splitInstallManager.startInstall(request)
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    R.string.error_installing_analytics_features,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}