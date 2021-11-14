package com.bottlerocket.service

import com.bottlerocket.data.containerService.Advertisement
import com.bottlerocket.data.containerService.Container
import com.bottlerocket.data.containerService.Image
import com.bottlerocket.data.containerService.Video
import com.bottlerocket.repository.ContainerRepository
import com.bottlerocket.util.ID
import com.bottlerocket.util.containerAdvertisement
import com.bottlerocket.util.containerImage
import com.bottlerocket.util.containerVideo
import com.bottlerocket.util.fullContainer
import io.github.pavleprica.kotlin.cache.time.based.CustomTimeBasedCache
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class ContainerServiceTest {
    @MockK
    private lateinit var containerRepository: ContainerRepository

    @MockK
    private lateinit var cache: CustomTimeBasedCache<Int, Container>

    @InjectMockKs
    private lateinit var underTest: ContainerService

    @Test
    fun `GIVEN success WHEN listContainers THEN returns containers`() {
        // Given
        val expected: List<Container> = listOf(fullContainer)

        every { containerRepository.listContainers() } returns expected
        justRun { cache[any()] = any<Container>() }

        // When
        val actual: List<Container> = underTest.listContainers()

        // Then
        verifySequence {
            containerRepository.listContainers()
            cache[any()] = any<Container>()
        }

        assertEquals(expected, actual, "Container list should equal expected")
    }

    @Test
    fun `GIVEN containerId cached WHEN getAdvertisements THEN returns advertisements`() {
        // Given
        every { cache[any()] } returns Optional.of(fullContainer)

        // When
        val actual: List<Advertisement> = underTest.getAdvertisements(ID)

        // Then
        verifySequence {
            cache[any()]
        }

        assertEquals(listOf(containerAdvertisement), actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId not cached WHEN getAdvertisements THEN calls repository`() {
        // Given
        every { cache[any()] } returns Optional.empty()
        every { containerRepository.getContainer(any()) } returns fullContainer
        justRun { cache[any()] = any<Container>() }

        // When
        val actual: List<Advertisement> = underTest.getAdvertisements(ID)

        // Then
        verifySequence {
            cache[any()]
            containerRepository.getContainer(any())
            cache[any()] = any<Container>()
        }

        assertEquals(listOf(containerAdvertisement), actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId cached WHEN getContainer THEN returns container`() {
        // Given
        every { cache[any()] } returns Optional.of(fullContainer)

        // When
        val actual: Container = underTest.getContainer(ID)

        // Then
        verifySequence {
            cache[any()]
        }

        assertEquals(fullContainer, actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId not cached WHEN getContainer THEN calls repository`() {
        // Given
        every { cache[any()] } returns Optional.empty()
        every { containerRepository.getContainer(any()) } returns fullContainer
        justRun { cache[any()] = any<Container>() }

        // When
        val actual: Container = underTest.getContainer(ID)

        // Then
        verifySequence {
            cache[any()]
            containerRepository.getContainer(any())
            cache[any()] = any<Container>()
        }

        assertEquals(fullContainer, actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId cached WHEN getImages THEN returns images`() {
        // Given
        every { cache[any()] } returns Optional.of(fullContainer)

        // When
        val actual: List<Image> = underTest.getImages(ID)

        // Then
        verifySequence {
            cache[any()]
        }

        assertEquals(listOf(containerImage), actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId not cached WHEN getImages THEN calls repository`() {
        // Given
        every { cache[any()] } returns Optional.empty()
        every { containerRepository.getContainer(any()) } returns fullContainer
        justRun { cache[any()] = any<Container>() }

        // When
        val actual: List<Image> = underTest.getImages(ID)

        // Then
        verifySequence {
            cache[any()]
            containerRepository.getContainer(any())
            cache[any()] = any<Container>()
        }

        assertEquals(listOf(containerImage), actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId cached WHEN getVideos THEN returns videos`() {
        // Given
        every { cache[any()] } returns Optional.of(fullContainer)

        // When
        val actual: List<Video> = underTest.getVideos(ID)

        // Then
        verifySequence {
            cache[any()]
        }

        assertEquals(listOf(containerVideo), actual, "Container should equal expected")
    }

    @Test
    fun `GIVEN containerId not cached WHEN getVideos THEN calls repository`() {
        // Given
        every { cache[any()] } returns Optional.empty()
        every { containerRepository.getContainer(any()) } returns fullContainer
        justRun { cache[any()] = any<Container>() }

        // When
        val actual: List<Video> = underTest.getVideos(ID)

        // Then
        verifySequence {
            cache[any()]
            containerRepository.getContainer(any())
            cache[any()] = any<Container>()
        }

        assertEquals(listOf(containerVideo), actual, "Container should equal expected")
    }
}
