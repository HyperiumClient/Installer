/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.ui.ConfirmationDialog
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import tornadofx.*

class WelcomeStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)

        val img = resources.imageview("/assets/logo.png", true)
            .apply {
                fitWidth = 200.0
                fitHeight = 200.0
            }
        add(img)
        label("Hyperium Installer") { addClass(InstallerStyles.title) }
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                find<InstallerView> {
                    if (Installer.config.advanced)
                        root.selectionModel.selectNext()
                    else root.selectionModel.select(3)
                }
            }
        }
        jfxcheckbox("Advanced mode") {
            var confirmed = false
            selectedProperty().addListener { _, o, n ->
                if (!confirmed && n) {
                    isSelected = false
                    ConfirmationDialog(
                        "Would you like to continue?",
                        "You should only enable this option if you know what you're doing, would you like to continue?"
                    ) {
                        confirmed = true
                        isSelected = true
                    }.openModal()
                }
            }
            selectedProperty().bindBidirectional(Installer.config.advancedProperty)
            addClass(InstallerStyles.desc)
        }
    }
}