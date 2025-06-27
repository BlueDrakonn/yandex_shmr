// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.0" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
}



