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

package cc.hyperium.installer.backend.platform

import cc.hyperium.installer.backend.config.Config
import cc.hyperium.installer.shared.entities.addon.Addon
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.io.File
import java.time.Instant

class VanillaPlatform(private val config: Config) : InstallationPlatform {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    override fun runChecks(callback: (String) -> Unit): Boolean {
        if (config.optifine && getOptiFineVersion() == null) {
            callback("OptiFine not found. Please install OptiFine before installing Hyperium")
            return false
        } else if (!File(config.path, "versions/1.8.9").exists()) {
            callback("1.8.9 not found. Please run 1.8.9 atleast once before installing Hyperium")
            return false
        }
        return true
    }

    override fun install(lib: ByteArray) {
        val librariesDir = File(config.path, "libraries/cc/hyperium/Hyperium/LOCAL").apply { mkdirs() }
        File(librariesDir, "Hyperium-LOCAL.jar").writeBytes(lib)

        val dir = File(config.path, "versions/Hyperium 1.8.9").apply { mkdirs() }
        val base = javaClass.getResourceAsStream("/assets/base.json").bufferedReader().readText()
        val json = gson.fromJson(base, JsonObject::class.java)
        if (config.optifine) {
            val library = JsonObject()
            library.addProperty("name", "optifine:OptiFine:${getOptiFineVersion()}")
            json.getAsJsonArray("libraries").add(library)
        }
        File(dir, "Hyperium 1.8.9.json").writeText(gson.toJson(json))
    }

    override fun installProfile() {
        val profilesFile = File(config.path, "launcher_profiles.json")
        val json = gson.fromJson(profilesFile.readText(), JsonObject::class.java)
        val profiles = json.getAsJsonObject("profiles")
        val profile = JsonObject()
        val instant = Instant.ofEpochMilli(System.currentTimeMillis())
        profile.addProperty("name", "Hyperium 1.8.9")
        profile.addProperty("lastVersionId", "Hyperium 1.8.9")
        profile.addProperty("type", "custom")
        profile.addProperty("created", instant.toString())
        profile.addProperty("lastUsed", instant.toString())
        profile.addProperty(
            "javaArgs",
            "-Xmx${config.ram}G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Duser.country=US -Duser.language=en"
        )
        profiles.add("Hyperium", profile)
        json.addProperty("selectedProfile", "Hyperium")
        profilesFile.writeText(gson.toJson(json))
    }

    override fun installAddons(addons: Map<Addon, ByteArray>) {
        val addonsDir = File(config.path, "addons").apply { mkdir() }
        addons.forEach { (addon, bytes) -> File(addonsDir, "${addon.name}.jar").writeBytes(bytes) }
    }

    override fun getOptiFineVersion() = File(config.path, "versions")
        .listFiles { _, name -> name.startsWith("1.8.9-OptiFine_") }
        ?.maxBy { it.lastModified() }
        ?.name
        ?.replace("-OptiFine", "")
}