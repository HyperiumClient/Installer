/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.shared.entities.version

import com.google.gson.annotations.SerializedName

data class Version(
    val build: String,
    val id: Int,
    val url: String,
    val size: Long,
    @SerializedName("target_installer")
    val targetInstaller: Int,
    val sha1: String,
    val time: Long,
    val beta: Boolean,
    val sha256: String
)