/*
 * Copyright © 2020 by Sk1er LLC
 *
 * All rights reserved.
 *
 * Sk1er LLC
 * 444 S Fulton Ave
 * Mount Vernon, NY
 * sk1er.club
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    application
}

group = "cc.hyperium.installer"
version = "1.5.4"

application {
    mainClassName = "cc.hyperium.installer.HyperiumInstallerKt"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")
    implementation("com.github.bkenn:kfoenix:0.1.3")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("org.ow2.asm:asm:7.3.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}