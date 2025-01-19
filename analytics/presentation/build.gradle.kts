plugins {
   alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.shubhans.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}