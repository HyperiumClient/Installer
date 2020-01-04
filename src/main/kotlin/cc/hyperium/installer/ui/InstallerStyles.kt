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

package cc.hyperium.installer.ui

import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.paint.Color
import kfoenix.JFXStylesheet
import tornadofx.*

class InstallerStyles : JFXStylesheet() {
    companion object {
        val dark = c("#222222")
        val descColor = c("#eeeeee")
        val primary = c("#90CAF9")
        val secondary = c("#90CAF9")

        val jfxTabPane by cssclass()
        val coloredTrack by cssclass()
        val animatedThumb by cssclass()

        val longButton by cssclass()
        val selectedButton by cssclass()
        val backButtonContainer by cssclass()
        val backButton by cssclass()
        val container by cssclass()
        val spacer by cssclass()
        val title by cssclass()
        val desc by cssclass()
    }

    init {
        val desc = loadFont("/assets/fonts/OpenSans-Regular.ttf", 10)
        val reg = loadFont("/assets/fonts/OpenSans-Regular.ttf", 11)
        val title = loadFont("/assets/fonts/OpenSans-Regular.ttf", 18)

        s(root, cell, virtualFlow) {
            backgroundColor += dark
        }

        comboBoxPopup {
            listView {
                backgroundColor += dark
            }
        }

        jfxComboBox {
            textField {
                backgroundColor += dark
            }
        }

        text {
            reg?.let { font = it }
            fill = Color.WHITE
        }

        title {
            text {
                title?.let { font = it }
            }
        }

        desc {
            text {
                desc?.let { font = it }
                fill = descColor
            }
        }

        jfxTabPane {
            tabMaxHeight = 0.px
            tabHeaderArea {
                visibility = FXVisibility.HIDDEN
            }
        }

        jfxButton {
            backgroundColor += primary
            text {
                fill = Color.BLACK
            }
        }

        backButton {
            backgroundColor += Color.TRANSPARENT
            textFill = descColor
            desc?.also { font = it }
        }

        backButtonContainer {
            spacing = 10.px
        }

        s(slider, textField, comboBox) {
            textFill = descColor
            maxWidth = 200.px
        }

        textArea {
            textFill = descColor
            maxWidth = 400.px
        }

        scrollBar {
            backgroundColor += Color.TRANSPARENT
            prefWidth = 7.px
            prefHeight = 7.px
            arrowsVisible = false
            s(incrementArrow, decrementArrow) {
                visibility = FXVisibility.HIDDEN
                maxWidth = 0.px
                maxHeight = 0.px
            }
        }

        longButton {
            prefWidth = 200.px
        }

        spacer {
            prefHeight = 20.px
        }

        container {
            spacing = 12.px
            alignment = Pos.CENTER
        }

        s(thumb, coloredTrack, selectedButton, animatedThumb) {
            backgroundColor += secondary
        }

        jfxCheckBox {
            jfxCheckedColor.set(secondary)
        }

        addStageIcon(Image("/assets/logo.png"))
    }
}