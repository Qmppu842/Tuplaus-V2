package io.qmppu842.game


import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.qmppu842.RandomHolder
import io.qmppu842.newPerson
import io.qmppu842.player.PlayerData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GameApplicationTest {
    val missingInfo = HttpStatusCode(400, "Missing information")

    private lateinit var player: PlayerData
    private val hiLoQueue = LinkedList<Boolean>()

    @BeforeEach
    fun setUp() {
        RandomHolder.setseed(10) //8, 4, 3, 8, 8, 12, 10, 3, 13, 2, 13, 5
        hiLoQueue.offer(true)
        hiLoQueue.offer(false)
        hiLoQueue.offer(false)
        hiLoQueue.offer(true)
        hiLoQueue.offer(true)
        hiLoQueue.offer(true)
        hiLoQueue.offer(true)//num 10, game 7
        hiLoQueue.offer(false)
        hiLoQueue.offer(true)
        hiLoQueue.offer(false)
        hiLoQueue.offer(true)
        hiLoQueue.offer(false)//num 5, game 12

    }

    private suspend fun ApplicationTestBuilder.makePlayer() {
        val playerName = newPerson()
        val playerBalance = 10000
        val response = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"name\": \"$playerName\",\n" +
                        "  \"balance\": $playerBalance\n" +
                        "}"
            )
        }
        val ident = response.bodyAsText()
        player = PlayerData(ident, playerName, playerBalance)
    }

    @Test
    fun playSomeGames() = testApplication {
        makePlayer()

        var betAmount = 399
        val response = client.post("/playGame") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": \"${player.identity}\",\n" +
                        "  \"playersBet\": $betAmount,\n" +
                        "  \"isItBig\": ${hiLoQueue.poll()}\n" +
                        "}"
            )
        }
        println("response = ${response.bodyAsText()}")
        val ge = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        assertEquals(betAmount, ge["winnings"].toString().toInt())
        assertEquals(HttpStatusCode.OK, response.status)

//
////        Game 2
//        betAmount = 20
//        val response2 = client.post("/playGame") {
//            contentType(ContentType.Application.Json)
//            setBody(
//                "{\n" +
//                        "  \"identity\": ${player.identity},\n" +
//                        "  \"playersBet\": $betAmount,\n" +
//                        "  \"isItBig\": ${hiLoQueue.poll()}\n" +
//                        "}"
//            )
//        }
//        val ge2: GameEvent = response.body()
//
//        assertEquals(betAmount, ge2.winnings)
//        assertEquals(HttpStatusCode.OK, response2.status)
//
//
//        //        Game 3
//        betAmount = 3
//        val response3 = client.post("/playGame") {
//            contentType(ContentType.Application.Json)
//            setBody(
//                "{\n" +
//                        "  \"identity\": ${player.identity},\n" +
//                        "  \"playersBet\": $betAmount,\n" +
//                        "  \"isItBig\": ${!hiLoQueue.poll()}\n" +
//                        "}"
//            )
//        }
//        val ge3: GameEvent = response.body()
//
//        assertEquals(-betAmount, ge3.winnings)
//        assertEquals(HttpStatusCode.OK, response3.status)
    }



    @Test
    fun playSomeBrokenGames() = testApplication {
        var betAmount = 399
        val response = client.post("/playGame") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"playersBet\": $betAmount,\n" +
                        "  \"isItBig\": ${hiLoQueue.poll()}\n" +
                        "}"
            )
        }
        assertEquals(missingInfo, response.status)


//        Game 2
        betAmount = 20
        val response2 = client.post("/playGame") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${player.identity},\n" +
                        "  \"playersBet\": $betAmount\n" +
                        "}"
            )
        }
        assertEquals(missingInfo, response2.status)


        //        Game 3
        betAmount = 888
        val response3 = client.post("/playGame") {
            contentType(ContentType.Application.Atom)
        }
        assertEquals(missingInfo, response3.status)
    }

    @Test
    fun playSomeComboGames() = testApplication {
        makePlayer()

        var betAmount = 399
        val response = client.post("/combo") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${player.identity},\n" +
                        "  \"playersBet\": $betAmount,\n" +
                        "  \"isItBig\": ${hiLoQueue.poll()}\n" +
                        "}"
            )
        }
        val ge: GameEvent = response.body()

        assertEquals(betAmount, ge.winnings)
        assertEquals(HttpStatusCode.OK, response.status)


//        Game 2
        betAmount *= 2
        val response2 = client.post("/combo") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${player.identity},\n" +
                        "  \"isItBig\": ${hiLoQueue.poll()}\n" +
                        "}"
            )
        }
        val ge2: GameEvent = response.body()

        assertEquals(ge.id, ge2.comboId)
        assertEquals(HttpStatusCode.OK, response2.status)


        //        Game 3
        betAmount *= 2
        val response3 = client.post("/combo") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${player.identity},\n" +
                        "  \"isItBig\": ${!hiLoQueue.poll()}\n" +
                        "}"
            )
        }
        val ge3: GameEvent = response.body()

        assertEquals(ge2.id, ge3.comboId)
        assertEquals(HttpStatusCode.OK, response3.status)

        betAmount *= 2
        val response4 = client.post("/combo") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${player.identity},\n" +
                        "  \"isItBig\": ${hiLoQueue.poll()}\n" +
                        "}"
            )
        }
//        val ge4: GameEvent = response.body()

//        assertEquals(-betAmount, ge4.winnings)
        assertEquals(HttpStatusCode.OK, response4.status)
    }

    @Test
    fun playSomeBrokenComboGames() = testApplication {
        val response = client.post("/combo") {
            contentType(ContentType.Application.Json)
            setBody(
                "{}"
            )
        }
        assertEquals(missingInfo, response.status)
    }
}
