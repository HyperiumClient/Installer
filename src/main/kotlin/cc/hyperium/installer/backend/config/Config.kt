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

package cc.hyperium.installer.backend.config

import cc.hyperium.installer.shared.entities.version.Version

interface Config {
    val ram: Number
    val path: String
    val optifine: Boolean
    val optifinePath: String
    val version: Version
    val addons: Map<String, Boolean>
    val cli: Boolean
    val cleanInstall: Boolean
}