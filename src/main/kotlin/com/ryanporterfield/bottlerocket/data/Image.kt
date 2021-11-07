package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

@Serializable
data class Image(val id: Int, val name: String, val url: String)
