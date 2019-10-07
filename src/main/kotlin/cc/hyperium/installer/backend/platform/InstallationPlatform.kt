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

interface InstallationPlatform {
    fun runChecks(callback: (String) -> Unit): Boolean

    fun install(lib: ByteArray)

    fun installProfile()

    fun installAddons(addons: Map<String, ByteArray>)

    fun getOptiFineVersion(): String?
}