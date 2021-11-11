package com.bottlerocket.repository

import com.bottlerocket.data.AssetType
import com.bottlerocket.data.VideoType
import com.bottlerocket.data.advertisementService.Advertisements
import com.bottlerocket.data.imageService.Images
import com.bottlerocket.data.videoService.Video
import com.bottlerocket.data.videoService.VideoAssets
import com.bottlerocket.data.videoService.Videos
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Tag("integration")
internal class RepositoryIntegrationTest {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    @Test
    fun `GIVEN videoId WHEN getVideo THEN returns video`() {
        // Given
        val videoId = 1301

        // When
        val actual: Video = getVideo(client, videoId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertEquals(videoId, actual.id, "Video ID should equal requested ID")
    }

    @Test
    fun `GIVEN invalid videoId WHEN getVideo THEN throws ServerResponseException`() {
        // Given
        val videoId = 0

        // When
        assertThrows<ServerResponseException>("Should throw ServerResponseException on invalid request") {
            getVideo(client, videoId)
        }
    }

    @Test
    fun `GIVEN client WHEN listAdvertisements THEN returns advertisements`() {
        // When
        val actual: Advertisements = listAdvertisements(client)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.advertisements.isEmpty() }
    }

    @Test
    fun `GIVEN containerId WHEN listAdvertisements THEN returns advertisements`() {
        // Given
        val containerId = 27

        // When
        val actual: Advertisements = listAdvertisements(client, containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.advertisements.isEmpty() }
    }

    @Test
    fun `GIVEN invalid containerId WHEN listAdvertisements THEN returns empty advertisements`() {
        // Given
        val containerId = 31

        // When
        val actual: Advertisements = listAdvertisements(client, containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.advertisements.isEmpty() }
    }

    @Test
    fun `GIVEN videoId WHEN listAssetReferences THEN returns assets`() {
        // Given
        val videoId = 1404

        // When
        val actual: VideoAssets = listAssetReferences(client, videoId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.videoAssets.isEmpty() }
    }

    @Test
    fun `GIVEN invalid videoId WHEN listAssetReferences THEN returns empty assets`() {
        // Given
        val videoId = 0

        // When
        val actual: VideoAssets = listAssetReferences(client, videoId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.videoAssets.isEmpty() }
    }

    @Test
    fun `GIVEN assetType WHEN listAssetReferences THEN returns assets`() {
        // Given
        val videoId = 1404
        val assetType = AssetType.IMAGE

        // When
        val actual: VideoAssets = listAssetReferences(client, videoId, assetType)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.videoAssets.isEmpty() }
    }

    @Test
    fun `GIVEN invalid assetType WHEN listAssetReferences THEN returns empty assets`() {
        // Given
        val videoId = 1404
        val assetType = AssetType.AD

        // When
        val actual: VideoAssets = listAssetReferences(client, videoId, assetType)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.videoAssets.isEmpty() }
    }

    @Test
    fun `GIVEN client WHEN listImages THEN returns images`() {
        // When
        val actual: Images = listImages(client)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.images.isEmpty() }
    }

    @Test
    fun `GIVEN containerId WHEN listImages THEN returns images`() {
        // Given
        val containerId = 17

        // When
        val actual: Images = listImages(client, containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.images.isEmpty() }
    }

    @Test
    fun `GIVEN invalid containerId WHEN listImages THEN returns empty images`() {
        // Given
        val containerId = 31

        // When
        val actual: Images = listImages(client, containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.images.isEmpty() }
    }

    @Test
    fun `GIVEN client WHEN listVideos THEN returns videos`() {
        // When
        val actual: Videos = listVideos(client)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.videos.isEmpty() }
    }

    @Test
    fun `GIVEN containerId WHEN listVideos THEN returns videos`() {
        // Given
        val containerId = 17

        // When
        val actual: Videos = listVideos(client, containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.videos.isEmpty() }
    }

    @Test
    fun `GIVEN invalid containerId WHEN listVideos THEN returns empty videos`() {
        // Given
        val containerId = 31

        // When
        val actual: Videos = listVideos(client, containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.videos.isEmpty() }
    }

    @Test
    fun `GIVEN type WHEN listVideos THEN returns videos`() {
        // Given
        val type = VideoType.CLIP

        // When
        val actual: Videos = listVideos(client, type)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.videos.isEmpty() }
    }

    @Test
    fun `GIVEN containerId and type WHEN listVideos THEN returns videos`() {
        // Given
        val containerId = 17
        val type = VideoType.CLIP

        // When
        val actual: Videos = listVideos(client, containerId, type)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.videos.isEmpty() }
    }

    @Test
    fun `GIVEN invalid containerId and type WHEN listVideos THEN returns empty videos`() {
        // Given
        val containerId = 31
        val type = VideoType.CLIP

        // When
        val actual: Videos = listVideos(client, containerId, type)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.videos.isEmpty() }
    }
}
