package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/{home-id}/temperature") {
            if (call.parameters["home-id"] == "1") call.respondText { "36.6" }
            else call.respondText("ваш дом не найден. вы бездомный?", status = HttpStatusCode.NotFound)
        }

        post("/home/{home-id}/led/{led-id}") {
            if (call.parameters["home-id"] == "1") {
                if (call.parameters["led-id"] == "1") {
                    call.respondText { "OK" }
                }
            } else call.respondText("ваш дом не найден. вы бездомный?", status = HttpStatusCode.NotFound)
        }
    }
}