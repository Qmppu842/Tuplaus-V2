package io.qmppu842

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.qmppu842.plugins.*

fun main(args: Array<String>): Unit  = EngineMain.main(args)

fun Application.module() {
    configureRouting()
}
