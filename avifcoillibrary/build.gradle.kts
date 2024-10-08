plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

task("androidSourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/MessengerBotTeam/avif-coder-coil")
                credentials {
                    username = project.findProperty("gpr.user").toString() ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key").toString() ?: System.getenv("TOKEN")
                }
            }
        }
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = "org.msgbot"
                artifactId = "avif-coder-coil"
                version = "1.6.9"
                from(components.findByName("release"))
//                artifact("androidSourcesJar")
            }
        }
    }
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    namespace = "com.github.awxkee.avifcodercoil"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    api("io.coil-kt.coil3:coil:3.0.0-alpha10")
    api("com.github.awxkee:avif-coder:1.8.0")
}