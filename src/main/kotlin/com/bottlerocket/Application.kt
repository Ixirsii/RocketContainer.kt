package com.bottlerocket

import com.bottlerocket.module.responseService
import com.bottlerocket.service.ResponseService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.engine.cio.CIO
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json(com.bottlerocket.module.json())
    }

    registerContainerRoutes()
}

fun Application.registerContainerRoutes() {
    routing {
        containerRouting()
    }
}

fun Route.containerRouting() {
    val responseService: ResponseService = responseService(CIO.create())

    route("/containers") {
        get {
            responseService.listContainers(call)
        }
        route("{containerId}") {
            get {
                val containerId: Int = call.parameters["containerId"]?.toInt() ?: return@get call.respondText(
                    "Missing or malformed containerId",
                    status = HttpStatusCode.BadRequest
                )

                responseService.getContainer(containerId, call)
            }
            get("ads") {
                val containerId: Int = call.parameters["containerId"]?.toInt() ?: return@get call.respondText(
                    "Missing or malformed containerId",
                    status = HttpStatusCode.BadRequest
                )

                responseService.getAdvertisements(containerId, call)
            }
            get("images") {
                val containerId: Int = call.parameters["containerId"]?.toInt() ?: return@get call.respondText(
                    "Missing or malformed containerId",
                    status = HttpStatusCode.BadRequest
                )

            }
            get("videos") {
                val containerId: Int = call.parameters["containerId"]?.toInt() ?: return@get call.respondText(
                    "Missing or malformed containerId",
                    status = HttpStatusCode.BadRequest
                )

            }
        }

    }
}
