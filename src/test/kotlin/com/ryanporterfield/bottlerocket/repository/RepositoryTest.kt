package com.ryanporterfield.bottlerocket.repository

import com.ryanporterfield.bottlerocket.data.AssetType
import com.ryanporterfield.bottlerocket.data.VideoType
import com.ryanporterfield.bottlerocket.data.advertisementService.Advertisement
import com.ryanporterfield.bottlerocket.data.advertisementService.Advertisements
import com.ryanporterfield.bottlerocket.data.imageService.Image
import com.ryanporterfield.bottlerocket.data.imageService.Images
import com.ryanporterfield.bottlerocket.data.videoService.AssetReference
import com.ryanporterfield.bottlerocket.data.videoService.Video
import com.ryanporterfield.bottlerocket.data.videoService.VideoAssets
import com.ryanporterfield.bottlerocket.data.videoService.Videos
import com.ryanporterfield.bottlerocket.module.httpClient
import com.ryanporterfield.bottlerocket.module.json
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.ServerResponseException
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.math.exp
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
internal class RepositoryTest {

    @Test
    fun `GIVEN videoId WHEN getVideo THEN returns Video`() {
        // Given
        val containerId = 1
        val description = "Video description"
        val expirationDate = "1970-01-01"
        val id = 1
        val playbackUrl = "https://youtube.com/"
        val title = "Test video"
        val type = VideoType.CLIP
        val mockEngine = getMockEngine(
            ByteReadChannel(
                """{
                    "containerId": $containerId,
                    "description": "$description",
                    "expirationDate": "$expirationDate",
                    "id": $id,
                    "playbackUrl": "$playbackUrl",
                    "title": "$title",
                    "type": "$type"
                }""".trimMargin()
            )
        )
        val client = httpClient(mockEngine, json())
        val videoId = 1301
        val expected = Video(
            containerId = containerId,
            description = description,
            expirationDate = expirationDate,
            id = id,
            playbackUrl = playbackUrl,
            title = title,
            type = type
        )

        // When
        val actual: Video = getVideo(client, videoId)

        // Then
        assertEquals(expected, actual, "Video should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 requests")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().endsWith(videoId.toString()) }
        }
    }

    @Test
    fun `GIVEN invalidRequest WHEN getVideo THEN throws ServerResponseException`() {
        // Given
        val mockEngine: MockEngine = getMockEngine()
        val client = httpClient(mockEngine, json())
        val videoId = 0

        // When
        assertThrows<ServerResponseException> { getVideo(client, videoId) }

        // Then
        assertEquals(3, mockEngine.requestHistory.size, "Should make 3 requests")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().endsWith(videoId.toString()) }
        }
    }

    @Test
    fun `GIVEN httpClient WHEN listAdvertisements THEN returns Advertisements`() {
        // Given
        val containerId = 1
        val id = 1
        val name = "Advertisement"
        val url = "https://www.google.com"
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"advertisements": [{
                    "containerId": $containerId,
                    "id": $id,
                    "name": "$name",
                    "url": "$url"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Advertisements(
            advertisements = listOf(Advertisement(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Advertisements = listAdvertisements(client)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN container ID WHEN listAdvertisements THEN returns Advertisements`() {
        // Given
        val containerId = 1
        val id = 1
        val name = "Advertisement"
        val url = "https://www.google.com"
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"advertisements": [{
                    "containerId": $containerId,
                    "id": $id,
                    "name": "$name",
                    "url": "$url"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Advertisements(
            advertisements = listOf(Advertisement(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Advertisements = listAdvertisements(client, containerId)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }

    @Test
    fun `GIVEN video ID WHEN listAssetReferences THEN returns Advertisements`() {
        // Given
        val assetId = 1
        val assetType = AssetType.IMAGE
        val videoId = 1
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videoAssets": [{
                    "assetId": $assetId,
                    "assetType": "$assetType",
                    "videoId": "$videoId"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = VideoAssets(
            videoAssets = listOf(AssetReference(assetId = assetId, assetType = assetType, videoId = videoId))
        )

        // When
        val actual: VideoAssets = listAssetReferences(client, videoId)

        // Then
        assertEquals(expected, actual, "VideoAssets should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().contains(videoId.toString()) }
        }
    }

    @Test
    fun `GIVEN assetType ID WHEN listAssetReferences THEN returns Advertisements`() {
        // Given
        val assetId = 1
        val assetType = AssetType.IMAGE
        val videoId = 1
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videoAssets": [{
                    "assetId": $assetId,
                    "assetType": "$assetType",
                    "videoId": "$videoId"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = VideoAssets(
            videoAssets = listOf(AssetReference(assetId = assetId, assetType = assetType, videoId = videoId))
        )

        // When
        val actual: VideoAssets = listAssetReferences(client, videoId, assetType)

        // Then
        assertEquals(expected, actual, "VideoAssets should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertTrue("URL should contain video ID") { it.url.toString().contains(videoId.toString()) }
            assertNotNull(it.url.parameters["assetType"], "Query parameters should contain assetType")
        }
    }

    @Test
    fun `GIVEN httpClient WHEN listImages THEN returns Images`() {
        // Given
        val containerId = 1
        val id = 1
        val name = "Image"
        val url = "https://images.google.com"
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"images": [{
                    "containerId": $containerId,
                    "id": $id,
                    "name": "$name",
                    "url": "$url"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Images(
            images = listOf(Image(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Images = listImages(client)

        // Then
        assertEquals(expected, actual, "Images should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN containerId WHEN listImages THEN returns Images`() {
        // Given
        val containerId = 1
        val id = 1
        val name = "Image"
        val url = "https://images.google.com"
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"images": [{
                    "containerId": $containerId,
                    "id": $id,
                    "name": "$name",
                    "url": "$url"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Images(
            images = listOf(Image(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Images = listImages(client, containerId)

        // Then
        assertEquals(expected, actual, "Images should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }

    @Test
    fun `GIVEN httpClient WHEN listVideos THEN returns Images`() {
        // Given
        val containerId = 1
        val description = "Video description"
        val expirationDate = "1970-01-01"
        val id = 1
        val playbackUrl = "https://youtube.com/"
        val title = "Test video"
        val type = VideoType.CLIP
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $containerId,
                    "description": "$description",
                    "expirationDate": "$expirationDate",
                    "id": $id,
                    "playbackUrl": "$playbackUrl",
                    "title": "$title",
                    "type": "$type"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Videos(
            videos = listOf(
                Video(
                    containerId = containerId,
                    description = description,
                    expirationDate = expirationDate,
                    id = id,
                    playbackUrl = playbackUrl,
                    title = title,
                    type = type,
                )
            )
        )

        // When
        val actual: Videos = listVideos(client)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN containerId WHEN listVideos THEN returns Images`() {
        // Given
        val containerId = 1
        val description = "Video description"
        val expirationDate = "1970-01-01"
        val id = 1
        val playbackUrl = "https://youtube.com/"
        val title = "Test video"
        val type = VideoType.CLIP
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $containerId,
                    "description": "$description",
                    "expirationDate": "$expirationDate",
                    "id": $id,
                    "playbackUrl": "$playbackUrl",
                    "title": "$title",
                    "type": "$type"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Videos(
            videos = listOf(
                Video(
                    containerId = containerId,
                    description = description,
                    expirationDate = expirationDate,
                    id = id,
                    playbackUrl = playbackUrl,
                    title = title,
                    type = type,
                )
            )
        )

        // When
        val actual: Videos = listVideos(client, containerId)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }

    @Test
    fun `GIVEN type WHEN listVideos THEN returns Images`() {
        // Given
        val containerId = 1
        val description = "Video description"
        val expirationDate = "1970-01-01"
        val id = 1
        val playbackUrl = "https://youtube.com/"
        val title = "Test video"
        val type = VideoType.CLIP
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $containerId,
                    "description": "$description",
                    "expirationDate": "$expirationDate",
                    "id": $id,
                    "playbackUrl": "$playbackUrl",
                    "title": "$title",
                    "type": "$type"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Videos(
            videos = listOf(
                Video(
                    containerId = containerId,
                    description = description,
                    expirationDate = expirationDate,
                    id = id,
                    playbackUrl = playbackUrl,
                    title = title,
                    type = type,
                )
            )
        )

        // When
        val actual: Videos = listVideos(client, type)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["type"], "Query parameters should contain type")
        }
    }

    @Test
    fun `GIVEN containerId and type WHEN listVideos THEN returns Images`() {
        // Given
        val containerId = 1
        val description = "Video description"
        val expirationDate = "1970-01-01"
        val id = 1
        val playbackUrl = "https://youtube.com/"
        val title = "Test video"
        val type = VideoType.CLIP
        val mockEngine: MockEngine = getMockEngine(
            ByteReadChannel(
                """{"videos": [{
                    "containerId": $containerId,
                    "description": "$description",
                    "expirationDate": "$expirationDate",
                    "id": $id,
                    "playbackUrl": "$playbackUrl",
                    "title": "$title",
                    "type": "$type"
                }]}""".trimIndent()
            )
        )
        val client: HttpClient = httpClient(mockEngine, json())
        val expected = Videos(
            videos = listOf(
                Video(
                    containerId = containerId,
                    description = description,
                    expirationDate = expirationDate,
                    id = id,
                    playbackUrl = playbackUrl,
                    title = title,
                    type = type,
                )
            )
        )

        // When
        val actual: Videos = listVideos(client, containerId, type)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
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
