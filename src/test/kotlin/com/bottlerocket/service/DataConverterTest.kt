package com.bottlerocket.service

import com.bottlerocket.data.AssetType
import com.bottlerocket.data.VideoType
import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.AssetReference
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DataConverterTest {
    private val assets: List<AssetReference> = emptyList()
    private val containerAdvertisement = Advertisement(
        id = 1,
        name = "Advertisement",
        url = "URL"
    )
    private val containerAsset = AssetReference(assetId = 1, assetType = AssetType.AD)
    private val containerImage = Image(
        id = 1,
        name = "Image",
        url = "Url"
    )
    private val containerVideo = Video(
        assets = assets,
        description = "Description",
        expirationDate = "1970-01-01",
        id = 1,
        playbackUrl = "Playback Url",
        title = "Title",
        type = VideoType.MOVIE
    )

    @Test
    fun `GIVEN advertisement WHEN convertAdvertisement THEN converts data`() {
        // Given
        val advertisement = com.bottlerocket.data.advertisementService.Advertisement(
            containerId = 1,
            id = containerAdvertisement.id,
            name = containerAdvertisement.name,
            url = containerAdvertisement.url
        )

        // When
        val actual: Advertisement = convertAdvertisement(advertisement)

        // Then
        assertEquals(containerAdvertisement, actual, "Converted advertisement should equal expected")
    }

    @Test
    fun `GIVEN asset WHEN convertAssetReference THEN converts data`() {
        // Given
        val assetReference = com.bottlerocket.data.videoService.AssetReference(
            assetId = containerAsset.assetId,
            assetType = containerAsset.assetType,
            videoId = 1
        )

        // When
        val actual: AssetReference = convertAssetReference(assetReference)

        // Then
        assertEquals(containerAsset, actual, "Converted asset reference should equal expected")
    }

    @Test
    fun `GIVEN ads and images WHEN convertContainer THEN converts data`() {
        // Given
        val ads: List<Advertisement> = listOf(containerAdvertisement)
        val id = 1
        val images: List<Image> = listOf(containerImage)
        val videos: List<Video> = listOf(containerVideo)
        val container = Container(
            ads = ads,
            id = id,
            images = images,
            title = "container-1_ads_images_videos",
            videos = videos
        )

        // When
        val actual: Container = convertContainer(id, ads, images, videos)

        // Then
        assertEquals(container, actual, "Converted container should equal expected")
    }

    @Test
    fun `GIVEN videos WHEN convertContainer THEN converts data`() {
        // Given
        val ads: List<Advertisement> = emptyList()
        val id = 1
        val images: List<Image> = emptyList()
        val videos: List<Video> = listOf(containerVideo)
        val container = Container(
            ads = ads,
            id = id,
            images = images,
            title = "container-1_videos",
            videos = videos
        )

        // When
        val actual: Container = convertContainer(id, ads, images, videos)

        // Then
        assertEquals(container, actual, "Converted container should equal expected")
    }

    @Test
    fun `GIVEN image WHEN convertImage THEN converts data`() {
        // Given
        val image = com.bottlerocket.data.imageService.Image(
            containerId = 1,
            id = containerImage.id,
            name = containerImage.name,
            url = containerImage.url
        )

        // When
        val actual: Image = convertImage(image)

        // Then
        assertEquals(containerImage, actual, "Converted image should equal expected")
    }

    @Test
    fun `GIVEN video WHEN convertVideo THEN converts data`() {
        // Given
        val video = com.bottlerocket.data.videoService.Video(
            containerId = 1,
            description = containerVideo.description,
            expirationDate = containerVideo.expirationDate,
            id = containerVideo.id,
            playbackUrl = containerVideo.playbackUrl,
            title = containerVideo.title,
            type = containerVideo.type
        )

        // When
        val actual: Video = convertVideo(video, assets)

        // Then
        assertEquals(containerVideo, actual, "Converted video should equal expected")
    }
}
