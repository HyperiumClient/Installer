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