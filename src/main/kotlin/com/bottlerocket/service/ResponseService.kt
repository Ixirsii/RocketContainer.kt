package com.bottlerocket.service

import com.bottlerocket.Logging
import com.bottlerocket.LoggingImpl
import io.ktor.application.ApplicationCall
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText

/**
 * Service/business layer for responding to server requests.
 */
class ResponseService(
    /** Container service for getting data from cache or dependencies. */
    private val containerService: ContainerService
) : Logging by LoggingImpl<ResponseService>() {

    /**
     * Respond to GET /containers/{containerId}/ads.
     *
     * @param containerId container ID from path.
     * @param call Application call for getting parameters and responding to the request.
     */
    suspend fun getAdvertisements(containerId: Int, call: ApplicationCall) {
        log.info("Responding to call GET /containers/{}/ads", containerId)

        respond(call) { containerService.getAdvertisements(containerId) }
    }

    /**
     * Respond to GET /containers/{containerId}.
     *
     * @param containerId container ID from path.
     * @param call Application call for getting parameters and responding to the request.
     */
    suspend fun getContainer(containerId: Int, call: ApplicationCall) {
        log.info("Responding to call GET /containers/{}", containerId)

        respond(call) { containerService.getContainer(containerId) }
    }

    /**
     * Respond to GET /containers/{containerId}/images.
     *
     * @param containerId container ID from path.
     * @param call Application call for getting parameters and responding to the request.
     */
    suspend fun getImages(containerId: Int, call: ApplicationCall) {
        log.info("Responding to call GET /containers/{}/images", containerId)

        respond(call) { containerService.getImages(containerId) }
    }

    /**
     * Respond to GET /containers/{containerId}/videos.
     *
     * @param containerId container ID from path.
     * @param call Application call for getting parameters and responding to the request.
     */
    suspend fun getVideos(containerId: Int, call: ApplicationCall) {
        log.info("Responding to call GET /containers/{}/videos", containerId)

        respond(call) { containerService.getVideos(containerId) }
    }

    /**
     * Respond to GET /containers.
     *
     * @param call Application call for getting parameters and responding to the request.
     */
    suspend fun listContainers(call: ApplicationCall) {
        log.info("Responding to call GET /containers")

        try {
            call.respond(containerService.listContainers())
        } catch (e: ClientRequestException) {
            call.respondText(
                "Bad request: ${e.message}",
                status = HttpStatusCode.BadRequest
            )
        } catch (e: RedirectResponseException) {
            call.respondText(
                "Service configured incorrectly. Client returned 3xx: ${e.message}",
                status = HttpStatusCode.InternalServerError
            )
        } catch (e: ServerResponseException) {
            call.respondText(
                "Exceeded maximum retries while calling dependent service: ${e.message}",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    /* ********************************************************************************************************** *
     *                                             Private utility functions                                      *
     * ********************************************************************************************************** */

    /**
     * Try to respond with data but respond with text on dependency failure.
     *
     * @param call Application call for getting parameters and responding to the request.
     * @param block
     */
    private suspend fun respond(call: ApplicationCall, block: () -> Any) {
        try {
            call.respond(block())

            log.info("Responded successfully")
        } catch (e: ClientRequestException) {
            log.error("Caught ClientRequestException attempting to respond to request", e)

            call.respondText(
                "Bad request: ${e.message}",
                status = HttpStatusCode.NotFound
            )
        } catch (e: RedirectResponseException) {
            log.error("Caught RedirectResponseException attempting to respond to request", e)

            call.respondText(
                "Service configured incorrectly. Client returned 3xx: ${e.message}",
                status = HttpStatusCode.InternalServerError
            )
        } catch (e: ServerResponseException) {
            log.error("Caught ServerResponseException attempting to respond to request", e)

            call.respondText(
                "Exceeded maximum retries while calling dependent service: ${e.message}",
                status = HttpStatusCode.NotFound
            )
        } catch (e: IllegalArgumentException) {
            log.error("Caught IllegalArgumentException attempting to respond to request", e)

            call.respondText(
                "Not Found: ${e.message}",
                status = HttpStatusCode.NotFound
            )
        }
    }
}
