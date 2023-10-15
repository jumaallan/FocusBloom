/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.nativeCocoapod)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.joelkanyi.focusbloom.shared"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/commonMain/resources")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
    }

    cocoapods {
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
            transitiveExport = true
            compilations.all {
                kotlinOptions.freeCompilerArgs += arrayOf("-linker-options", "-lsqlite3")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.koin.core)
                api(libs.koin.compose)

                api(compose.material3)
                api(compose.material)
                api(compose.materialIconsExtended)

                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.bottomSheetNavigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.tabNavigator)

                implementation(libs.kotlinX.serializationJson)

                implementation(libs.material3.window.size.multiplatform)

                implementation(libs.sqlDelight.runtime)
                implementation(libs.sqlDelight.coroutine)
                implementation(libs.primitive.adapters)

                api(libs.multiplatformSettings.noArg)
                api(libs.multiplatformSettings.coroutines)

                api(libs.napier)

                implementation(libs.kotlinX.dateTime)
                implementation(libs.koalaplot.core)

                implementation(libs.korau)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sqlDelight.android)
                implementation(libs.accompanist.systemUIController)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqlDelight.jvm)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosX64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.sqlDelight.native)
                implementation(libs.components.resources)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:web-worker-driver:2.0.0")
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.0"))
                implementation(npm("sql.js", "1.7.0"))
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }
    }
}

sqldelight {
    databases {
        create("BloomDatabase") {
            packageName.set("com.joelkanyi.focusbloom.core.data.local.sqldelight")
            generateAsync.set(true)
        }
    }
}
