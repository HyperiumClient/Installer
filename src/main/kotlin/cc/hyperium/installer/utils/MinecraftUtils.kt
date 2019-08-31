/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.utils

import java.io.File

object MinecraftUtils {
    private val os = System.getProperty("os.name").toLowerCase()

    fun getMinecraftDir() = when {
        os.contains("window") -> File(System.getenv("APPDATA"), ".minecraft")
        os.contains("mac") -> File(System.getProperty("user.home"), "/Library/Application Support/minecraft")
        else -> File(System.getProperty("user.home"), ".minecraft")
    }
}