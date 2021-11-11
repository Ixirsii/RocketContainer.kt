package com.bottlerocket.service

import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.AssetReference
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video

/**
 * Convert an advertisement service advertisement into a container service advertisement.
 *
 * @param advertisement Advertisement service data.
 * @return container service advertisement.
 * @see com.bottlerocket.data.advertisementService.Advertisement
 * @see com.bottlerocket.data.containerService.Advertisement
 */
fun convertAdvertisement(advertisement: com.bottlerocket.data.advertisementService.Advertisement): Advertisement {
    return Advertisement(
        id = advertisement.id,
        name = advertisement.name,
        url = advertisement.url
    )
}

/**
 * Convert a video service asset reference into a container service asset reference.
 *
 * @param asset Video service data.
 * @return container service asset reference.
 * @see com.bottlerocket.data.videoService.AssetReference
 * @see com.bottlerocket.data.containerService.AssetReference
 */
fun convertAssetReference(asset: com.bottlerocket.data.videoService.AssetReference): AssetReference {
    return AssetReference(
        assetId = asset.assetId,
        assetType = asset.assetType
    )
}

/**
 * Build a [Container] from data.
 *
 * @param id Container ID.
 * @param ads Container advertisements.
 * @param images Container images.
 * @param videos Container videos.
 * @return a container.
 */
fun convertContainer(
    id: Int,
    ads: List<Advertisement>,
    images: List<Image>,
    videos: List<Video>
):Container {
    val titleAds: String = if (ads.isNotEmpty()) "_ads" else ""
    val titleImages: String = if (images.isNotEmpty()) "_images" else ""
    val title = "container-$id${titleAds}${titleImages}_videos"

    return Container(
        ads = ads,
        id = id,
        images = images,
        title = title,
        videos = videos
    )
}

/**
 * Convert an image service image into a container service image.
 *
 * @param image Image service data.
 * @return container service image.
 * @see com.bottlerocket.data.imageService.Image
 * @see com.bottlerocket.data.containerService.Image
 */
fun convertImage(image: com.bottlerocket.data.imageService.Image): Image {
    return Image(
        id = image.id,
        name = image.name,
        url = image.url
    )
}

/**
 * Convert a video service video into a container service video.
 *
 * @param video Video service data.
 * @param assets Video assets.
 * @return container service video.
 * @see com.bottlerocket.data.videoService.Video
 * @see com.bottlerocket.data.containerService.Video
 */
fun convertVideo(video: com.bottlerocket.data.videoService.Video, assets: List<AssetReference>): Video {
    return Video(
        assets = assets,
        description = video.description,
        expirationDate = video.expirationDate,
        id = video.id,
        playbackUrl = video.playbackUrl,
        title = video.title,
        type = video.type
    )
}
