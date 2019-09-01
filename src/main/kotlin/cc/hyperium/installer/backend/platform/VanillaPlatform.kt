/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend.platform

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.backend.entities.Arguments
import cc.hyperium.installer.backend.entities.Library
import cc.hyperium.installer.backend.entities.Version
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class VanillaPlatform : InstallationPlatform {
    override fun install() {
        val gson = Gson()
        val time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .format(Date())
        val libraries = listOf(
            Library("cc.hyperium:Hyperium:")
        )
        val dir = File(Installer.config.path, "versions/Hyperium 1.8.9").apply { mkdirs() }
        val version = Version(
            "1.8.9-Hyperium",
            "release",
            "1.8.9",
            "1.8.9",
            time,
            time,
            0,
            "net.minecraft.launchwrapper.Launch",
            Arguments(listOf("--tweakClass", "cc.hyperium.launch.HyperiumTweaker")),
            libraries
        )

    }

    override fun installProfile() {

    }
}