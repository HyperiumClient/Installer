/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.ui

import javafx.geometry.Pos
import javafx.scene.paint.Color
import kfoenix.JFXStylesheet
import tornadofx.*

class InstallerStyles : JFXStylesheet() {
    companion object {
        val dark = c("#222222")
        val darker = c("#202020")
        val descColor = c("#eeeeee")
        val primary = c("#90CAF9")

        val jfxTabPane by cssclass()

        val longButton by cssclass()
        val container by cssclass()
        val radioContainer by cssclass()
        val spacer by cssclass()
        val title by cssclass()
        val desc by cssclass()
    }

    init {
        val desc = loadFont("/assets/fonts/OpenSans-Regular.ttf", 10)
        val reg = loadFont("/assets/fonts/OpenSans-Regular.ttf", 11)
        val title = loadFont("/assets/fonts/OpenSans-Regular.ttf", 18)

        root {
            backgroundColor += dark
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
        radioContainer {
            alignment = Pos.TOP_LEFT
            maxWidth = 200.px
            jfxRadioButton {
                alignment = Pos.CENTER
            }
        }
    }
}