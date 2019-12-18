package cc.hyperium.installer.backend.util

import cc.hyperium.installer.backend.Installer
import org.apache.commons.lang3.SystemUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI

fun browse(uri: URI): Boolean {
    if (browseDesktop(uri) || openSystemSpecific(uri.toString())) {
        return true
    }

    Installer.logger.warn("Failed to browse $uri")
    return false
}

fun open(file: File): Boolean {
    if (openDesktop(file) || openSystemSpecific(file.path)) {
        return true
    }

    Installer.logger.warn("Failed to open ${file.absolutePath}")
    return false
}

fun edit(file: File): Boolean {
    if (editDesktop(file) || openSystemSpecific(file.path)) {
        return true
    }

    Installer.logger.warn("Failed to edit ${file.absolutePath}")
    return false
}

private fun openSystemSpecific(file: String): Boolean {
    if (SystemUtils.IS_OS_LINUX) {
        if (xdg) {
            if (runCommand("xdg-open", "%s", file)) {
                return true
            }
        }

        if (kde) {
            if (runCommand("kde-open", "%s", file)) {
                return true
            }
        }

        if (gnome) {
            if (runCommand("gnome-open", "%s", file)) {
                return true
            }
        }

        if (runCommand("kde-open", "%s", file)) {
            return true
        }

        if (runCommand("gnome-open", "%s", file)) {
            return true
        }
    }

    if (SystemUtils.IS_OS_MAC) {
        if (runCommand("open", "%s", file)) {
            return true
        }
    }

    return if (SystemUtils.IS_OS_WINDOWS) {
        runCommand("explorer", "%s", file)
    } else false
}

private fun browseDesktop(uri: URI): Boolean {
    return try {
        if (!Desktop.isDesktopSupported()) {
            Installer.logger.debug("Platform is not supported.")
            return false
        }

        if (!Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Installer.logger.debug("BROWSE is not supported.")
            return false
        }

        Installer.logger.info("Trying to use Desktop.getDesktop().browse() with $uri")
        Desktop.getDesktop().browse(uri)
        true
    } catch (t: Throwable) {
        Installer.logger.error("Error using desktop browse.", t)
        false
    }
}

private fun openDesktop(file: File): Boolean {
    return try {
        if (!Desktop.isDesktopSupported()) {
            Installer.logger.debug("Platform is not supported.")
            return false
        }

        if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Installer.logger.debug("OPEN is not supported.")
            return false
        }

        Installer.logger.info("Trying to use Desktop.getDesktop().open() with $file")
        Desktop.getDesktop().open(file)
        true
    } catch (t: Throwable) {
        Installer.logger.error("Error using desktop open.", t)
        false
    }
}

private fun editDesktop(file: File): Boolean {
    return try {
        if (!Desktop.isDesktopSupported()) {
            Installer.logger.debug("Platform is not supported.")
            return false
        }
        if (!Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
            Installer.logger.debug("EDIT is not supported.")
            return false
        }
        Installer.logger.info("Trying to use Desktop.getDesktop().edit() with $file")
        Desktop.getDesktop().edit(file)
        true
    } catch (t: Throwable) {
        Installer.logger.error("Error using desktop edit.", t)
        false
    }
}

private fun runCommand(command: String, args: String, file: String): Boolean {
    Installer.logger.info("Trying to exec:\n   cmd = $command\n   args = $args\n   %s = $file")
    val parts = prepareCommand(command, args, file)
    return try {
        val p = Runtime.getRuntime().exec(parts) ?: return false
        try {
            Installer.logger.error(if (p.exitValue() == 0) "Process ended immediately." else "Process crashed.")
            false
        } catch (itse: IllegalThreadStateException) {
            Installer.logger.error("Process is running.")
            true
        }
    } catch (e: IOException) {
        Installer.logger.error("Error running command.", e)
        false
    }
}

private fun prepareCommand(
    command: String,
    args: String,
    file: String
): Array<String> {
    val parts = arrayListOf<String>()
    parts.add(command)
    args.split(" ").toTypedArray().forEach { s ->
        var fileName = s
        fileName = String.format(fileName, file) // put in the filename thing
        parts.add(fileName.trim { it <= ' ' })
    }

    return parts.toTypedArray()
}

private val xdg: Boolean
    get() {
        val xdgSessionId = System.getenv("XDG_SESSION_ID")
        return xdgSessionId != null && xdgSessionId.isNotEmpty()
    }

private val gnome: Boolean
    get() {
        val gdmSession = System.getenv("GDMSESSION")
        return gdmSession != null && gdmSession.toLowerCase().contains("gnome")
    }

private val kde: Boolean
    get() {
        val gdmSession = System.getenv("GDMSESSION")
        return gdmSession != null && gdmSession.toLowerCase().contains("kde")
    }