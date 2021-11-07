package com.ryanporterfield.bottlerocket

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
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
