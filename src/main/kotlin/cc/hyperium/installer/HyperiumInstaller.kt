/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer

import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import tornadofx.App
import tornadofx.launch
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