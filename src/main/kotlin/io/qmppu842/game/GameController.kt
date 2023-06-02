package io.qmppu842.game

import io.ktor.util.date.*
import io.qmppu842.RandomHolder
import io.qmppu842.player.PlayerController
import io.qmppu842.player.PlayerData

object GameController {
    private val gameHistory = ArrayList<GameEvent>(100)


    fun playGame(playerIdent: String, playersBet: Int?, isItBig: Boolean): GameEvent {
        return playGame(PlayerController.getPlayerByIdentity(playerIdent), playersBet, isItBig)
    }

    fun playGame(player: PlayerData, playersBet: Int?, isItBig: Boolean): GameEvent {
        var bet = playersBet
        var comboId = -1
        if (bet == null) {
            val lastGame = getLastGameFrom(player.identity) ?: throw Exception("500:Player has yet to play any games")
            if (lastGame.winnings < 1) throw Exception("500:Try winning bigger to be able to combo")
            bet = lastGame.bet + lastGame.winnings
            comboId = lastGame.id
        }
        if (player.balance - bet < 0) {
            throw Exception("400:Too big bet")
        }
        if (bet < 0) {
            throw Exception("400:Too small bet")
        }

        val result = RandomHolder.rand(1, 14)
        val winMultiplier = if ((result < 7 && !isItBig) || (result > 7 && isItBig)) 1 else -1
        val winnings = bet * winMultiplier
//
        PlayerController.updatePlayerBalance(player.identity, winnings)

        val ge = GameEvent(
            id = gameHistory.size,
            playerIdentity = player.identity,
            timestamp = getTimeMillis(),
            bet = bet,
            isPlayerChoiceBig = isItBig,
            endCard = result,
            winnings = winnings,
            comboId = comboId
        )
        gameHistory.add(ge)

        return ge
    }


    fun getAllGames(): List<GameEvent> {
        return gameHistory
    }

    fun getLastGameFrom(identity: String): GameEvent? {
        return getAllGames().mapNotNull {
            if (it.playerIdentity == identity) {
                it
            } else {
                null
            }
        }.maxByOrNull { it.timestamp; it.id }
        // huh, there actually shouldn't be any case where timestamps outperforms id alone
    }

    fun printGames() {
        gameHistory.forEach { println(it) }
    }


}