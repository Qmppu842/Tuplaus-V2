package io.qmppu842.game

import io.qmppu842.RandomHolder
import io.qmppu842.newPerson
import io.qmppu842.player.PlayerController
import io.qmppu842.player.PlayerData
import org.junit.jupiter.api.AfterEach
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
        hiLoQueue.offer(true)
        hiLoQueue.offer(true)
        hiLoQueue.offer(true)//num 10, game 7
        hiLoQueue.offer(false)
        hiLoQueue.offer(true)
        hiLoQueue.offer(false)
        hiLoQueue.offer(true)
        hiLoQueue.offer(false)//num 5, game 12

        player = PlayerController.newPlayer(newPerson())
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun playSomeGames() {
        val gameEvents = GameController.getAllGames().size

        //Game 1
        val gameEvent = GameController.playGame(player, 10, hiLoQueue.poll())
        assertEquals(8, gameEvent.endCard, "Argh, just go read the Sec Randoms and match'em up")
        assertEquals(10, gameEvent.winnings, "Winnings don't match")
        var newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10010, newPlayer.balance)
        assertEquals(gameEvents + 1, GameController.getAllGames().size)

        //Game 2
        GameController.playGame(player, 350, hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10360, newPlayer.balance)
        assertEquals(gameEvents + 2, GameController.getAllGames().size)

        //Game 3 Incorrect
        GameController.playGame(player, 150, !hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10210, newPlayer.balance)

        //Game 4 Incorrect
        GameController.playGame(player, 10, !hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10200, newPlayer.balance)

        //Game 5 Incorrect
        GameController.playGame(player, 200, !hiLoQueue.poll())
        newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10000, newPlayer.balance)
    }


    @Test
    fun playSomeMoreGames() {
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


        //Game 2 Incorrect
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

    @Test
    fun findLastGames() {
        playSomeGames()

        //Game 6
        val actualLastGame = GameController.playGame(player, 400, hiLoQueue.poll())
        val newPlayer = PlayerController.getPlayer(player.identity)
        assertEquals(10400, newPlayer.balance)

        var gettedLastGame: GameEvent? = GameController.getLastGameFrom(newPlayer.identity)
        assertEquals(actualLastGame, gettedLastGame, "Last game was not what was expected")

        //Last game when there is more than 1 player
        GameController.playGame(PlayerController.newPlayer(newPerson()), 456, hiLoQueue.poll())
        gettedLastGame = GameController.getLastGameFrom(newPlayer.identity)
        assertEquals(actualLastGame, gettedLastGame)
    }


    @Test
    fun playComboGames() {
        playSomeGames()

        //Game 6 & 7 & 8
        val game1 = GameController.playGame(player, 400, hiLoQueue.poll())
        val game2 = GameController.playGame(player, null, hiLoQueue.poll())
        val game3 = GameController.playGame(player, null, hiLoQueue.poll())

        assertEquals(game1.id, game2.comboId)
        assertEquals(game2.id, game3.comboId)

        assertEquals(800, game2.bet)
        assertEquals(1600, game3.bet)

        assertEquals(12800, player.balance)

//        for (x in 0..10) {
//            GameController.playGame(PlayerController.newPlayer(newPerson()), 456, false)
//        }

        //Game 9 & 10 Incorrect
        val game4 = GameController.playGame(player, null, hiLoQueue.poll())
        val game5 = GameController.playGame(player, null, !hiLoQueue.poll())



        assertEquals(game3.id, game4.comboId)
        assertEquals(game4.id, game5.comboId)

        assertEquals(3200, game4.bet)
        assertEquals(6400, game5.bet)


        assertEquals(9600, player.balance)

        //Game 11 Incorrect
        var didCatch = false
        try {
            GameController.playGame(player, null, !hiLoQueue.poll())
        } catch (e: Exception) {
            didCatch = true
        }
        assertTrue(didCatch, "Combo game was allowed even after loss")

    }

}