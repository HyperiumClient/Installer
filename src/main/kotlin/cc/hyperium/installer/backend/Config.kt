/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.backend

data class Config(
    var advanced: Boolean = false,
    var ram: Int = 2,
    var path: String = ""
)