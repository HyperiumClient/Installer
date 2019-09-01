/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend

import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import tornadofx.*

class AddonsSelectionStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Addons") { addClass(InstallerStyles.title) }
        label("Please note that OptiFine requires it to be installed in the launcher.") {
            addClass(InstallerStyles.desc)
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxcheckbox("OptiFine") { selectedProperty().bindBidirectional(Installer.config.optifineProperty) }
        // TODO: Addons
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                find<InstallerView> { root.selectionModel.selectNext() }
            }
        }
    }
}