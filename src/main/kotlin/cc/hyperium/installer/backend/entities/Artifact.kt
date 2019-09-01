/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend.entities

data class Artifact(
    val path: String,
    val sha1: String,
    val size: Long,
    val url: String
)