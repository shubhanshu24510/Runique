plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.shubhans.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
}