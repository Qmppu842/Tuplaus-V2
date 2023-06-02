package io.qmppu842

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.qmppu842.plugins.configureRouting

fun main(args: Array<String>): Unit  = EngineMain.main(args)

fun Application.module() {
    install(StatusPages){
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    configureRouting()
}
