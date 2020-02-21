package cc.hyperium.installer.backend.platform

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.backend.config.Config
import cc.hyperium.installer.shared.entities.addon.Addon
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import java.util.zip.ZipFile

class MultiMCPlatform(private val config: Config) : InstallationPlatform {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    override fun runChecks(callback: (String) -> Unit) = true

    override fun install(lib: ByteArray) {
        val instanceDir = File(config.path, "instances/Hyperium 1.8.9")
        instanceDir.mkdirs()

        val libDir = File(instanceDir, "libraries")
        libDir.mkdirs()
        val libFile = File(libDir, "Hyperium.jar")
        libFile.writeBytes(lib)

        val iconsDir = File(config.path, "icons")
        iconsDir.mkdirs()
        val iconFile = File(iconsDir, "hyperium.png")
        iconFile.writeBytes(javaClass.getResourceAsStream("/assets/logo_solid.png").use { it.readBytes() })

        val instanceCfgFile = File(instanceDir, "instance.cfg")
        val props = Properties()
        props["name"] = "Hyperium"
        props["iconKey"] = "hyperium"
        props["MaxMemAlloc"] = (config.ram.toInt() * 1024).toString()
        props["InstanceType"] = "OneSix"
        props["OverrideMemory"] = "true"
        props.store(instanceCfgFile.outputStream(), "Hyperium instance settings")

        val version = config.version.toString()

        val json =
            javaClass.getResourceAsStream("/assets/mmc/base.mmc-pack.json").bufferedReader().use { it.readText() }
        val baseData = gson.fromJson(json, JsonObject::class.java)
        val baseComponentArray = baseData.getAsJsonArray("components")

        if (config.optifine) {
            val optifineComponent = JsonObject()
            optifineComponent.addProperty("important", true)
            optifineComponent.addProperty("uid", "optifine.OptiFine")
            optifineComponent.addProperty("version", "OptiFine")
            baseComponentArray.add(optifineComponent)
        }

        val hyperiumComponent = JsonObject()
        hyperiumComponent.addProperty("important", true)
        hyperiumComponent.addProperty("uid", "cc.hyperium")
        hyperiumComponent.addProperty("version", version)

        baseComponentArray.add(hyperiumComponent)

        File(instanceDir, "mmc-pack.json").writeText(gson.toJson(baseData))

        instanceDir.installPatch("cc.hyperium", version) {
            val librariesArray = it["libraries"].asJsonArray
            var librariesBase = JsonArray()
            ZipFile(libFile).use { zip ->
                val entry = zip.getEntry("libraries.base.json")
                if (entry != null) {
                    librariesBase =
                        JsonParser.parseString(zip.getInputStream(entry).bufferedReader().use { it.readText() })
                            .asJsonArray
                }
            }
            for (element in librariesBase) {
                val obj = element.asJsonObject
                val mmcObj = JsonObject()
                mmcObj.add("name", obj["name"])
                val artifact = obj["downloads"]?.asJsonObject?.getAsJsonObject("artifact")
                if (artifact != null) {
                    mmcObj.add("MMC-absoluteUrl", artifact["url"])
                }
                librariesArray.add(mmcObj)
            }
            it.add("libraries", librariesArray)
        }
        if (config.optifine) {
            instanceDir.installPatch("optifine.OptiFine", "OptiFine")
        }
    }

    private fun File.installPatch(name: String, version: String, addData: (JsonObject) -> Unit = {}) {
        val json = javaClass.getResourceAsStream("/assets/mmc/base.$name.json").bufferedReader().use { it.readText() }
        val baseData = gson.fromJson(json, JsonObject::class.java)
        baseData.addProperty("version", version)
        addData(baseData)
        val patchDir = File(this, "patches")
        patchDir.mkdir()
        File(patchDir, "$name.json").writeText(gson.toJson(baseData))
    }

    override fun installProfile() {
    }

    override fun installAddons(addons: Map<Addon, ByteArray>) {
        val addonsDir = File(config.path, "instances/Hyperium 1.8.9/.minecraft/addons").apply { mkdirs() }
        addons.forEach { (addon, bytes) -> File(addonsDir, "${addon.name}.jar").writeBytes(bytes) }
    }

    override fun installOptiFine(file: File) {
        Installer.launch {
            val librariesDir = File(config.path, "instances/Hyperium 1.8.9/libraries")
            librariesDir.mkdirs()
            val optifine = File(librariesDir, "OptiFine.jar")
            file.copyTo(optifine, true)
        }
    }

    override fun deleteInstall() {
        File(config.path, "instances/Hyperium 1.8.9").deleteRecursively()
    }
}