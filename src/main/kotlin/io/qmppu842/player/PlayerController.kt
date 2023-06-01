package io.qmppu842.player

import java.util.UUID

object PlayerController {
    private val players = HashMap<String, PlayerData>(100)

    fun newPlayer(name: String, balance: Int = 10000): PlayerData {
        if (balance < 0) throw Exception("Bad balance")

        val player = PlayerData(UUID.randomUUID().toString(), name, balance)
        players[player.identity] = player
        return player
    }

    fun getPlayer(identity: String): PlayerData = players[identity] ?: throw Exception("No player found")

    fun getAllPlayers(): List<PlayerData> {
        return players.values.toList()
    }

    fun updatePlayerBalance(identity: String, winnings: Int) {
        val player = getPlayer(identity)

        if (player.balance + winnings < 0) throw Exception("Bad balance")

        player.balance += winnings
        players[identity] = player
    }
}

