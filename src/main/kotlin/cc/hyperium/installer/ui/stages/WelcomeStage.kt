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

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.config.JFXConfig
import cc.hyperium.installer.ui.ConfirmationDialog
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import javafx.scene.shape.Line
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import kfoenix.jfxprogressbar
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
                    if (JFXConfig.advanced)
                        tabPane.selectionModel.selectNext()
                    else tabPane.selectionModel.select(4)
                }
            }
        }
        jfxcheckbox("Advanced Mode") {
            var confirmed = false
            selectedProperty().addListener { _, _, n ->
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
            selectedProperty().bindBidirectional(JFXConfig.advancedProperty)
            addClass(InstallerStyles.desc)
        }
    }
}