package com.bottlerocket.module

import com.bottlerocket.service.ContainerService
import com.bottlerocket.service.ResponseService
import io.ktor.client.engine.HttpClientEngine

/**
 * [ContainerService] provider.
 *
 * @param engine [HttpClientEngine].
 * @return [ContainerService].
 */
fun containerService(engine: HttpClientEngine): ContainerService {
    return ContainerService(containerRepository(engine))
}

/**
 * [ResponseService] provider.
 *
 * @param engine [HttpClientEngine].
 * @return [ResponseService].
 */
fun responseService(engine: HttpClientEngine): ResponseService {
    return ResponseService(containerService(engine))
}
