/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend.platform

interface InstallationPlatform {
    fun install()

    fun installProfile()
}