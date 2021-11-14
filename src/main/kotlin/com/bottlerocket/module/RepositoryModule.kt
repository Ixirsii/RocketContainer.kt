package com.bottlerocket.module

import com.bottlerocket.repository.AdvertisementRepository
import com.bottlerocket.repository.ContainerRepository
import com.bottlerocket.repository.ImageRepository
import com.bottlerocket.repository.VideoRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

/**
 * [ContainerRepository] provider.
 *
 * @param engine [HttpClientEngine].
 * @return [ContainerRepository].
 */
fun containerRepository(engine: HttpClientEngine): ContainerRepository {
    val client: HttpClient = httpClient(engine)

    return ContainerRepository(
        advertisementRepository = AdvertisementRepository(client),
        imageRepository = ImageRepository(client),
        videoRepository = VideoRepository(client)
    )
}
