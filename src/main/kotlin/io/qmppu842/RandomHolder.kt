package io.qmppu842

import io.ktor.util.date.*
import java.security.SecureRandom
import kotlin.random.Random

object RandomHolder {
        private var secureRandom = SecureRandom()
//    private var secureRandom = Random(11)

    init {
        setseed()
    }

    fun setseed(seed: Long = getTimeMillis()) {
        secureRandom = SecureRandom(seed.toString().toByteArray())
//        secureRandom = Random(seed)
        secureRandom.setSeed(seed)
    }

    fun rand(min: Int = 0, max: Int = 14): Int {
        return secureRandom.nextInt(min, max)
    }

    fun print15Rands() {
        println("Here are the randoms:")
        for (x in 0 until 15) {
            print("${rand()}, ")
        }
        println()
    }

}