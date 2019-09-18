/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.shared.utils

import cc.hyperium.installer.shared.entities.addon.AddonsManifest
import com.google.gson.Gson
import java.net.URL

object VersionUtils {
    private val gson = Gson()

    init {
        System.setProperty("http.agent", "Mozilla/5.0 (Hyperium Installer)")
    }

    fun isLocal() = javaClass.getResource("/assets/client.bin") != null

    fun getAddonsManifest() = gson.fromJson(
        get("https://raw.githubusercontent.com/HyperiumClient/Hyperium-Repo/master/installer/addons.json"),
        AddonsManifest::class.java
    )

    fun get(url: String) = URL(url).readText()
}