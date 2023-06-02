package io.qmppu842.player


import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlayerApplicationTest {
    val missingInfo = HttpStatusCode(400, "Missing information")

    @Test
    fun addPlayer() = testApplication {
        val playerName = "kala"
        val playerBalance = 99999
        val response = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"name\": $playerName,\n" +
                        "  \"balance\": $playerBalance\n" +
                        "}"
            )
        }
        val bodyText = response.bodyAsText()
        println("bodyText = ${bodyText}")
        assertEquals(36, bodyText.length)
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun addBrokenPayer() = testApplication {
        val playerName = "kala"
        val playerBalance = 99999

//        Response 1
        val response = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"balance\": $playerBalance\n" +
                        "}"
            )
        }
        assertEquals(missingInfo, response.status)


//        Response 2
        val response2 = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"name\": $playerName\n" +
                        "}"
            )
        }
        assertEquals(missingInfo, response2.status)

//        Reponse 3
        val response3 = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.InternalServerError, response3.status)
    }

    @Test
    fun updateBalance() = testApplication {
        val playerName = "kala"
        val playerBalance = 99999
        val response = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"name\": $playerName,\n" +
                        "  \"balance\": $playerBalance\n" +
                        "}"
            )
        }
        assertEquals(36, response.bodyAsText().length)
        assertEquals(HttpStatusCode.Created, response.status)
        val playerIdent = response.bodyAsText()


        val response2 = client.post("/updateBalance") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${playerIdent},\n" +
                        "  \"balanceChange\": 1\n" +
                        "}"
            )
        }


        assertEquals(100000, response2.bodyAsText().toInt())
        assertEquals(HttpStatusCode.Accepted, response2.status)
    }


    @Test
    fun updateNegBalance() = testApplication {
        val playerName = "kala"
        val playerBalance = 199
        val response = client.post("/newPlayer") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"name\": $playerName,\n" +
                        "  \"balance\": $playerBalance\n" +
                        "}"
            )
        }
        assertEquals(36, response.bodyAsText().length)
        assertEquals(HttpStatusCode.Created, response.status)

        val playerIdent = response.bodyAsText()

//        Update 1
        val response2 = client.post("/updateBalance") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${playerIdent},\n" +
                        "  \"balanceChange\": -100\n" +
                        "}"
            )
        }

        assertEquals(99, response2.bodyAsText().toInt())
        assertEquals(HttpStatusCode.Accepted, response2.status)


//        Update 2
        val response3 = client.post("/updateBalance") {
            contentType(ContentType.Application.Json)
            setBody(
                "{\n" +
                        "  \"identity\": ${playerIdent},\n" +
                        "  \"balanceChange\": -100\n" +
                        "}"
            )
        }

        assertEquals(HttpStatusCode(400, "Bad balance"), response3.status)
    }
}
