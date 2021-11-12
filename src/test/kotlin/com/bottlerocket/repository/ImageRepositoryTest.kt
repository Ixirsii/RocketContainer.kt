package com.bottlerocket.repository

import com.bottlerocket.data.imageService.Image
import com.bottlerocket.data.imageService.Images
import com.bottlerocket.module.httpClient
import com.bottlerocket.module.json
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class ImageRepositoryTest {
    private val containerId = 1
    private val id = 1
    private val name = "Image"
    private val url = "https://images.google.com"

    private val mockEngine = MockEngine {
        respond(
            content = ByteReadChannel(
                """{"images": [{
                    "containerId": $containerId,
                    "id": $id,
                    "name": "$name",
                    "url": "$url"
                }]}""".trimIndent()
            ),
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            status = HttpStatusCode.OK
        )
    }
    private val client: HttpClient = httpClient(mockEngine, json())

    private lateinit var underTest: ImageRepository

    @BeforeEach
    fun setup() {
        underTest = ImageRepository(client)
    }

    @Test
    fun `GIVEN httpClient WHEN listImages THEN returns Images`() {
        // Given
        val expected = Images(
            images = listOf(Image(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Images = underTest.listImages()

        // Then
        assertEquals(expected, actual, "Images should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN containerId WHEN listImages THEN returns Images`() {
        // Given
        val expected = Images(
            images = listOf(Image(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Images = underTest.listImages(containerId)

        // Then
        assertEquals(expected, actual, "Images should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }
}
