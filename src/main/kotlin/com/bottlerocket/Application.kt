package com.bottlerocket

import com.bottlerocket.module.httpClient
import com.bottlerocket.module.json
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.engine.cio.CIO
import io.ktor.features.ContentNegotiation
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val json = json()
    val client = httpClient(CIO.create(), json)
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(json)
        }
        routing {
            get("/") {
                call.respondText("Hello world!")
            }
            route("/containers") {
                get {

                }
                route("{containerId}") {
                    get {

                    }
                    get("ads") {

                    }
                    get("images") {

                    }
                    get("videos") {

                    }
                }

            }
        }
    }.start(wait = true)
}
