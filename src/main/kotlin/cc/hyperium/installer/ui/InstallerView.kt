/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui

import cc.hyperium.installer.ui.stages.ConfigStage
import cc.hyperium.installer.ui.stages.WelcomeStage
import javafx.scene.control.TabPane
import javafx.scene.input.KeyCode
import kfoenix.jfxtabpane
import tornadofx.View
import tornadofx.tab

class InstallerView : View("Hyperium Installer") {
    override val root = jfxtabpane {
        tab(WelcomeStage())
        tab(ConfigStage())

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        setOnKeyPressed { if (it.code == KeyCode.TAB && it.isControlDown) it.consume() }

        prefWidth = 854.0
        prefHeight = 480.0
    }

    init {
        icon = resources.imageview("/assets/logo_solid.png", true)
    }
}