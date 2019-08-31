/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import javafx.scene.control.ToggleGroup
import kfoenix.jfxbutton
import kfoenix.jfxradiobutton
import tornadofx.*

class ConfigStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)

        label("Select installation target") { addClass(InstallerStyles.title) }
        val group = ToggleGroup()
        vbox {
            jfxradiobutton("Vanilla Launcher", group) { isSelected = true }
            jfxradiobutton("MultiMC", group)
            addClass(InstallerStyles.radioContainer)
        }

        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                find<InstallerView> { root.selectionModel.selectNext() }
            }
        }
    }
}