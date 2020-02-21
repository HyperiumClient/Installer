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

import cc.hyperium.installer.shared.entities.addon.Addon
import java.io.File

interface InstallationPlatform {
    fun runChecks(callback: (String) -> Unit): Boolean

    fun install(lib: ByteArray)

    fun installProfile()

    fun installAddons(addons: Map<Addon, ByteArray>)

    fun installOptiFine(file: File)

    fun deleteInstall()
}