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

package cc.hyperium.installer

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.backend.config.CLIConfig
import cc.hyperium.installer.backend.config.JFXConfig
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import kotlinx.coroutines.runBlocking
import tornadofx.App
import tornadofx.launch
import javax.swing.JOptionPane
import javax.swing.UIManager

class InstallerApp : App(InstallerView::class, InstallerStyles::class)

fun main(args: Array<String>) {
    // Update to current date when changing version
    Installer.logger.info("Running Hyperium installer version 1.5.1/March 18th 2020")

    if (args.isEmpty() || (System.console() == null && JFXConfig.cli))
        try {
            launch<InstallerApp>()
        } catch (ex: NoClassDefFoundError) {
            ex.printStackTrace()
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
            } catch (_: Exception) {
            }
            JOptionPane.showMessageDialog(
                null,
                "Please make sure that JavaFX is installed on your system.",
                "Class not found",
                JOptionPane.WARNING_MESSAGE
            )
        }
    else mainBody("Hyperium Installer") {
        runBlocking {
            Installer.install(ArgParser(args).parseInto(::CLIConfig)) { /* Ignore callback because we're using logger */ }
                .await()
        }
    }
}