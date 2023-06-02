package io.qmppu842.game

import io.qmppu842.RandomHolder
import io.qmppu842.player.PlayerController
import io.qmppu842.player.PlayerData

object GameController {


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

        return GameEvent(
            playerIdentity = player.identity,
            bet = bet,
            isPlayerChoiceBig = isItBig,
            endCard = result,
            winnings = winnings
        )
    }
}