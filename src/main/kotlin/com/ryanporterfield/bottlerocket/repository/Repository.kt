package com.ryanporterfield.bottlerocket.repository

import com.ryanporterfield.bottlerocket.data.Advertisement
import com.ryanporterfield.bottlerocket.data.AssetReference
import com.ryanporterfield.bottlerocket.data.Image
import com.ryanporterfield.bottlerocket.data.Video
import io.ktor.client.*
import io.ktor.client.request.*

const val ASSET_REFERENCE_SUFFIX = "asset-references"
const val ADVERTISEMENT_ENDPOINT = "http://ads.rocket-stream.bottlerocketservices.com/advertisements"
const val IMAGE_ENDPOINT = "http://images.rocket-stream.bottlerocketservices.com/images"
const val VIDEO_ENDPOINT = "http://videos.rocket-stream.bottlerocketservices.com/videos"

suspend fun getVideo(client: HttpClient, videoId: Int): Video {
    return client.get("$VIDEO_ENDPOINT/$videoId")
}

suspend fun listAdvertisements(client: HttpClient): List<Advertisement> {
    return client.get(ADVERTISEMENT_ENDPOINT)
}

suspend fun listAdvertisements(client: HttpClient, containerId: Int): List<Advertisement> {
    return client.get(ADVERTISEMENT_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

suspend fun listAssetReferences(client: HttpClient, videoId: Int): List<AssetReference> {
    return client.get("$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX")
}

suspend fun listAssetReferences(client: HttpClient, videoId: Int, assetType: AssetReference.Type): List<AssetReference> {
    return client.get("$VIDEO_ENDPOINT/$videoId/$ASSET_REFERENCE_SUFFIX") {
        parameter("assetType", assetType)
    }
}

suspend fun listImages(client: HttpClient): List<Image> {
    return client.get(IMAGE_ENDPOINT)
}

suspend fun listImages(client: HttpClient, containerId: Int): List<Image> {
    return client.get(IMAGE_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

suspend fun listVideos(client: HttpClient): List<Video> {
    return client.get(VIDEO_ENDPOINT)
}

suspend fun listVideos(client: HttpClient, containerId: Int): List<Video> {
    return client.get(VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
    }
}

suspend fun listVideos(client: HttpClient, type: Video.Type): List<Video> {
    return client.get(VIDEO_ENDPOINT) {
        parameter("type", type.name)
    }
}

suspend fun listVideos(client: HttpClient, containerId: Int, type: Video.Type): List<Video> {
    return client.get(VIDEO_ENDPOINT) {
        parameter("containerId", containerId)
        parameter("type", type.name)
    }
}
