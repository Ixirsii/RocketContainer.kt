package com.ryanporterfield.bottlerocket.data

import kotlinx.serialization.Serializable

@Serializable
data class Advertisement(val id: Int, val name: String, val url: String)
