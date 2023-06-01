package io.qmppu842.player

import io.qmppu842.newPerson
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
        val playersSize = PlayerController.getAllPlayers().size
        val newPlayer = PlayerController.newPlayer(newPerson(), 1000)

        assertEquals(playersSize + 1, PlayerController.getAllPlayers().size, "No player was added")

        val savedPlayerData = PlayerController.getPlayer(newPlayer.identity)

        assertEquals(savedPlayerData, newPlayer, "Saved one was not same as created one")
    }

    @Test
    fun addNegBalancePlayer() {
        var didCatch = false
        try {
            PlayerController.newPlayer(newPerson(), -1000)
        } catch (e: Exception) {
            didCatch = true
        }

        assertTrue(didCatch, "No exception with negative balance")
    }

    @Test
    fun updatePlayer() {
        val playerData = PlayerController.newPlayer(newPerson(), 1000)

        PlayerController.updatePlayerBalance(playerData.identity, 1000)
        var newPlayer = PlayerController.getPlayer(playerData.identity)

        assertEquals(2000, newPlayer.balance)

        var result = false

        try {
            PlayerController.updatePlayerBalance(playerData.identity, -4000)
            newPlayer = PlayerController.getPlayer(playerData.identity)
        } catch (e: Exception) {
            result = true
        }
        assertEquals(2000, newPlayer.balance)
        assertTrue(result)

        PlayerController.updatePlayerBalance(playerData.identity, -2000)
        newPlayer = PlayerController.getPlayer(playerData.identity)

        assertEquals(0, newPlayer.balance)
    }
}