/*
 * Copyright (c) 2018.
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

plugins {
//    kotlin("jvm") version libs.versions.kotlin
    `kotlin-dsl`
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
}

val kotlinVersion = libs.versions.kotlin.get()

dependencies {
//    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$embeddedKotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$embeddedKotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-native-utils:$embeddedKotlinVersion")
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:$embeddedKotlinVersion")
    runtimeOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    runtimeOnly("org.jetbrains.kotlin:kotlin-native-utils:$kotlinVersion")
    runtimeOnly("org.jetbrains.dokka:dokka-gradle-plugin:$kotlinVersion")
}

repositories {
    maven { url = file("mavenBundled").toURI() }
    mavenLocal()
    mavenCentral()
}
