package io.qmppu842

import io.ktor.util.date.*
import java.security.SecureRandom

object RandomHolder {
    var secureRandom = SecureRandom()

    init {
        setseed()
    }

    fun setseed(seed: Long = getTimeMillis()) {
//        secureRandom.setSeed(seed)
        secureRandom = SecureRandom(seed.toString().toByteArray())
    }

    fun rand(min: Int = 0, max: Int = 14): Int {
        return secureRandom.nextInt(min, max)
    }

}