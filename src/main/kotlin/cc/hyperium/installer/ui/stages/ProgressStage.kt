/*
 * Copyright © 2019 by Sk1er LLC
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