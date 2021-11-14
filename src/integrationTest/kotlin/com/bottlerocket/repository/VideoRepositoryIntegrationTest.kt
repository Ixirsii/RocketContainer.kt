package com.bottlerocket.repository

import com.bottlerocket.data.AssetType
import com.bottlerocket.data.VideoType
import com.bottlerocket.data.videoService.Video
import com.bottlerocket.data.videoService.VideoAssets
import com.bottlerocket.data.videoService.Videos
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.ServerResponseException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class VideoRepositoryIntegrationTest {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private lateinit var underTest: VideoRepository

    @BeforeEach
    fun setup() {
        underTest = VideoRepository(client)
    }

    @Test
    fun `GIVEN videoId WHEN getVideo THEN returns video`() {
        // Given
        val videoId = 1301

        // When
        val actual: Video = underTest.getVideo(videoId)

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
            underTest.getVideo(videoId)
        }
    }

    @Test
    fun `GIVEN videoId WHEN listAssetReferences THEN returns assets`() {
        // Given
        val videoId = 1404

        // When
        val actual: VideoAssets = underTest.listAssetReferences(videoId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.videoAssets.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN invalid videoId WHEN listAssetReferences THEN returns empty assets`() {
        // Given
        val videoId = 0

        // When
        val actual: VideoAssets = underTest.listAssetReferences(videoId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue(actual.videoAssets.isEmpty(), "Response should be empty")
    }

    @Test
    fun `GIVEN assetType WHEN listAssetReferences THEN returns assets`() {
        // Given
        val videoId = 1404
        val assetType = AssetType.IMAGE

        // When
        val actual: VideoAssets = underTest.listAssetReferences(videoId, assetType)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.videoAssets.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN invalid assetType WHEN listAssetReferences THEN returns empty assets`() {
        // Given
        val videoId = 1404
        val assetType = AssetType.AD

        // When
        val actual: VideoAssets = underTest.listAssetReferences(videoId, assetType)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue(actual.videoAssets.isEmpty(), "Response should be empty")
    }

    @Test
    fun `GIVEN client WHEN listVideos THEN returns videos`() {
        // When
        val actual: Videos = underTest.listVideos()

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.videos.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN containerId WHEN listVideos THEN returns videos`() {
        // Given
        val containerId = 17

        // When
        val actual: Videos = underTest.listVideos(containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.videos.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN invalid containerId WHEN listVideos THEN returns empty videos`() {
        // Given
        val containerId = 31

        // When
        val actual: Videos = underTest.listVideos(containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue(actual.videos.isEmpty(), "Response should be empty")
    }

    @Test
    fun `GIVEN type WHEN listVideos THEN returns videos`() {
        // Given
        val type = VideoType.CLIP

        // When
        val actual: Videos = underTest.listVideos(type)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.videos.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN containerId and type WHEN listVideos THEN returns videos`() {
        // Given
        val containerId = 17
        val type = VideoType.CLIP

        // When
        val actual: Videos = underTest.listVideos(containerId, type)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.videos.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN invalid containerId and type WHEN listVideos THEN returns empty videos`() {
        // Given
        val containerId = 31
        val type = VideoType.CLIP

        // When
        val actual: Videos = underTest.listVideos(containerId, type)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue(actual.videos.isEmpty(), "Response should be empty")
    }
}
