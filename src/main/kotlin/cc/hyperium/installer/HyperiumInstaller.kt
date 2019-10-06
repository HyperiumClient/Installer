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

package cc.hyperium.installer

import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import tornadofx.App
import tornadofx.launch
import java.io.File
import javax.swing.JOptionPane
import javax.swing.UIManager

class InstallerApp : App(InstallerView::class, InstallerStyles::class)

fun main() {
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
}

private fun getBundledJFX(): File? {
    val os = System.getProperty("os.name").toLowerCase()
    return when {
        os.contains("mac") -> File(System.getProperty("user.dir"), "Library/Application Support/minecraft/runtime/jre-x64/jre.bundle/Contents/Home/lib/ext/jfxrt.jar")
        os.contains("win") -> File("C:\\Program Files (x86)\\Minecraft Launcher\\runtime\\jre-x64\\lib\\ext\\jfxrt.jar")
        else -> null
    }
}