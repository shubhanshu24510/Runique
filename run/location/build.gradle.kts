plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.shubhans.run.location"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.run.domain)
    implementation(libs.bundles.koin)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)
}