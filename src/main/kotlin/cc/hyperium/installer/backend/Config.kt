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

import cc.hyperium.installer.shared.entities.version.Version
import cc.hyperium.installer.shared.utils.MinecraftUtils
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.PropertyDelegate

class Config {
    val advancedProperty = SimpleBooleanProperty(false)
    val ramProperty = SimpleIntegerProperty(2)
    val pathProperty = SimpleObjectProperty(MinecraftUtils.getMinecraftDir().canonicalPath)
    val optifineProperty = SimpleBooleanProperty(false)
    val versionProperty = SimpleObjectProperty<Version>()

    var advanced by PropertyDelegate(advancedProperty)
    var ram by PropertyDelegate(ramProperty)
    var path by PropertyDelegate(pathProperty)
    val optifine by PropertyDelegate(optifineProperty)
    val addons = mutableMapOf<String, BooleanProperty>()
    var version by PropertyDelegate(versionProperty)
}