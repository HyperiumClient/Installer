/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.ui.InstallerStyles
import tornadofx.View
import tornadofx.addClass
import tornadofx.label
import tornadofx.vbox

class ProgressStage : View() {
    override val root = vbox {
        label("Installing...") { addClass(InstallerStyles.title) }
        label("Please wait") {
            addClass(InstallerStyles.desc)
        }
    }
}