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

package cc.hyperium.installer.backend.config

import cc.hyperium.installer.shared.entities.version.Version
import javafx.beans.property.BooleanProperty

interface Config {
    var advanced: Boolean
    var ram: Number
    var path: String
    var optifine: Boolean
    var version: Version
    val addons: Map<String, BooleanProperty>
}