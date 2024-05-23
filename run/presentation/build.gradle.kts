plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.shubhans.run.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.run.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)

    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)
}