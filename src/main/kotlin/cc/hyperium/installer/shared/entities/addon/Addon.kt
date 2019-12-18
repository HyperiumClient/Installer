/*
 * Copyright © 2019 by Sk1er LLC
 *
 * All rights reserved.
 *
 * Sk1er LLC
 * 444 S Fulton Ave
 * Mount Vernon, NY
 * sk1er.club
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
    val depends: List<String>,
    val enabled: Boolean
)