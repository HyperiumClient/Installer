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
import java.net.URL

object Installer : CoroutineScope {
    override val coroutineContext = Dispatchers.Default + Job()

    private val logger = LoggerFactory.getLogger("Installer")
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

            callback("Starting installation...")
            callback("Fetching jar...")
            val jar = fetchHyperium()
            callback("Installing...")
            plat.install(jar)
            callback("Installing profile...")
            plat.installProfile()
            callback("Downloading addons...")
            val addons = fetchAddons()
            callback("Installing addons...")
            plat.installAddons(addons)
            callback("Installation finished")
            return true
        } catch (t: Throwable) {
            callback("Error: $t")
            logger.error("An error occurred whilst installing", t)
        }
        return false
    }

    fun fetchAddons() = VersionUtils.addonsManifest.addons
        .filter { config.addons[it.name]?.value == true }
        .mapNotNull { runCatching { it.name to URL(it.url).readBytes() }.getOrNull() }
        .toMap()

    // TODO: Download latest beta from internet
    fun fetchHyperium() = javaClass.getResourceAsStream("/assets/client.bin").readBytes()

    fun getPlatform() = when (MinecraftUtils.detectTarget(config.path)) {
        InstallTarget.VANILLA -> VanillaPlatform()
        else -> null
    }
}