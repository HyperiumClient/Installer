/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

import edu.sc.seis.launch4j.tasks.Launch4jLibraryTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("edu.sc.seis.launch4j") version "2.4.6"
    application
}

group = "cc.hyperium.installer"
version = "1.0"

application {
    mainClassName = "cc.hyperium.installer.HyperiumInstallerKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
    implementation("com.github.bkenn:kfoenix:0.1.3")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha4")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Launch4jLibraryTask> {
    jar = "$projectDir/build/libs/hyperium-installer-1.0-all.jar"
    bundledJrePath = "C:\\Program Files (x86)\\Minecraft Launcher\\runtime\\jre-x64"
    mainClassName = "cc.hyperium.installer.HyperiumInstallerKt"
}
