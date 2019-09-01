/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.ui.ConfirmationDialog
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import javafx.stage.FileChooser
import kfoenix.jfxbutton
import kfoenix.jfxcheckbox
import kotlinx.coroutines.launch
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLEncoder

class AddonsSelectionStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Addons") { addClass(InstallerStyles.title) }
        label("Please note that OptiFine requires it to be installed in the launcher.") {
            addClass(InstallerStyles.desc)
        }
        pane { addClass(InstallerStyles.spacer) }
        jfxcheckbox("OptiFine") { selectedProperty().bindBidirectional(Installer.config.optifineProperty) }
        // TODO: Addons
        pane { addClass(InstallerStyles.spacer) }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                next()
            }
        }
        jfxbutton("Back") {
            addClass(InstallerStyles.backButton)
            action {
                find<InstallerView> { tabPane.selectionModel.selectPrevious() }
            }
        }
    }

    fun next() {
        if (Installer.config.optifine) {
            val plat = Installer.getPlatform()
            if (plat?.getOptiFineVersion() == null) {
                ConfirmationDialog(
                    "OptiFine not found",
                    "We could not find OptiFine 1.8.9 installation. Would you like to download & install it?"
                ) {
                    Installer.launch {
                        val ver = URL("http://optifine.net/version/1.8.9/HD_U.txt").readText()
                        val fileName = "OptiFine_1.8.9_HD_U_$ver.jar"
                        Desktop.getDesktop()
                            .browse(URI("http://optifine.net/adloadx?f=${URLEncoder.encode(fileName)}"))
                        val dialog = ConfirmationDialog(
                            "OptiFine installation",
                            "We've opened the download page for you, please download the jar. Once you have finished, press OK to select the jar."
                        ) {
                            if (plat?.getOptiFineVersion() == null) {
                                val files = chooseFile(
                                    "Choose OptiFine jar",
                                    arrayOf(FileChooser.ExtensionFilter("JAR Files (*.jar)", "*.jar"))
                                ) {
                                    initialDirectory = File(System.getProperty("user.home"), "Downloads")
                                    initialFileName = fileName
                                }
                                if (files.isNotEmpty()) {
                                    Installer.launch {
                                        run(files.first())
                                        if (plat?.getOptiFineVersion() != null)
                                            runLater { find<InstallerView> { tabPane.selectionModel.selectNext() } }
                                    }
                                }
                            } else find<InstallerView> { tabPane.selectionModel.selectNext() }
                        }
                        runLater { dialog.openModal() }
                    }
                }.openModal()
            } else find<InstallerView> { tabPane.selectionModel.selectNext() }
        } else
            find<InstallerView> { tabPane.selectionModel.selectNext() }
    }

    fun run(jar: File) {
        val win = System.getProperty("os.name").toLowerCase().contains("win")
        val java = if (win) "java.exe" else "java"
        val bin = File(System.getProperty("java.home"), "bin/$java").canonicalPath
        ProcessBuilder(bin, "-jar", jar.canonicalPath)
            .inheritIO()
            .redirectErrorStream(true)
            .start()
            .waitFor()
    }
}