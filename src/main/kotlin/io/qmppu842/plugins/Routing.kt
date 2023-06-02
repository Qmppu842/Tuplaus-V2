package io.qmppu842.plugins

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.qmppu842.game.GameController
import io.qmppu842.player.PlayerController
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/newPlayer") {
            val requestBody = call.receiveText()
            val json = Json.parseToJsonElement(requestBody).jsonObject
            if (json["name"] == null || json["balance"] == null) {
                throw Exception("400:Missing information")
            } else {
                val player = PlayerController.newPlayer(json["name"].toString(), json["balance"].toString().toInt())
                call.respondText(player.identity, status = HttpStatusCode.Created)
            }
        }
        post("/updateBalance") {
            val requestBody = call.receiveText()
            val json = Json.parseToJsonElement(requestBody).jsonObject
            if (json["identity"] == null || json["balanceChange"] == null) {
                throw Exception("400:Missing information")
            } else {
                val balance = PlayerController.updatePlayerBalance(
                    json["identity"].toString(), json["balanceChange"].toString().toInt()
                )
                call.respondText(balance.toString(), status = HttpStatusCode.Accepted)
            }
        }
        post("/playGame") {
            val requestBody = call.receiveText()
            val json = Json.parseToJsonElement(requestBody).jsonObject
            if (json["identity"] == null || json["playersBet"] == null || json["isItBig"] == null) {
                throw Exception("400:Missing information")
            } else {
                val ge = GameController.playGame(
                    json["identity"].toString(),
                    json["playersBet"].toString().toInt(),
                    json["isItBig"].toString().toBooleanStrict()
                )
                call.respond(ge)
            }
        }
        post("/combo") {
            val requestBody = call.receiveText()
            val json = Json.parseToJsonElement(requestBody).jsonObject
            if (json["identity"] == null || json["isItBig"] == null) {
                throw Exception("400:Missing information")
            } else {
                val ge = GameController.playGame(
                    json["identity"].toString(), null, json["isItBig"].toString().toBooleanStrict()
                )
                call.respond(ge)
            }
        }
    }
}
