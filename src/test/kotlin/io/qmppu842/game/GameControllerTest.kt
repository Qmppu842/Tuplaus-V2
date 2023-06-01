package io.qmppu842.game

import io.qmppu842.RandomHolder
import io.qmppu842.newPerson
import io.qmppu842.player.PlayerController
import io.qmppu842.player.PlayerData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameControllerTest {

    lateinit var player: PlayerData

    @BeforeEach
    fun setUp() {
        RandomHolder.setseed(10) //1, 4, 6, 11, 9, 13, 3, 5, 3, 9, 8, 8
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun playSomeGames() {
        player = PlayerController.newPlayer(newPerson())

        //Game 1
        val gameEvent = GameController.playGame(player, 10, false)
        assertEquals(20, gameEvent.winnings, "Winnings don't match")
        var newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)


        //Game 2
        GameController.playGame(player, 10, false)
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10020, newPlayer.balance)


        //Game 3
        GameController.playGame(player, 10, true)
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)


        //Game 4
        GameController.playGame(player, 10, false)
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10000, newPlayer.balance)
    }


}