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
import cc.hyperium.installer.shared.utils.MinecraftUtils
import cc.hyperium.installer.ui.ConfirmationDialog
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import javafx.scene.text.TextAlignment
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import kfoenix.jfxtextfield
import tornadofx.*

class TargetSelectionStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)

        label("Select installation target") { addClass(InstallerStyles.title) }
        label(
            "Please note that we only support vanilla launcher.\n" +
                    "This option is only enabled in advanced mode."
        ) {
            textAlignment = TextAlignment.CENTER
            addClass(InstallerStyles.desc)
        }

        pane { addClass(InstallerStyles.spacer) }

        jfxtextfield(JFXConfig.pathProperty) {
            editableWhen(JFXConfig.advancedProperty)
        }
        jfxcheckbox("Clean install") {
            var confirmed = false
            selectedProperty().addListener { _, _, n ->
                if (!confirmed && n) {
                    isSelected = false
                    ConfirmationDialog(
                        "Would you like to continue?",
                        "Enabling this option will reset all your Hyperium settings!"
                    ) {
                        confirmed = true
                        isSelected = true
                    }.openModal()
                }
            }
            selectedProperty().bindBidirectional(JFXConfig.cleanInstallProperty)
            addClass(InstallerStyles.desc)
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                if (MinecraftUtils.detectTarget(JFXConfig.path) == null) {
                    ConfirmationDialog(
                        "Would you like to continue?",
                        "Your minecraft path is invalid therefore the installer can't install it properly. Would you like to continue?"
                    ) { find<InstallerView> { tabPane.selectionModel.selectNext() } }.openModal()
                } else find<InstallerView> { tabPane.selectionModel.selectNext() }
            }
        }
        jfxbutton("Back") {
            addClass(InstallerStyles.backButton)
            action {
                find<InstallerView> { tabPane.selectionModel.selectPrevious() }
            }
        }
    }
}