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

package cc.hyperium.installer.ui

import javafx.geometry.Orientation
import kfoenix.jfxbutton
import tornadofx.*

class ConfirmationDialog(title: String, private val label: String, private val onConfirmed: () -> Unit) : View(title) {
    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            field(label, Orientation.VERTICAL) {
                addClass(InstallerStyles.desc)
            }
            buttonbar {
                jfxbutton("Yes") {
                    action {
                        close()
                        onConfirmed()
                    }
                }
                jfxbutton("No") {
                    action {
                        close()
                    }
                }
            }
        }
    }
}