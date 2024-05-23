plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.android.room)
}

android {
    namespace = "com.shubhans.core.database"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(libs.org.mongodb.bson)
}