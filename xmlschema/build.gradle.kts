/*
 * Copyright (c) 2021.
 *
 * This file is part of XmlUtil.
 *
 * This file is licenced to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You should have received a copy of the license with the source distribution.
 * Alternatively, you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

@file:Suppress("PropertyName")

import net.devrieze.gradle.ext.*
import org.gradle.api.attributes.java.TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlinSerialization)
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
    idea
    alias(libs.plugins.binaryValidator)
}

val schemaVersion = "0.84.0"
val androidAttribute = Attribute.of("net.devrieze.android", Boolean::class.javaObjectType)

val serializationVersion: String = libs.versions.kotlinx.serialization.get()
val kotlin_version: String = libs.versions.kotlin.get()
val jupiterVersion: String = libs.versions.junit5.jupiter.get()

base {
    archivesName.set("xmlschema")
    version = "0.84.0"
    description = "A simple library for serializing/deserializing xmlschema"
}

kotlin {
    targets {
        jvm {
            attributes {
                attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, envJvm)
                attribute(androidAttribute, false)
            }
            compilations.all {
                compileKotlinTaskProvider.configure {
                    kotlinOptions {
                        jvmTarget = "1.8"
                    }
                }
            }
        }
        jvm("android") {
            attributes {
                attribute(androidAttribute, true)
                attribute(TARGET_JVM_ENVIRONMENT_ATTRIBUTE, envAndroid)
                attribute(KotlinPlatformType.attribute, KotlinPlatformType.androidJvm)
            }
            compilations.all {
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }
        }
        js(BOTH) {
            browser()
            nodejs()
            compilations.all {
                kotlinOptions {
                    sourceMap = true
                    sourceMapEmbedSources = "always"
                    suppressWarnings = false
                    verbose = true
                    metaInfo = true
                    moduleKind = "umd"
                    main = "call"
                }
            }
        }

    }
    targets.all {
        if (this is org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget) {
            testRuns.all {
                executionTask.configure {
                    useJUnitPlatform()
                }
            }
        }
        mavenPublication {
            version = schemaVersion
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                api(project(":serialization"))
                implementation(libs.serialization.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.serialization.json)
            }
        }
        val javaShared by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(javaShared)
            dependencies {
                implementation(kotlin("stdlib-jdk8", kotlin_version))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit5"))
                implementation(libs.junit5.api)

                implementation(kotlin("test-junit5"))

                runtimeOnly(libs.junit5.engine)
                runtimeOnly(libs.woodstox)
            }
        }
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}

tasks.create<Task>("test") {
    dependsOn(tasks.named("jvmTest"))
    group="verification"
}

addNativeTargets()

apiValidation {
    ignoredPackages.apply {
//        add("nl.adaptivity.serialutil.impl")
    }

}

doPublish()

configureDokka(myModuleVersion = schemaVersion)

idea {
    module {
        name = "xmlutil-serialutil"
    }
}
