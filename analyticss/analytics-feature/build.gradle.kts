plugins {
   alias(libs.plugins.runique.android.dynamic.feature)
}
android {
    namespace = "com.shubhans.analyticss.analytics_feature"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":app"))
    implementation(libs.androidx.navigation.compose)

    api(projects.analyticss.presentation)
    implementation(projects.analyticss.domain)
    implementation(projects.analyticss.data)
    implementation(projects.core.database)
}