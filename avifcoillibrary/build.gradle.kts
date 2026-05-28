plugins {
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jetbrains.dokka-javadoc")
    id("maven-publish")
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    description = "Dokka가 생성한 Javadoc을 JAR로 압축합니다."
    from(tasks.dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}
val dokkaHtmlZip by tasks.registering(Zip::class) {
    description = "Dokka가 생성한 Html을 Zip로 압축합니다."

    from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html")
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/MessengerBotTeam/avif-coder-coil")
                credentials {
                    username = project.findProperty("gpr.user")?.toString() ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key")?.toString() ?: System.getenv("TOKEN")
                }

            }
        }
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = "org.msgbot"
                artifactId = "avif-coder-coil"
                version = "2.2.1"

                from(components.findByName("release"))
                artifact(dokkaJavadocJar)
                artifact(dokkaHtmlZip)

                pom {
                    name.set("AVIF Coder Coil")
                    description.set("AVIF encoder/decoder plugin for coil for Android")
                    inceptionYear.set("2025")
                    url.set("https://github.com/MessengerBotTeam/avif-coder-coil")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                        license {
                            name.set("The 3-Clause BSD License")
                            url.set("https://opensource.org/license/bsd-3-clause")
                            description.set("https://opensource.org/license/bsd-3-clause")
                        }
                    }
                    developers {
                        developer {
                            id.set("MessengerBotTeam")
                            name.set("MessengerBotTeam")
                            url.set("https://github.com/MessengerBotTeam")
                        }
                        developer {
                            id.set("awxkee")
                            name.set("Radzivon Bartoshyk")
                            url.set("https://github.com/awxkee")
                            email.set("radzivon.bartoshyk@proton.me")
                        }
                    }
                    scm {
                        url.set("https://github.com/MessengerBotTeam/avif-coder-coil.git")
                        connection.set("scm:git:git@github.com:MessengerBotTeam/avif-coder-coil.git")
                        developerConnection.set("scm:git:ssh://git@github.com/MessengerBotTeam/avif-coder-coil.git")
                    }
                }
            }
        }
    }
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
//            withJavadocJar()
        }
    }

    namespace = "com.github.awxkee.avifcodercoil"
    compileSdk = 37

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    api("androidx.core:core-ktx:1.18.0")
    api("io.coil-kt.coil3:coil:3.4.0")
    api("io.github.awxkee:avif-coder:2.2.1")
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:2.2.0")
}