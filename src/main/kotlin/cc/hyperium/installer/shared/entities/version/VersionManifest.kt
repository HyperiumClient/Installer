/*
 * Copyright (C) 2019 Cubxity. All Rights Reserved.
 */

package cc.hyperium.installer.shared.entities.version

import com.google.gson.annotations.SerializedName

data class VersionManifest (
    val latest: Version,
    val versions: List<Version>,
    @SerializedName("latest_beta")
    val latestBeta: Version
)