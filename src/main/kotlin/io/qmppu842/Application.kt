package io.qmppu842

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.qmppu842.plugins.configureRouting
import kotlin.reflect.typeOf

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
//            println("cause = ${cause}")
            val split = cause.toString().split(":")
//            println("split.first() = ${split.first()}")
//            println("split.last() = ${split.last()}")

            try {
                val code = HttpStatusCode(split[1].trim().toInt(), split.last())
                println("code = ${code}")
                call.respond(code)
            } catch (e: Exception) {
                println(e.message)
                call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            }
            if (split.size > 2) {
            } else {
            }
        }
    }
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}
