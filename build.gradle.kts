// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies{
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

}