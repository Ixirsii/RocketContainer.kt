package com.bottlerocket.service

import com.bottlerocket.data.containerService.Container
import com.bottlerocket.repository.AdvertisementRepository
import com.bottlerocket.repository.ImageRepository
import com.bottlerocket.repository.VideoRepository
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ContainerServiceTest {
    @MockK
    private lateinit var advertisementRepository: AdvertisementRepository

    @MockK
    private lateinit var imageRepository: ImageRepository

    @MockK
    private lateinit var videoRepository: VideoRepository

    @InjectMockKs
    private lateinit var underTest: ContainerService

    @Test
    fun `GIVEN ads and images WHEN listContainers THEN returns containers`() {
        // Given
        every { advertisementRepository.listAdvertisements() } returns advertisements
        every { imageRepository.listImages() } returns images
        every { videoRepository.listVideos() } returns videos
        every { videoRepository.listAssetReferences(any()) } returns videoAssets

        // When
        val actual: List<Container> = underTest.listContainers()

        // Then
        verifySequence {
            advertisementRepository.listAdvertisements()
            imageRepository.listImages()
            videoRepository.listVideos()
            videoRepository.listAssetReferences(any())
        }

        assertEquals(listOf(fullContainer), actual, "Containers should equal expected")
    }

    @Test
    fun `GIVEN container ID WHEN getContainer THEN returns container`() {
        // Given
        every { advertisementRepository.listAdvertisements(any()) } returns advertisements
        every { imageRepository.listImages(any()) } returns images
        every { videoRepository.listVideos(any<Int>()) } returns videos
        every { videoRepository.listAssetReferences(any()) } returns videoAssets

        // When
        val actual: Container = underTest.getContainer(ID)

        // Then
        verifySequence {
            advertisementRepository.listAdvertisements(any())
            imageRepository.listImages(any())
            videoRepository.listVideos(any<Int>())
            videoRepository.listAssetReferences(any())
        }

        assertEquals(fullContainer, actual, "Container should equal expected")
    }
}
