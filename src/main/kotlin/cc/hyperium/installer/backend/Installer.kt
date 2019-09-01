/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend

import org.slf4j.LoggerFactory

object Installer {
    private val logger = LoggerFactory.getLogger("Installer")
    val config = Config()

    fun install(callback: (String) -> Unit): Boolean {
        callback("Starting installation...")

        try {
            return true
        } catch (t: Throwable) {
            logger.error("An error occurred whilst installing", t)
        }
        return false
    }
}