package com.bottlerocket.repository

import com.bottlerocket.data.advertisementService.Advertisements
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AdvertisementRepositoryIntegrationTest {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private lateinit var underTest: AdvertisementRepository

    @BeforeEach
    fun setup() {
        underTest = AdvertisementRepository(client)
    }

    @Test
    fun `GIVEN client WHEN listAdvertisements THEN returns advertisements`() {
        // When
        val actual: Advertisements = underTest.listAdvertisements()

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.advertisements.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN containerId WHEN listAdvertisements THEN returns advertisements`() {
        // Given
        val containerId = 27

        // When
        val actual: Advertisements = underTest.listAdvertisements(containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.advertisements.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN invalid containerId WHEN listAdvertisements THEN returns empty advertisements`() {
        // Given
        val containerId = 31

        // When
        val actual: Advertisements = underTest.listAdvertisements(containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue(actual.advertisements.isEmpty(), "Response should be empty")
    }
}
