package com.ryanporterfield.bottlerocket

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json

fun main() {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }
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
