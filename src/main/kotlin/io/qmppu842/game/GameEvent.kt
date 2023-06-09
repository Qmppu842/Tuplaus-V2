package io.qmppu842.game

import io.ktor.util.date.*
import kotlinx.serialization.Serializable

@Serializable
data class GameEvent(
    val id: Int = -1,
    val playerIdentity: String,
    val timestamp: Long = getTimeMillis(),
    val bet: Int = -1,
    val isPlayerChoiceBig: Boolean,
    val endCard: Int = -1,
    val winnings: Int = 0,
    val comboId: Int = -1
)
