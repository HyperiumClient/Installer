/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import cc.hyperium.installer.utils.InstallerTarget
import cc.hyperium.installer.utils.MinecraftUtils
import com.jfoenix.controls.JFXButton
import kfoenix.jfxbutton
import kfoenix.jfxtextfield
import tornadofx.*

class TargetSelectionStage : View() {
    private var target by PropertyDelegate(InstallerTarget.VANILLA.toProperty())

    override val root = vbox {
        addClass(InstallerStyles.container)

        label("Select installation target") { addClass(InstallerStyles.title) }
        label("Please note that we do not support other third party launchers") { addClass(InstallerStyles.desc) }

        pane { addClass(InstallerStyles.spacer) }

        var multimc: JFXButton? = null
        val vanilla = jfxbutton("Vanilla Launcher") {
            action {
                if (target != InstallerTarget.VANILLA) {
                    target = InstallerTarget.VANILLA
                    Installer.config.path = MinecraftUtils.getMinecraftDir().canonicalPath
                    addClass(InstallerStyles.selectedButton)
                    multimc!!.removeClass(InstallerStyles.selectedButton)
                }
            }
            addClass(InstallerStyles.longButton)
            addClass(InstallerStyles.selectedButton)
        }
        multimc = jfxbutton("MultiMC") {
            action {
                if (target != InstallerTarget.MULTIMC) {
                    target = InstallerTarget.MULTIMC
                    addClass(InstallerStyles.selectedButton)
                    vanilla.removeClass(InstallerStyles.selectedButton)
                }
            }
            addClass(InstallerStyles.longButton)
        }
        jfxtextfield(Installer.config.pathProperty) {
            editableWhen(Installer.config.advancedProperty)
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                find<InstallerView> { root.selectionModel.selectNext() }
            }
        }
    }
}