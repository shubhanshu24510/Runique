import com.android.build.api.dsl.LibraryExtension
import com.shubhans.convention.addUiLayerDependencies
import com.shubhans.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("runique.android.library.compose")
            }
            dependencies {
                addUiLayerDependencies(target)

            }
        }
    }
}