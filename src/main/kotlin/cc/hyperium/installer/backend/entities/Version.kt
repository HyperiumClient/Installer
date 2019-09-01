/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend.entities

data class Version (
    val id: String,
    val type: String,
    val inheritsFrom: String,
    val jar: String,
    val time: String,
    val releaseTime: String,
    val minimumLauncherVersion: Int,
    val mainClass: String,
    val arguments: Arguments,
    val libraries: List<Library>
)