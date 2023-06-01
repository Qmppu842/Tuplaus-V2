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
        val winnings =
            chooseCard(isItBig, bet)

        PlayerController.updatePlayerBalance(player.identity, bet * winnings)

        return GameEvent(playerIdentity = player.identity, bet = bet, isPlayerChoiceBig = isItBig, winnings = winnings)
    }

    private fun chooseCard(isItBig: Boolean, bet: Int): Int {
        val result = RandomHolder.rand(1, 14)
        val winnings =
            if ((result < 7 && !isItBig) || (result > 7 && isItBig)) {
                bet * 2
            } else {
                -bet
            }
        return winnings
    }


}