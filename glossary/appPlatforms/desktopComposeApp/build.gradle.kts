import org.jetbrains.compose.desktop.application.dsl.TargetFormat


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {

    jvm("desktop") {
        withJava()
    }

    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(projects.shared)

            //decompose
            implementation(libs.decompose)
            implementation(libs.decompose.compose)


            //compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.koin.core)

            implementation(projects.ui.compose.theme)
            implementation(projects.ui.compose.kit)
            implementation(projects.appPlatforms.composeAppShared)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.jna)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ru.mission.glossary"
            packageVersion = "1.0.0"
        }
    }
}
