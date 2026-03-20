import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("maven-publish")
    id("signing")
}

// 读取 local.properties 中的敏感配置
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

// 辅助函数：优先从 local.properties 读取，否则从 gradle.properties 读取
fun getPropertyValue(key: String): String? {
    return localProperties.getProperty(key)
        ?: project.findProperty(key)?.toString()
        ?: System.getenv(key.replace(".", "_").uppercase())
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.yhz.composetoast"
        compileSdk = 36
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "toastKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // JVM Desktop
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    // Web (JS)
    js(IR) {
        browser()
        binaries.executable()
    }

    // WASM
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Compose dependencies
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.animation)
                // Lifecycle
                implementation(libs.androidx.lifecycle.viewmodelCompose)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Android specific dependencies
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {
                // iOS specific dependencies
            }
        }

        val desktopMain by getting {
            dependencies {
                // JVM Desktop specific dependencies
                implementation(compose.desktop.currentOs)
            }
        }

        val jsMain by getting {
            dependencies {
                // Web (JS) specific dependencies
            }
        }

        val wasmJsMain by getting {
            dependencies {
                // WASM specific dependencies
            }
        }
    }

}

// Maven Central publishing configuration
group = "io.github.ocnyang"  // Change to your Maven Central group ID (e.g., io.github.yourusername)
version = "1.0.0"

publishing {
    publications {
        // JitPack publication (existing)
        create<MavenPublication>("jitpack") {
            groupId = "com.github.ocnyang"
            artifactId = "compose-toast"
            version = "1.0.0"
        }
    }

    publications.withType<MavenPublication> {
        // Generate javadoc JAR for all publications
        val javadocJar = tasks.register<Jar>("${name}JavadocJar") {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        }
        artifact(javadocJar)

        // Configure POM for Maven Central
        pom {
            name.set("ComposeToast")
            description.set("A beautiful, customizable Toast library for Kotlin Multiplatform Compose")
            url.set("https://github.com/ocnyang/ComposeToast")

            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }

            developers {
                developer {
                    id.set("ocnyang")
                    name.set("OCN Yang")
                    email.set("ocnyang@gmail.com")  // Change to your email
                }
            }

            scm {
                connection.set("scm:git:git://github.com/ocnyang/ComposeToast.git")
                developerConnection.set("scm:git:ssh://github.com/ocnyang/ComposeToast.git")
                url.set("https://github.com/ocnyang/ComposeToast")
            }
        }
    }

    // Maven Central repositories
    repositories {
        // Method 1: Legacy Sonatype OSSRH (传统方式)
        maven {
            name = "sonatype"
            url = uri(
                if (version.toString().endsWith("SNAPSHOT")) {
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                } else {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                }
            )
            credentials {
                username = getPropertyValue("ossrhUsername")
                password = getPropertyValue("ossrhPassword")
            }
        }

        // Method 2: Maven Central Portal (推荐的新方式)
        // 使用 Portal Token，无需手动 Close/Release
        maven {
            name = "centralPortal"
            url = uri("https://central.sonatype.com/api/v1/publisher/upload")
            credentials {
                username = getPropertyValue("centralPortalUsername")
                password = getPropertyValue("centralPortalToken")
            }
        }
    }
}

// Signing configuration for Maven Central
signing {
    // 使用 GPG 命令行工具（优先从 local.properties 读取）
    val gpgKeyName = getPropertyValue("signing.gnupg.keyName")
    if (gpgKeyName != null) {
        useGpgCmd()
    }
}

// 在所有配置完成后设置签名
afterEvaluate {
    val gpgKeyName = getPropertyValue("signing.gnupg.keyName")
    if (gpgKeyName != null) {
        signing {
            sign(publishing.publications)
        }
    }
}
