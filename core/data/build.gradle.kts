plugins {
   alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.shubhans.core.data"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(libs.timber)
}