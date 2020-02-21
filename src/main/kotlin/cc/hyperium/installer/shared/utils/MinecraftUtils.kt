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

package cc.hyperium.installer.shared.utils

import java.io.File

object MinecraftUtils {
    private val os = System.getProperty("os.name").toLowerCase()

    fun getMinecraftDir() = when {
        os.contains("window") -> File(System.getenv("APPDATA"), ".minecraft")
        os.contains("mac") -> File(System.getProperty("user.home"), "/Library/Application Support/minecraft")
        else -> File(System.getProperty("user.home"), ".minecraft")
    }

    fun detectTarget(s: String): InstallTarget? {
        val f = File(s)
        if (!f.exists()) return null
        return when {
            File(f, "launcher_profiles.json").exists() -> InstallTarget.VANILLA
            File(f, "multimc.cfg").exists() -> InstallTarget.MULTIMC
            else -> null
        }
    }
}