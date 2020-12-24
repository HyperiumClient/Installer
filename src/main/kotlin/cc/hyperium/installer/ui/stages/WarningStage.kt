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

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.backend.config.JFXConfig
import cc.hyperium.installer.backend.util.Desktop
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import kfoenix.jfxbutton
import kotlinx.coroutines.launch
import tornadofx.*
import java.net.URI

class WarningStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Hyperium isn't actively developed") { addClass(InstallerStyles.title) }
        label("You won't get any support with Hyperium anymore.") {
            addClass(
                InstallerStyles.desc
            )
        }
        label("Our forge mods are both better and actively developed.") {
            addClass(
                InstallerStyles.desc
            )
        }

        hyperlink("Click here to go to Sk1er.club") {
            addClass(
                InstallerStyles.desc
            )
        }.action {
            Installer.launch {
                val url = "https://sk1er.club/"
                Installer.logger.info("Attempting to open $url")
                if (!Desktop.browse(URI(url))) Installer.logger
                    .error("Failed to open $url. Please navigate to $url manually.")
            }
        }

        pane { addClass(InstallerStyles.spacer) }

        jfxbutton("EXIT") {
            addClass(InstallerStyles.longButton)
            action {
                close()
            }
        }

        pane { addClass(InstallerStyles.spacer) }

        jfxbutton("CONTINUE ANYWAYS") {
            addClass(InstallerStyles.backButton)
            action {
                find<InstallerView> {
                    if (JFXConfig.advanced)
                        tabPane.selectionModel.selectNext()
                    else tabPane.selectionModel.select(5)
                }
            }
        }
    }
}
