package io.qmppu842.game

import io.qmppu842.RandomHolder
import io.qmppu842.newPerson
import io.qmppu842.player.PlayerController
import io.qmppu842.player.PlayerData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GameControllerTest {

    private lateinit var player: PlayerData
    val hiLoQueue = LinkedList<Boolean>()

    @BeforeEach
    fun setUp() {
        RandomHolder.setseed(10) //8, 4, 3, 8, 8, 12, 10, 3, 13, 2, 13, 5
        hiLoQueue.offer(true)
        hiLoQueue.offer(false)
        hiLoQueue.offer(false)
        hiLoQueue.offer(true)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun playSomeGames() {
        player = PlayerController.newPlayer(newPerson())

        //Game 1
        val gameEvent = GameController.playGame(player, 10, hiLoQueue.poll())
        assertEquals(8, gameEvent.endCard, "Argh, just go read the Sec Randoms and match'em up")
        assertEquals(10, gameEvent.winnings, "Winnings don't match")
        var newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)


        //Game 2
        GameController.playGame(player, 10, hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10020, newPlayer.balance)


        //Game 3
        GameController.playGame(player, 10, !hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)


        //Game 4
        GameController.playGame(player, 10, !hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10000, newPlayer.balance)
    }


    @Test
    fun playSomeMoreGames() {
        player = PlayerController.newPlayer(newPerson())

        //Game 1
        val gameEvent = GameController.playGame(player, 10, hiLoQueue.poll())
        assertEquals(10, gameEvent.winnings, "Winnings don't match")
        var newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)

        var didCatch = false

        try {
            GameController.playGame(player, 10011, hiLoQueue.peek())
        } catch (e: Exception) {
            didCatch = true
        }

        assertTrue(didCatch, "No exception with too big bet")


        //Game 2
        GameController.playGame(player, 10010, !hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(0, newPlayer.balance)


        didCatch = false

        try {
            GameController.playGame(player, 10011, hiLoQueue.peek())
        } catch (e: Exception) {
            didCatch = true
        }

        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(0, newPlayer.balance)
        assertTrue(didCatch, "No exception with too big but winning bet")
    }


    @Test
    fun playSomeNegGames() {
        player = PlayerController.newPlayer(newPerson())

        //Game 1
        val gameEvent = GameController.playGame(player, 10, hiLoQueue.poll())
        assertEquals(10, gameEvent.winnings, "Winnings don't match")
        var newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)

        var didCatch = false

        try {
            GameController.playGame(player, -100, hiLoQueue.peek())
        } catch (e: Exception) {
            didCatch = true
        }

        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)
        assertTrue(didCatch, "No exception with negative bet with losing game")


        try {
            GameController.playGame(player, -100, !hiLoQueue.peek())
        } catch (e: Exception) {
            didCatch = true
        }

        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)
        assertTrue(didCatch, "No exception with negative bet with winning game")
    }

}