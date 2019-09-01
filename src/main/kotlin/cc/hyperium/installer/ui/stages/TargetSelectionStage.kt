/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.ui.ConfirmationDialog
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import cc.hyperium.installer.utils.MinecraftUtils
import javafx.scene.text.TextAlignment
import kfoenix.jfxbutton
import kfoenix.jfxtextfield
import kotlinx.coroutines.launch
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

        jfxtextfield(Installer.config.pathProperty) {
            editableWhen(Installer.config.advancedProperty)
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                if (MinecraftUtils.detectTarget(Installer.config.path) == null) {
                    ConfirmationDialog(
                        "Would you like to continue?",
                        "Your minecraft path is invalid therefore the installer can't install it properly. Would you like to continue?"
                    ) { find<InstallerView> { root.selectionModel.selectNext() } }.openModal()
                } else find<InstallerView> { root.selectionModel.selectNext() }
            }
        }
    }
}