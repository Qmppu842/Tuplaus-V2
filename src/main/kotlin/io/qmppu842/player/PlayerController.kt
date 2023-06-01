package io.qmppu842.player

import java.util.UUID

object PlayerController {
    val players = HashMap<String, PlayerData>(100)

    fun newPlayer(name: String, balance: Int = 10000): PlayerData? = if (balance > 0) {
        val player = PlayerData(UUID.randomUUID().toString(), name, balance)
        players[player.identity] = player
        player
    } else {
        null
    }

    fun getPlayer(identity: String): PlayerData? {
        return players[identity]
    }
}

