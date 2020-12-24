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

package cc.hyperium.installer.ui

import cc.hyperium.installer.ui.stages.*
import javafx.scene.control.TabPane
import javafx.scene.input.KeyCode
import kfoenix.jfxtabpane
import tornadofx.View
import tornadofx.tab

class InstallerView : View("Hyperium Installer") {
    val tabPane = jfxtabpane {
        tab(find<WelcomeStage>()) // 0
        tab(find<WarningStage>()) // 1
        tab(find<TargetSelectionStage>()) // 2
        tab(find<RamSelectionStage>()) // 3
        tab(find<VersionSelectionStage>()) // 4
        tab(find<AddonsSelectionStage>()) // 5
        tab(find<OptiFineStage>()) // 6
        add(find<AgreementStage>()) // 7
        tab(find<ProgressStage>()) // 8
        tab(find<FinishedStage>()) // 9

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        setOnKeyPressed {
            if ((it.code == KeyCode.TAB && it.isControlDown) || it.code == KeyCode.LEFT || it.code == KeyCode.RIGHT || it.code == KeyCode.UP || it.code == KeyCode.DOWN)
                it.consume()
        }

        prefWidth = 854.0
        prefHeight = 480.0
    }
    override val root = tabPane

    init {
        icon = resources.imageview("/assets/logo.png", true)
    }
}