package com.bottlerocket.service

import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.AssetReference
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import com.bottlerocket.util.ID
import com.bottlerocket.util.adsContainer
import com.bottlerocket.util.advertisement
import com.bottlerocket.util.assetReference
import com.bottlerocket.util.containerAdvertisement
import com.bottlerocket.util.containerAsset
import com.bottlerocket.util.containerImage
import com.bottlerocket.util.containerVideo
import com.bottlerocket.util.emptyContainer
import com.bottlerocket.util.fullContainer
import com.bottlerocket.util.image
import com.bottlerocket.util.imagesContainer
import com.bottlerocket.util.video
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DataConverterTest {

    @Test
    fun `GIVEN advertisement WHEN convertAdvertisement THEN converts data`() {
        // When
        val actual: Advertisement = convertAdvertisement(advertisement)

        // Then
        assertEquals(containerAdvertisement, actual, "Converted advertisement should equal expected")
    }

    @Test
    fun `GIVEN asset WHEN convertAssetReference THEN converts data`() {
        // When
        val actual: AssetReference = convertAssetReference(assetReference)

        // Then
        assertEquals(containerAsset, actual, "Converted asset reference should equal expected")
    }

    @Test
    fun `GIVEN ads WHEN convertContainer THEN converts data`() {
        // When
        val actual: Container = convertContainer(
            id = ID,
            ads = listOf(containerAdvertisement),
            images = emptyList(),
            videos = listOf(containerVideo))

        // Then
        assertEquals(adsContainer, actual, "Converted container should equal expected")
    }

    @Test
    fun `GIVEN images WHEN convertContainer THEN converts data`() {
        // When
        val actual: Container = convertContainer(
            id = ID,
            ads = emptyList(),
            images = listOf(containerImage),
            videos = listOf(containerVideo))

        // Then
        assertEquals(imagesContainer, actual, "Converted container should equal expected")
    }

    @Test
    fun `GIVEN ads and images WHEN convertContainer THEN converts data`() {
        // When
        val actual: Container = convertContainer(
            id = ID,
            ads = listOf(containerAdvertisement),
            images = listOf(containerImage),
            videos = listOf(containerVideo))

        // Then
        assertEquals(fullContainer, actual, "Converted container should equal expected")
    }

    @Test
    fun `GIVEN videos WHEN convertContainer THEN converts data`() {
        // When
        val actual: Container = convertContainer(ID, emptyList(), emptyList(), listOf(containerVideo))

        // Then
        assertEquals(emptyContainer, actual, "Converted container should equal expected")
    }

    @Test
    fun `GIVEN image WHEN convertImage THEN converts data`() {
        // When
        val actual: Image = convertImage(image)

        // Then
        assertEquals(containerImage, actual, "Converted image should equal expected")
    }

    @Test
    fun `GIVEN video WHEN convertVideo THEN converts data`() {
        // When
        val actual: Video = convertVideo(video, listOf(containerAsset))

        // Then
        assertEquals(containerVideo, actual, "Converted video should equal expected")
    }
}
