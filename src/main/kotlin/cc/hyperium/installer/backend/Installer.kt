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

package cc.hyperium.installer.backend

import cc.hyperium.installer.backend.platform.VanillaPlatform
import cc.hyperium.installer.shared.utils.InstallTarget
import cc.hyperium.installer.shared.utils.MinecraftUtils
import cc.hyperium.installer.shared.utils.VersionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.net.URL
import java.security.MessageDigest
import java.util.*

object Installer : CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()

    private val logger = LoggerFactory.getLogger("Installer")
    private val sha256 = MessageDigest.getInstance("SHA-256")
    val config = Config()

    /**
     * Blocking function to install
     * @return if success
     */
    fun install(callback: (String) -> Unit): Boolean {
        callback("Running pre-checks")
        try {
            val plat = getPlatform()
            if (plat == null) {
                callback("Unable to detect the platform, please try again")
                return false
            }
            if (!plat.runChecks(callback)) return false

            logger.info("Starting installation")
            callback("Starting installation...")
            val bytes = if (VersionUtils.isLocal()) {
                logger.info("Fetching hyperium")
                callback("Fetching Hyperium...")
                fetchHyperium()
            } else {
                callback("Downloading Hyperium...")
                downloadHyperium()
            }
            logger.info("Installing")
            callback("Installing...")
            plat.install(bytes)
            logger.info("Installing profile")
            callback("Installing profile...")
            plat.installProfile()
            logger.info("Downloading addons")
            callback("Downloading addons...")
            val addons = fetchAddons()
            logger.info("Installing addons")
            callback("Installing addons...")
            plat.installAddons(addons)
            logger.info("Installation finished")
            callback("Installation finished")
            return true
        } catch (t: Throwable) {
            callback("Error: $t")
            logger.error("An error occurred whilst installing", t)
        }
        return false
    }

    fun fetchAddons() = VersionUtils.addonsManifest?.addons
        ?.filter { config.addons[it.name]?.value == true }
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

    fun downloadHyperium(): ByteArray {
        val ver = config.version ?: throw IllegalStateException("Failed to fetch version manifest")
        logger.info("Downloading Hyperium v${ver.build} b${ver.id} beta: ${ver.beta}")
        val bytes = URL(ver.url).readBytes()
        logger.info("Verifying integrity")
        if (toHex(sha256.digest(bytes)) != ver.sha256.toLowerCase())
            throw SecurityException("Integrity check failed")
        return bytes
    }

    fun fetchHyperium() = javaClass.getResourceAsStream("/assets/client.bin").readBytes()

    fun getPlatform() = when (MinecraftUtils.detectTarget(config.path)) {
        InstallTarget.VANILLA -> VanillaPlatform()
        else -> null
    }

    private fun toHex(bytes: ByteArray) = buildString { bytes.forEach { append("%02x".format(it)) } }
}