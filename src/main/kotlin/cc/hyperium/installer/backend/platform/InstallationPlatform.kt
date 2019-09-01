/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend.platform

interface InstallationPlatform {
    fun runChecks(callback: (String) -> Unit): Boolean

    fun install(lib: ByteArray)

    fun installProfile()

    fun getOptiFineVersion(): String?
}