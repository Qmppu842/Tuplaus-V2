package io.qmppu842.player

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class PlayerControllerTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun addPlayer() {
        val newPlayer = PlayerController.newPlayer("kala", 1000)

        assertEquals(1, PlayerController.players.size, "No player was added")
        assertTrue(newPlayer != null, "New Player was null")

        val savedPlayerData = PlayerController.getPlayer(newPlayer!!.identity)

        assertEquals(savedPlayerData, newPlayer, "Saved one was not same as created one")
    }

    @Test
    fun addNegBalancePlayer() {
        val players = PlayerController.players.size
        val newPlayer = PlayerController.newPlayer("kala", -1000)

        assertEquals(players, PlayerController.players.size)
        assertTrue(newPlayer == null)

    }
}