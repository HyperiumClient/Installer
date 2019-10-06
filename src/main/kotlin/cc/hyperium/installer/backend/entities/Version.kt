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