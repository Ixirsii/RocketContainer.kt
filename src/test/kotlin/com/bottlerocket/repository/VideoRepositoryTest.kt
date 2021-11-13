package com.bottlerocket.repository

import com.bottlerocket.data.videoService.Video
import com.bottlerocket.data.videoService.VideoAssets
import com.bottlerocket.data.videoService.Videos
import com.bottlerocket.module.httpClient
import com.bottlerocket.module.json
import com.bottlerocket.util.CONTAINER_ID
import com.bottlerocket.util.DESCRIPTION
import com.bottlerocket.util.EXPIRATION_DATE
import com.bottlerocket.util.ID
import com.bottlerocket.util.TITLE
import com.bottlerocket.util.VIDEO_URL
import com.bottlerocket.util.assetType
import com.bottlerocket.util.video
import com.bottlerocket.util.videoAssets
import com.bottlerocket.util.videoType
import com.bottlerocket.util.videos
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.ServerResponseException
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class VideoRepositoryTest {

    @Test
    fun `GIVEN videoId WHEN getVideo THEN returns Video`() {
        // Given
        val mockEngine = getMockEngine(
            ByteReadChannel(
                """{
                    "containerId": $CONTAINER_ID,
                    "description": "$DESCRIPTION",
                    "expirationDate": "$EXPIRATION_DATE",
                    "id": $ID,
                    "playbackUrl": "$VIDEO_URL",
                    "title": "$TITLE",
                    "type": "$videoType"
                }""".trimMargin()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: Video = underTest.getVideo(ID)

        // Then
        assertEquals(video, actual, "Video should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 requests")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().endsWith(ID.toString()) }
        }
    }

    @Test
    fun `GIVEN invalidRequest WHEN getVideo THEN throws ServerResponseException`() {
        // Given
        val mockEngine: MockEngine = getMockEngine()
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        assertThrows<ServerResponseException> { underTest.getVideo(ID) }

        // Then
        assertEquals(3, mockEngine.requestHistory.size, "Should make 3 requests")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().endsWith(ID.toString()) }
        }
    }

    @Test
    fun `GIVEN video ID WHEN listAssetReferences THEN returns VideoAssets`() {
        // Given
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videoAssets": [{
                    "assetId": $ID,
                    "assetType": "$assetType",
                    "videoId": "$ID"
                }]}""".trimIndent()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: VideoAssets = underTest.listAssetReferences(ID)

        // Then
        assertEquals(videoAssets, actual, "VideoAssets should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().contains(ID.toString()) }
        }
    }

    @Test
    fun `GIVEN assetType ID WHEN listAssetReferences THEN returns VideoAssets`() {
        // Given
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videoAssets": [{
                    "assetId": $ID,
                    "assetType": "$assetType",
                    "videoId": "$ID"
                }]}""".trimIndent()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: VideoAssets = underTest.listAssetReferences(ID, assetType)

        // Then
        assertEquals(videoAssets, actual, "VideoAssets should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().contains(ID.toString()) }
            assertNotNull(it.url.parameters["assetType"], "Query parameters should contain assetType")
        }
    }

    @Test
    fun `GIVEN httpClient WHEN listVideos THEN returns Videos`() {
        // Given
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $CONTAINER_ID,
                    "description": "$DESCRIPTION",
                    "expirationDate": "$EXPIRATION_DATE",
                    "id": $ID,
                    "playbackUrl": "$VIDEO_URL",
                    "title": "$TITLE",
                    "type": "$videoType"
                }]}""".trimIndent()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: Videos = underTest.listVideos()

        // Then
        assertEquals(videos, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN containerId WHEN listVideos THEN returns Videos`() {
        // Given
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $CONTAINER_ID,
                    "description": "$DESCRIPTION",
                    "expirationDate": "$EXPIRATION_DATE",
                    "id": $ID,
                    "playbackUrl": "$VIDEO_URL",
                    "title": "$TITLE",
                    "type": "$videoType"
                }]}""".trimIndent()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: Videos = underTest.listVideos(CONTAINER_ID)

        // Then
        assertEquals(videos, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }

    @Test
    fun `GIVEN type WHEN listVideos THEN returns Videos`() {
        // Given
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $CONTAINER_ID,
                    "description": "$DESCRIPTION",
                    "expirationDate": "$EXPIRATION_DATE",
                    "id": $ID,
                    "playbackUrl": "$VIDEO_URL",
                    "title": "$TITLE",
                    "type": "$videoType"
                }]}""".trimIndent()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: Videos = underTest.listVideos(videoType)

        // Then
        assertEquals(videos, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["type"], "Query parameters should contain type")
        }
    }

    @Test
    fun `GIVEN containerId and type WHEN listVideos THEN returns Videos`() {
        // Given
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $CONTAINER_ID,
                    "description": "$DESCRIPTION",
                    "expirationDate": "$EXPIRATION_DATE",
                    "id": $ID,
                    "playbackUrl": "$VIDEO_URL",
                    "title": "$TITLE",
                    "type": "$videoType"
                }]}""".trimIndent()
            )
        )
        val underTest = VideoRepository(httpClient(mockEngine, json()))

        // When
        val actual: Videos = underTest.listVideos(CONTAINER_ID, videoType)

        // Then
        assertEquals(videos, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
            assertNotNull(it.url.parameters["type"], "Query parameters should contain type")
        }
    }

    /**
     * Get a [MockEngine] which returns 3 501 responses.
     */
    private fun getMockEngine(): MockEngine {
        return MockEngine {
            respond(
                content = ByteReadChannel(""),
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                status = HttpStatusCode.NotImplemented
            )
        }
    }

    /**
     * Get a [MockEngine] which returns a 200 with the provided content.
     */
    private fun getMockEngine(content: ByteReadChannel): MockEngine {
        return MockEngine {
            respond(
                content = content,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                status = HttpStatusCode.OK
            )
        }
    }
}
