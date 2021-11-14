package com.bottlerocket.repository

import com.bottlerocket.data.advertisementService.Advertisements
import com.bottlerocket.module.httpClient
import com.bottlerocket.module.json
import com.bottlerocket.util.ADVERTISEMENT_NAME
import com.bottlerocket.util.ADVERTISEMENT_URL
import com.bottlerocket.util.CONTAINER_ID
import com.bottlerocket.util.ID
import com.bottlerocket.util.advertisements
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

internal class AdvertisementRepositoryTest {

    private val mockEngine = MockEngine {
        respond(
            content = ByteReadChannel(
                """{"advertisements": [{
                    "containerId": $CONTAINER_ID,
                    "id": $ID,
                    "name": "$ADVERTISEMENT_NAME",
                    "url": "$ADVERTISEMENT_URL"
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
        // When
        val actual: Advertisements = underTest.listAdvertisements()

        // Then
        assertEquals(advertisements, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
    }

    @Test
    fun `GIVEN container ID WHEN listAdvertisements THEN returns Advertisements`() {
        // When
        val actual: Advertisements = underTest.listAdvertisements(CONTAINER_ID)

        // Then
        assertEquals(advertisements, actual, "Advertisements should equal expected")
        assertEquals(1, mockEngine.requestHistory.size, "Should make 1 request")
        mockEngine.requestHistory.forEach {
            assertNotNull(it.url.parameters["containerId"], "Query parameters should contain containerId")
        }
    }
}
