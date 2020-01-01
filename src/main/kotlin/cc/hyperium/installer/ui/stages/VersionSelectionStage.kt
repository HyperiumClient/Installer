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
import cc.hyperium.installer.shared.entities.version.Version
import cc.hyperium.installer.shared.utils.VersionUtils
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import javafx.scene.text.TextAlignment
import kfoenix.jfxbutton
import kfoenix.jfxcombobox
import kotlinx.coroutines.launch
import tornadofx.*

class VersionSelectionStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)

        label("Select Hyperium version") { addClass(InstallerStyles.title) }
        val desc = label(
            "We recommend using the latest Hyperium version\n" +
                    "Older versions are unsupported."
        ) {
            textAlignment = TextAlignment.CENTER
            addClass(InstallerStyles.desc)
        }

        pane { addClass(InstallerStyles.spacer) }

        jfxcombobox<Version> {
            JFXConfig.versionProperty.bind(selectionModel.selectedItemProperty())

            fun refresh() {
                Installer.launch {
                    val versions = VersionUtils.versionsManifest?.versions
                        ?.sortedByDescending { it.time }
                        ?.toMutableList()
                    if (versions != null) {
                        if (!JFXConfig.advanced)
                            versions.removeIf { it.beta }
                        runLater {
                            items.setAll(versions)
                            selectionModel.select(versions.first())
                        }
                    } else desc.text = "Failed to fetch versions manifest, please check your internet connection."
                }
            }

            refresh()
            JFXConfig.advancedProperty.onChange { refresh() }
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