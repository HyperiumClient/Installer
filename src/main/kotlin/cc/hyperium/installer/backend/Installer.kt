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

package cc.hyperium.installer.backend

import cc.hyperium.installer.backend.config.Config
import cc.hyperium.installer.backend.config.JFXConfig.cleanInstall
import cc.hyperium.installer.backend.platform.VanillaPlatform
import cc.hyperium.installer.shared.utils.InstallTarget
import cc.hyperium.installer.shared.utils.MinecraftUtils
import cc.hyperium.installer.shared.utils.VersionUtils
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.security.MessageDigest

object Installer : CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()

    val logger = LoggerFactory.getLogger("Installer")
    private val sha256 = MessageDigest.getInstance("SHA-256")
    private val gson = Gson()

    /**
     * Blocking function to install
     * @return if success
     */
    fun install(config: Config, callback: (String) -> Unit) = async {
        logger.debug("Path: ${config.path}")
        logger.debug("Java home: ${System.getProperty("java.home")}")
        logger.debug("Java version: ${System.getProperty("java.version")}")
        callback("Running pre-checks")
        try {
            val plat = getPlatform(config)
            if (plat == null) {
                logger.error("Unable to detect platform, path: ${config.path}")
                callback("Unable to detect the platform, please try again")
                return@async false
            }
            if (!plat.runChecks(callback)) return@async false

            if (cleanInstall) {
                logger.info("Deleting hyperium")
                callback("Deleting Hyperium...")
                val hyperiumConfigFolder = File("${config.path}/hyperium")
                val hyperiumAddonsFolder = File("${config.path}/addons")
                val hyperiumLibariesFolder = File("${config.path}/libraries/cc/hyperium")
                val hyperiumVersionFolder = File("${config.path}/versions/Hyperium 1.8.9")
                hyperiumConfigFolder.deleteRecursively()
                hyperiumAddonsFolder.deleteRecursively()
                hyperiumLibariesFolder.deleteRecursively()
                hyperiumVersionFolder.deleteRecursively()
            }

            logger.info("Starting installation")
            callback("Starting installation...")
            val bytes = if (VersionUtils.isLocal()) {
                logger.info("Fetching hyperium")
                callback("Fetching Hyperium...")
                fetchHyperium()
            } else {
                callback("Downloading Hyperium...")
                downloadHyperium(config)
            }
            logger.info("Installing")
            callback("Installing...")
            plat.install(bytes)
            logger.info("Installing profile")
            callback("Installing profile...")
            plat.installProfile()
            logger.info("Downloading addons")
            callback("Downloading addons...")
            val addons = fetchAddons(config)
            logger.info("Installing addons")
            callback("Installing addons...")
            plat.installAddons(addons)
            logger.info("Installation finished")
            callback("Installation finished")
            return@async true
        } catch (t: Throwable) {
            callback("Error: $t")
            logger.error("An error occurred whilst installing", t)
        }
        return@async false
    }

    fun fetchAddons(config: Config) = VersionUtils.addonsManifest?.addons
        ?.filter { config.addons[it.name] == true }
        ?.mapNotNull {
            logger.info("Downloading addon: ${it.name}")
            runCatching { it to URL(it.url).readBytes() }.getOrNull()
        }
        ?.apply {
            forEach { (addon, bytes) ->
                logger.info("Verifying integrity of addon: ${addon.name}")
                if (toHex(sha256.digest(bytes)) != addon.sha256.toLowerCase())
                    throw SecurityException("Integrity check failed for addon: ${addon.name}")
            }
        }
        ?.toMap() ?: emptyMap()

    fun downloadHyperium(config: Config): ByteArray {
        val ver = config.version
        logger.info("Downloading Hyperium v${ver.build} b${ver.id} beta: ${ver.beta}")
        val bytes = URL(ver.url).readBytes()
        logger.info("Verifying integrity")
        if (toHex(sha256.digest(bytes)) != ver.sha256.toLowerCase())
            throw SecurityException("Integrity check failed")
        return bytes
    }

    fun fetchHyperium() = javaClass.getResourceAsStream("/assets/client.bin").readBytes()

    fun getPlatform(config: Config) = when (MinecraftUtils.detectTarget(config.path)) {
        InstallTarget.VANILLA -> VanillaPlatform(config)
        else -> null
    }

    private fun toHex(bytes: ByteArray) = buildString { bytes.forEach { append("%02x".format(it)) } }
}