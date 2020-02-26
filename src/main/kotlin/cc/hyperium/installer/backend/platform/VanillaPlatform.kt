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

package cc.hyperium.installer.backend.platform

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.backend.config.Config
import cc.hyperium.installer.backend.util.MessageException
import cc.hyperium.installer.shared.entities.addon.Addon
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.time.Instant
import java.util.*
import java.util.zip.ZipFile

class VanillaPlatform(private val config: Config) : InstallationPlatform {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    override fun runChecks(callback: (String) -> Unit) = true

    override fun install(lib: ByteArray) {
        val librariesDir = File(config.path, "libraries/cc/hyperium/Hyperium/LOCAL").apply { mkdirs() }
        val file = File(librariesDir, "Hyperium-LOCAL.jar")
        file.writeBytes(lib)
        var librariesArray = JsonArray()
        ZipFile(file).use { it ->
            val entry = it.getEntry("libraries.base.json")
            if (entry != null) {
                val stream = it.getInputStream(entry).bufferedReader()
                librariesArray = JsonParser.parseString(stream.use { it.readText() }).asJsonArray
            }
        }

        val dir = File(config.path, "versions/Hyperium 1.8.9").apply { mkdirs() }
        val base = javaClass.getResourceAsStream("/assets/base.json").bufferedReader().readText()
        val json = gson.fromJson(base, JsonObject::class.java)
        if (config.optifine) {
            Installer.logger.info("Adding OptiFine to libraries")
            val library = JsonObject()
            library.addProperty("name", "cc.hyperium:OptiFine:LOCAL")
            json.getAsJsonArray("libraries").add(library)
            json.addProperty(
                "minecraftArguments",
                json["minecraftArguments"].asString + " --tweakClass optifine.OptiFineForgeTweaker"
            )
        }
        json.addProperty(
            "minecraftArguments",
            json["minecraftArguments"].asString + " --tweakClass cc.hyperium.launch.HyperiumTweaker"
        )
        val librariesArrayOriginal = json.getAsJsonArray("libraries")
        for (element in librariesArray) {
            librariesArrayOriginal.add(element)
        }
        File(dir, "Hyperium 1.8.9.json").writeText(gson.toJson(json))
    }

    override fun installProfile() {
        val profilesFile = File(config.path, "launcher_profiles.json")
        val json = gson.fromJson(profilesFile.readText(), JsonObject::class.java)
        val profiles = json.getAsJsonObject("profiles")
            ?: throw MessageException("Please run Minecraft at least once before installing Hyperium.")
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
        profile.addProperty(
            "icon",
            "data:image/png;base64," +
                    Base64.getEncoder().encodeToString(
                        javaClass.getResourceAsStream("/assets/logo.png")
                            .use { it.readBytes() }
                    )
        )
        profiles.add("Hyperium", profile)
        json.addProperty("selectedProfile", "Hyperium")
        profilesFile.writeText(gson.toJson(json))
    }

    override fun installAddons(addons: Map<Addon, ByteArray>) {
        val addonsDir = File(config.path, "addons").apply { mkdir() }
        addons.forEach { (addon, bytes) -> File(addonsDir, "${addon.name}.jar").writeBytes(bytes) }
    }

    override fun installOptiFine(file: File) {
        try {
            val dir = File(config.path, "/libraries/cc/hyperium/OptiFine/LOCAL")
            dir.mkdirs()
            val jar = File(dir, "OptiFine-LOCAL.jar")
            file.copyTo(jar, true)
        } catch (e: Exception) {
            Installer.logger.error("Failed installing OptiFine", e)
            error("Failed to install OptiFine. Are you sure Minecraft is closed?")
        }
    }

    override fun deleteInstall() {
        val hyperiumConfigFolder = File("${config.path}/hyperium")
        val hyperiumAddonsFolder = File("${config.path}/addons")
        val hyperiumLibariesFolder = File("${config.path}/libraries/cc/hyperium")
        val hyperiumVersionFolder = File("${config.path}/versions/Hyperium 1.8.9")
        hyperiumConfigFolder.deleteRecursively()
        hyperiumAddonsFolder.deleteRecursively()
        hyperiumLibariesFolder.deleteRecursively()
        hyperiumVersionFolder.deleteRecursively()
    }
}