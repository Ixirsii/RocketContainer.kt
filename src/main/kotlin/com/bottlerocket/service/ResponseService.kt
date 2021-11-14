package com.bottlerocket.service

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
) {

    suspend fun getContainer(containerId: Int, call: ApplicationCall) {
        try {
            call.respond(containerService.getContainer(containerId))
        } catch (e: ClientRequestException) {
            call.respondText(
                "Bad request: ${e.message}",
                status = HttpStatusCode.NotFound
            )
        } catch (e: RedirectResponseException) {
            call.respondText(
                "Service configured incorrectly. Client returned 3xx: ${e.message}",
                status = HttpStatusCode.InternalServerError
            )
        } catch (e: ServerResponseException) {
            call.respondText(
                "Exceeded maximum retries while calling dependent service: ${e.message}",
                status = HttpStatusCode.NotFound
            )
        }
    }

    /**
     * Respond to GET /containers.
     *
     * @param call Application call for getting parameters and responding to the request.
     */
    suspend fun listContainers(call: ApplicationCall) {
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
}
