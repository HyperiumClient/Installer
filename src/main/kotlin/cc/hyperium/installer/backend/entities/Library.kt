/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend.entities

data class Library(
    val name: String,
    val url: String? = null,
    val downloads: Downloads? = null
)