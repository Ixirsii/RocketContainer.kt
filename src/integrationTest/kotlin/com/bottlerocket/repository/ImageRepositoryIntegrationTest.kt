package com.bottlerocket.repository

import com.bottlerocket.data.imageService.Images
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

internal class ImageRepositoryIntegrationTest {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private lateinit var underTest: ImageRepository

    @BeforeEach
    fun setup() {
        underTest = ImageRepository(client)
    }

    @Test
    fun `GIVEN client WHEN listImages THEN returns images`() {
        // When
        val actual: Images = underTest.listImages()

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.images.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN containerId WHEN listImages THEN returns images`() {
        // Given
        val containerId = 17

        // When
        val actual: Images = underTest.listImages(containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertFalse(actual.images.isEmpty(), "Response should not be empty")
    }

    @Test
    fun `GIVEN invalid containerId WHEN listImages THEN returns empty images`() {
        // Given
        val containerId = 31

        // When
        val actual: Images = underTest.listImages(containerId)

        // Then
        assertNotNull(actual, "Response should not be null")
        assertTrue(actual.images.isEmpty(), "Response should be empty")
    }
}
