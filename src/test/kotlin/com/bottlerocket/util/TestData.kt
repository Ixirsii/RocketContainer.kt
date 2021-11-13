package com.bottlerocket.util

import com.bottlerocket.data.AssetType
import com.bottlerocket.data.VideoType
import com.bottlerocket.data.advertisementService.Advertisements
import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.AssetReference
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import com.bottlerocket.data.imageService.Images
import com.bottlerocket.data.videoService.VideoAssets
import com.bottlerocket.data.videoService.Videos

// Constants
const val ADVERTISEMENT_NAME = "Advertisement"
const val ADVERTISEMENT_URL = "https://www.google.com"
const val CONTAINER_ID = 1
const val DESCRIPTION = "Video description"
const val EXPIRATION_DATE = "1970-01-01"
const val ID = 1
const val IMAGE_NAME = "Image"
const val IMAGE_URL = "https://images.google.com"
const val TITLE = "Test video"
const val VIDEO_URL = "https://youtube.com/"

// Values which can't be const
val assetType = AssetType.IMAGE
val videoType = VideoType.CLIP

// Dependent service types
val advertisement = com.bottlerocket.data.advertisementService.Advertisement(
    containerId = CONTAINER_ID,
    id = ID,
    name = ADVERTISEMENT_NAME,
    url = ADVERTISEMENT_URL
)
val advertisements = Advertisements(advertisements = listOf(advertisement))
val assetReference = com.bottlerocket.data.videoService.AssetReference(
    assetId = ID,
    assetType = assetType,
    videoId = ID
)
val videoAssets = VideoAssets(videoAssets = listOf(assetReference))
val image = com.bottlerocket.data.imageService.Image(
    containerId = CONTAINER_ID,
    id = ID,
    name = IMAGE_NAME,
    url = IMAGE_URL
)
val images = Images(images = listOf(image))
val video = com.bottlerocket.data.videoService.Video(
    containerId = CONTAINER_ID,
    description = DESCRIPTION,
    expirationDate = EXPIRATION_DATE,
    id = ID,
    playbackUrl = VIDEO_URL,
    title = TITLE,
    type = videoType
)
val videos = Videos(videos = listOf(video))

// Container service types
val containerAdvertisement = Advertisement(
    id = ID,
    name = ADVERTISEMENT_NAME,
    url = ADVERTISEMENT_URL
)
val containerAsset = AssetReference(
    assetId = ID,
    assetType = assetType
)
val containerImage = Image(
    id = ID,
    name = IMAGE_NAME,
    url = IMAGE_URL
)
val containerVideo = Video(
    assets = listOf(containerAsset),
    description = DESCRIPTION,
    expirationDate = EXPIRATION_DATE,
    id = ID,
    playbackUrl = VIDEO_URL,
    title = TITLE,
    type = videoType
)

// Containers
val emptyContainer = Container(
    ads = emptyList(),
    id = ID,
    images = emptyList(),
    title = "container-${ID}_videos",
    videos = listOf(containerVideo)
)
val adsContainer = Container(
    ads = listOf(containerAdvertisement),
    id = ID,
    images = emptyList(),
    title = "container-${ID}_ads_videos",
    videos = listOf(containerVideo)
)
val imagesContainer = Container(
    ads = emptyList(),
    id = ID,
    images = listOf(containerImage),
    title = "container-${ID}_images_videos",
    videos = listOf(containerVideo)
)
val fullContainer = Container(
    ads = listOf(containerAdvertisement),
    id = ID,
    images = listOf(containerImage),
    title = "container-${ID}_ads_images_videos",
    videos = listOf(containerVideo)
)
