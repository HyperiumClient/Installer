/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui

import cc.hyperium.installer.ui.stages.AddonsSelectionStage
import cc.hyperium.installer.ui.stages.*
import javafx.scene.control.TabPane
import javafx.scene.input.KeyCode
import kfoenix.jfxtabpane
import tornadofx.*

class InstallerView : View("Hyperium Installer") {
    val tabPane = jfxtabpane {
        tab(find<WelcomeStage>())
        tab(find<TargetSelectionStage>())
        tab(find<RamSelectionStage>())
        tab(find<AddonsSelectionStage>())
        add(find<AgreementStage>())
        tab(find<ProgressStage>())
        tab(find<FinishedStage>())

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        setOnKeyPressed { if ((it.code == KeyCode.TAB && it.isControlDown) || it.code == KeyCode.LEFT || it.code == KeyCode.RIGHT) it.consume() }

        prefWidth = 854.0
        prefHeight = 480.0
    }
    override val root = tabPane

    init {
        icon = resources.imageview("/assets/logo_solid.png", true)
    }
}