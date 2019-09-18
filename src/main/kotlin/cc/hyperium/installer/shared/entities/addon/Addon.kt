/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.shared.entities.addon

data class Addon(
    val name: String,
    val description: String,
    val version: String,
    val author: String,
    val verified: Boolean,
    val sha256: String,
    val url: String,
    val depends: List<String>
)