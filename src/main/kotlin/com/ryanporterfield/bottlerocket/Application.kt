package com.ryanporterfield.bottlerocket

import com.ryanporterfield.bottlerocket.module.httpClient
import com.ryanporterfield.bottlerocket.module.json
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.features.ContentNegotiation
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json

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
