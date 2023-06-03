package io.qmppu842.player

import java.util.*

object PlayerController {
    private val players = HashMap<String, PlayerData>(100)

    fun newPlayer(name: String, balance: Int = 10000): PlayerData {
        if (balance < 0) throw Exception("400:Bad balance")

        val player = PlayerData(UUID.randomUUID().toString(), name, balance)
        players[player.identity] = player
        printPlayers()
        return player
    }

    fun getPlayerByIdentity(identity: String): PlayerData {
//        printPlayers()
//
//        println("identity = ${identity}")
        return players[identity] ?: throw Exception("500:No player found")
    }

    fun getAllPlayers(): List<PlayerData> {
        return players.values.toList()
    }

    fun updatePlayerBalance(identity: String, balanceChange: Int): Int {
        val player = getPlayerByIdentity(identity)
        if (player.balance + balanceChange < 0) throw Exception("400:Bad balance")

        player.balance += balanceChange
        players[identity] = player
        return player.balance
    }

    fun printPlayers() {
        println("Players are here:")
       players.values.forEach { println(it) }
    }
}

