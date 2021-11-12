package com.bottlerocket.repository

import com.bottlerocket.data.imageService.Images
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Tag("integration")
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
        Assertions.assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.images.isEmpty() }
    }

    @Test
    fun `GIVEN containerId WHEN listImages THEN returns images`() {
        // Given
        val containerId = 17

        // When
        val actual: Images = underTest.listImages(containerId)

        // Then
        Assertions.assertNotNull(actual, "Response should not be null")
        assertFalse("Response should not be empty") { actual.images.isEmpty() }
    }

    @Test
    fun `GIVEN invalid containerId WHEN listImages THEN returns empty images`() {
        // Given
        val containerId = 31

        // When
        val actual: Images = underTest.listImages(containerId)

        // Then
        Assertions.assertNotNull(actual, "Response should not be null")
        assertTrue("Response should be empty") { actual.images.isEmpty() }
    }
}
