package com.bottlerocket.module

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

/**
 * [HttpClient] provider.
 *
 * @param engine [HttpClientEngine].
 * @return [HttpClient].
 */
fun httpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json())
        }
    }
}

/**
 * [Json] provider.
 *
 * @return [Json].
 */
fun json(): Json {
    return Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    }
}
