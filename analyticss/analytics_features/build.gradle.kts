plugins {
    alias(libs.plugins.runique.android.dynamic.feature)
}
android {
    namespace = "com.shubhans.analytics.analytics_features"
}

dependencies {
    implementation(project(":app"))
    implementation(libs.androidx.navigation.compose)

    api(projects.analyticss.presentation)
    implementation(projects.analyticss.domain)
    implementation(projects.analyticss.data)
    implementation(projects.core.database)
}