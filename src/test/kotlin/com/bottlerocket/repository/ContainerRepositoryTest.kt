package com.bottlerocket.repository

import com.bottlerocket.data.containerService.Container
import com.bottlerocket.util.ID
import com.bottlerocket.util.advertisements
import com.bottlerocket.util.fullContainer
import com.bottlerocket.util.images
import com.bottlerocket.util.videoAssets
import com.bottlerocket.util.videos
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifySequence
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ContainerRepositoryTest {
    @MockK
    private lateinit var advertisementRepository: AdvertisementRepository

    @MockK
    private lateinit var imageRepository: ImageRepository

    @MockK
    private lateinit var videoRepository: VideoRepository

    @InjectMockKs
    private lateinit var underTest: ContainerRepository

    @Test
    fun `GIVEN ads and images WHEN listContainers THEN returns containers`() {
        // Given
        every { videoRepository.listVideos() } returns videos
        every { videoRepository.listAssetReferences(any()) } returns videoAssets
        every { advertisementRepository.listAdvertisements() } returns advertisements
        every { imageRepository.listImages() } returns images

        // When
        val actual: List<Container> = underTest.listContainers()

        // Then
        verifySequence {
            videoRepository.listVideos()
            videoRepository.listAssetReferences(any())
            advertisementRepository.listAdvertisements()
            imageRepository.listImages()
        }

        assertEquals(listOf(fullContainer), actual, "Containers should equal expected")
    }

    @Test
    fun `GIVEN container ID WHEN getContainer THEN returns container`() {
        // Given
        every { videoRepository.listVideos(any<Int>()) } returns videos
        every { videoRepository.listAssetReferences(any()) } returns videoAssets
        every { advertisementRepository.listAdvertisements(any()) } returns advertisements
        every { imageRepository.listImages(any()) } returns images

        // When
        val actual: Container = underTest.getContainer(ID)

        // Then
        verifySequence {
            videoRepository.listVideos(any<Int>())
            videoRepository.listAssetReferences(any())
            advertisementRepository.listAdvertisements(any())
            imageRepository.listImages(any())
        }

        assertEquals(fullContainer, actual, "Container should equal expected")
    }
}
