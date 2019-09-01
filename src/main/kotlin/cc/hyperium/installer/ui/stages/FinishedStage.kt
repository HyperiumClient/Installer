/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.ui.InstallerStyles
import kfoenix.jfxbutton
import tornadofx.*

class FinishedStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)

        label("Installation success!") { addClass(InstallerStyles.title) }
        label("You can now launch Hyperium from your Minecraft launcher.") { addClass(InstallerStyles.desc) }

        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("CLOSE") {
            addClass(InstallerStyles.longButton)
            action {
                close()
            }
        }
    }
}