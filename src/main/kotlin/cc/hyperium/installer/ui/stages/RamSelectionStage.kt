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
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import com.sun.management.OperatingSystemMXBean
import javafx.scene.text.TextAlignment
import kfoenix.jfxbutton
import kfoenix.jfxslider
import tornadofx.*
import java.lang.management.ManagementFactory

class RamSelectionStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Select allocated ram") { addClass(InstallerStyles.title) }
        val av =
            runCatching { (ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean).totalPhysicalMemorySize / 1000000000L }
                .getOrNull() ?: 0
        label(
            "Please note that you should leave atleast 2GB for your system. You have ${av}GB.\n" +
                    "This option is only enabled in advanced mode."
        ) {
            textAlignment = TextAlignment.CENTER
            addClass(InstallerStyles.desc)
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxslider {
            valueProperty().bindBidirectional(JFXConfig.ramProperty)
            enableWhen(JFXConfig.advancedProperty)
            min = 1.0
            max = 4.0
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                find<InstallerView> { tabPane.selectionModel.selectNext() }
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