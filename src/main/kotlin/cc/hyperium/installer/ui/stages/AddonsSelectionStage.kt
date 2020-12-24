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
import cc.hyperium.installer.shared.entities.addon.Addon
import cc.hyperium.installer.shared.utils.InstallTarget
import cc.hyperium.installer.shared.utils.MinecraftUtils
import cc.hyperium.installer.shared.utils.VersionUtils
import cc.hyperium.installer.ui.ConfirmationDialog
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import com.jfoenix.controls.JFXCheckBox
import javafx.beans.binding.BooleanBinding
import javafx.scene.control.Tooltip
import javafx.stage.FileChooser
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tornadofx.*
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLEncoder

class AddonsSelectionStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Addons") { addClass(InstallerStyles.title) }
        val desc = label("Please note that OptiFine requires it to be installed in the launcher.") {
            addClass(InstallerStyles.desc)
        }
        pane { addClass(InstallerStyles.spacer) }
        vbox {
            jfxcheckbox("OptiFine") {
                selectedProperty().bindBidirectional(JFXConfig.optifineProperty)
                tooltip = Tooltip("A Minecraft optimization mod.")
            }
            Installer.launch {
                val addons = VersionUtils.addonsManifest?.addons
                val checkboxes = mutableMapOf<Addon, JFXCheckBox>()
                runLater {
                    if (addons != null) {
                        addons.forEach {
                            jfxcheckbox(it.name) {
                                JFXConfig.addonsProperties[it.name] = selectedProperty()
                                tooltip = Tooltip("${it.description}\nAuthor(s): ${it.author}")
                                checkboxes[it] = this
                            }
                        }
                        checkboxes.forEach { (addon, checkbox) ->
                            checkbox.enableWhen { bindDependency(addon, checkboxes).and(addon.enabled) }
                        }
                    } else desc.text = "Failed to fetch addons manifest, please check your internet connection."
                }
            }
            maxWidth = 100.0
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                if (JFXConfig.optifine) {
                    find<InstallerView> { tabPane.selectionModel.select(6) }
                } else find<InstallerView> { tabPane.selectionModel.select(7) }
            }
        }

        jfxbutton("Back") {
            addClass(InstallerStyles.backButton)
            action {
                if (JFXConfig.advanced) find<InstallerView> { tabPane.selectionModel.select(4) }
                else {
                    find<InstallerView> { tabPane.selectionModel.select(0) }
                }

            }
        }
    }

    fun bindDependency(addon: Addon, checkboxes: Map<Addon, JFXCheckBox>) =
        addon.depends.mapNotNull { dep -> checkboxes.entries.find { it.key.name == dep } }.let {
            it.firstOrNull()?.let { f ->
                it.fold(BooleanBinding.booleanExpression(f.value.selectedProperty())) { prop, checkbox ->
                    prop.and(checkbox.value.selectedProperty())
                }
            } ?: true.toProperty()
        }
}