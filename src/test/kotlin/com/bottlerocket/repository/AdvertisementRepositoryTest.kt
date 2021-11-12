package com.bottlerocket.repository

import com.bottlerocket.data.advertisementService.Advertisement
import com.bottlerocket.data.advertisementService.Advertisements
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

internal class AdvertisementRepositoryTest {
    private val containerId = 1
    private val id = 1
    private val name = "Advertisement"
    private val url = "https://www.google.com"

    private val mockEngine = MockEngine {
        respond(
            content = ByteReadChannel(
                """{"advertisements": [{
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

    private lateinit var underTest: AdvertisementRepository

    @BeforeEach
    fun setup() {
        underTest = AdvertisementRepository(client)
    }

    @Test
    fun `GIVEN httpClient WHEN listAdvertisements THEN returns Advertisements`() {
        // Given
        val expected = Advertisements(
            advertisements = listOf(Advertisement(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Advertisements = underTest.listAdvertisements()

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN container ID WHEN listAdvertisements THEN returns Advertisements`() {
        // Given
        val expected = Advertisements(
            advertisements = listOf(Advertisement(containerId = containerId, id = id, name = name, url = url))
        )

        // When
        val actual: Advertisements = underTest.listAdvertisements(containerId)

        // Then
        assertEquals(expected, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }
}
