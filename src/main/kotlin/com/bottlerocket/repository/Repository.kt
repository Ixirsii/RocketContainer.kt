package com.bottlerocket.repository

import com.bottlerocket.Logging
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

/**
 * Abstract repository class which provides exponential backoff and retry helper.
 */
abstract class Repository(val client: HttpClient) : Logging {
    /**
     * Make a get request with retries and exponential backoff.
     *
     * @param url Request URL.
     * @param block [HttpRequestBuilder] used to configure the request.
     * @return deserialized data from endpoint.
     * @throws RedirectResponseException if a 3xx response is returned on the maximum number of attempts.
     * @throws ClientRequestException if a 4xx response is returned.
     * @throws ServerResponseException if a 5xx response is returned on the maximum number of attempts.
     */
    @Throws(RedirectResponseException::class, ClientRequestException::class, ServerResponseException::class)
    suspend inline fun <reified T> exponentialBackoffAndRetry(
        url: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        val maxAttempts = 10

        // Start at 1 instead of 0 because the last attempt is outside the loop
        for (i in (1 until maxAttempts)) {
            try {
                return client.get(url, block)
            } catch (e: RedirectResponseException) {
                val backoff: Long = exponentialBackoff(i)
                log.warn("Caught RedirectResponseException calling {} on attempt {}. Waiting {}ms", url, i, backoff, e)
                delay(backoff)
            } catch (e: ServerResponseException) {
                val backoff: Long = exponentialBackoff(i)
                log.warn("Caught ServerResponseException calling {} on attempt {}. Waiting {}ms", url, i, backoff, e)
                delay(backoff)
            }
        }

        return client.get(url, block)
    }

    /* ********************************************************************************************************** *
     *                                             Private utility functions                                      *
     * ********************************************************************************************************** */

    /**
     * Get exponential backoff delay.
     *
     * @param attempt Attempt number.
     * @return exponential backoff delay.
     */
    @PublishedApi
    internal fun exponentialBackoff(attempt: Int): Long {
        val maxBackoff = 1_000L
        val exponent: Long = 2f.pow(attempt).toLong()
        val randomMillis: Long = Random.nextLong(0, 50)
        val backoff: Long = exponent + randomMillis

        return min(backoff, maxBackoff)
    }
}

