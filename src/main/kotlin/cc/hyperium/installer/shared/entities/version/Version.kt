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
) {
    override fun toString() = if (beta) "$build b$id" else build
}