import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.shubhans.convention.ExtensionType
import com.shubhans.convention.addUiLayerDependencies
import com.shubhans.convention.configureAndroidCompose
import com.shubhans.convention.configureBuildTypes
import com.shubhans.convention.configureKotlinAndroid
import com.shubhans.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import kotlin.jvm.internal.Intrinsics.Kotlin

class AndroidDynamicsFeaturesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.dynamic-feature")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<DynamicFeatureExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.DYNAMIC_FEATURE
                )
            }
            dependencies {
                addUiLayerDependencies(target)
                "testImplementation"(kotlin("test"))

            }
        }
    }
}