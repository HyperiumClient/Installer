/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import kfoenix.jfxbutton
import kfoenix.jfxtextarea
import kotlinx.coroutines.launch
import tornadofx.*

class AgreementStage : View() {
    private val policy = """
        What data does Hyperium Collect?
        
        When using Hyperium, the client and integrated mods send a few small pieces of information to a remote server in exchange for a temporary token for accessing data from Hypixel. This information includes your Minecraft UUID and username, Minecraft Version, Client Version and specific mod being used. This information is processed and used to determine if the client is out of date or if the client should abort the startup procedure. Hyperium may also collect the servers you play on and the duration of time spent on servers.
        
        
        What we do with the data
        
        The data is stored securely for analytic purposes. We will never sell or release the data collected from specific users. All analytic graphs may be viewed on sk1er.club/graphs/hyperium 
    """.trimIndent()

    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Privacy Policy") { addClass(InstallerStyles.title) }
        label("By continuing you agree to our Privacy Policy") {
            addClass(InstallerStyles.desc)
        }
        pane { addClass(InstallerStyles.spacer) }

        jfxtextarea(policy) { isEditable = false }

        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("DISAGREE") {
            addClass(InstallerStyles.longButton)
            action {
                close()
            }
        }
        jfxbutton("AGREE AND CONTINUE") {
            addClass(InstallerStyles.longButton)
            action {
                find<InstallerView> { start() }
            }
        }
    }

    private fun start() {
        val view = find<InstallerView>()
        view.root.selectionModel.selectNext()
        Installer.launch {
            val progressStage = find<ProgressStage>()
            val success = Installer.install { runLater { progressStage.status = it } }
            if (success)
                runLater { view.root.selectionModel.selectNext() }
        }
    }
}