package io.qmppu842.game

import io.ktor.util.date.*
import io.qmppu842.RandomHolder
import io.qmppu842.player.PlayerController
import io.qmppu842.player.PlayerData
import kotlin.random.Random

object GameController {
    private val gameHistory = ArrayList<GameEvent>(100)


    fun playGame(player: PlayerData, bet: Int?, isItBig: Boolean): GameEvent {
        if (bet == null) {

            TODO("to implement the combo system")
        }
        if (player.balance - bet < 0) {
            throw Exception("Too big bet")
        }
        if (bet < 0) {
            throw Exception("Too small bet")
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
            winnings = winnings
        )
        gameHistory.add(ge)

        return ge
    }


    fun getAllGames(): List<GameEvent> {
        return gameHistory
    }

    fun getLastGameFrom(identity: String): GameEvent {
        return getAllGames().mapNotNull {
            if (it.playerIdentity == identity) {
                it
            } else {
                null
            }
        }.maxBy { it.timestamp; it.id }
        // huh, there actually shouldn't be any case where timestamps outperform id alone
    }


}