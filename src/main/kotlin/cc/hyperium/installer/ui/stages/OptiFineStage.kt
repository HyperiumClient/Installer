package cc.hyperium.installer.ui.stages

import cc.hyperium.installer.backend.Installer
import cc.hyperium.installer.backend.config.JFXConfig
import cc.hyperium.installer.backend.util.Desktop
import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import javafx.stage.FileChooser
import kfoenix.jfxbutton
import kotlinx.coroutines.launch
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.Opcodes
import tornadofx.*
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.util.jar.JarFile
import java.util.zip.ZipEntry

class OptiFineStage : View() {
    override val root = vbox {
        addClass(InstallerStyles.container)
        label("Install OptiFine") { addClass(InstallerStyles.title) }
        hyperlink("Click here to get to the OptiFine download page!") { addClass(InstallerStyles.desc) }.action {
            Installer.launch {
                val url = readOptiFineVersion()
                Installer.logger.info("Attempting to open $url")
                if (!Desktop.browse(URI(url))) Installer.logger
                    .error("Failed to open $url. Please navigate to $url manually.")
            }
        }

        pane { addClass(InstallerStyles.spacer) }

        jfxbutton("Select OptiFine Jar") {
            addClass(InstallerStyles.longButton)
            action {
                chooseFile(
                    "Choose OptiFine jar",
                    arrayOf(FileChooser.ExtensionFilter("JAR Files (*.jar)", "*.jar")),
                    FileChooserMode.Single
                ) {
                    initialDirectory = File(System.getProperty("user.home"), "Downloads")
                }.joinToString().also { JFXConfig.optifinePath = it }
            }
        }
        jfxbutton("NEXT") {
            addClass(InstallerStyles.longButton)
            action {
                if (verifyOptiFineVersion(File(JFXConfig.optifinePath)) == "1.8.9") {
                    find<InstallerView> { tabPane.selectionModel.selectNext() }
                } else {
                    // TODO: let user know they fucked up
                }
            }
        }

        jfxbutton("Back") {
            addClass(InstallerStyles.backButton)
            action {
                find<InstallerView> { tabPane.selectionModel.selectPrevious() }
            }
        }
    }

    private fun readOptiFineVersion(): String {
        val ver = URL("http://optifine.net/version/1.8.9/HD_U.txt").readText().lines().first()
        val fileName = "OptiFine_1.8.9_HD_U_$ver.jar"
        return "http://optifine.net/adloadx?f=${URLEncoder.encode(fileName, "UTF-8")}"
    }

    private fun verifyOptiFineVersion(jar: File): String? {
        try {
            val jarfile = JarFile(jar)
            val entry = jarfile.getEntry("Config.class") ?: jarfile.getEntry("net/optifine/Config.class")
            val reader = ClassReader(jarfile.getInputStream(entry as ZipEntry).use { it.readBytes() })
            var version: String? = null
            reader.accept(object : ClassVisitor(Opcodes.ASM7) {
                override fun visitField(
                    access: Int, name: String?, descriptor: String?, signature: String?, value: Any?
                ): FieldVisitor? {
                    if (name == "MC_VERSION" && value is String) {
                        version = value
                    }
                    return super.visitField(access, name, descriptor, signature, value)
                }
            }, ClassReader.EXPAND_FRAMES)
            return version
        } catch (_: Throwable) {
            return null
        }
    }
}