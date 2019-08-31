/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer

import cc.hyperium.installer.ui.InstallerStyles
import cc.hyperium.installer.ui.InstallerView
import tornadofx.App
import tornadofx.launch

class InstallerApp : App(InstallerView::class, InstallerStyles::class)

fun main() = launch<InstallerApp>()