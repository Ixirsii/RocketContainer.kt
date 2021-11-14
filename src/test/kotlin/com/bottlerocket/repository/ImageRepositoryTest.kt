package com.bottlerocket.repository

import com.bottlerocket.data.imageService.Images
import com.bottlerocket.module.httpClient
import com.bottlerocket.module.json
import com.bottlerocket.util.CONTAINER_ID
import com.bottlerocket.util.ID
import com.bottlerocket.util.IMAGE_NAME
import com.bottlerocket.util.IMAGE_URL
import com.bottlerocket.util.images
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ImageRepositoryTest {

    private val mockEngine = MockEngine {
        respond(
            content = ByteReadChannel(
                """{"images": [{
                    "containerId": $CONTAINER_ID,
                    "id": $ID,
                    "name": "$IMAGE_NAME",
                    "url": "$IMAGE_URL"
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
        // When
        val actual: Images = underTest.listImages()

        // Then
        assertEquals(images, actual, "Images should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN containerId WHEN listImages THEN returns Images`() {
        // When
        val actual: Images = underTest.listImages(CONTAINER_ID)

        // Then
        assertEquals(images, actual, "Images should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }
}
