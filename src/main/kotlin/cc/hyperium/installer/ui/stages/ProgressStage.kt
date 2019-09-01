/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.ui.InstallerStyles
import tornadofx.*

class ProgressStage : View() {
    private val statusProperty = "Please wait".toProperty()
    var status: String by PropertyDelegate(statusProperty)

    override val root = vbox {
        addClass(InstallerStyles.container)

        label("Installing") { addClass(InstallerStyles.title) }
        label(statusProperty) {
            addClass(InstallerStyles.desc)
        }
    }
}