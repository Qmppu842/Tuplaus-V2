package io.qmppu842

import io.ktor.util.date.*
import java.security.SecureRandom
import kotlin.random.Random

object RandomHolder {
    //    var secureRandom = SecureRandom()
    var secureRandom = Random(11)

    init {
        setseed()
    }

    fun setseed(seed: Long = getTimeMillis()) {
//        secureRandom.setSeed(seed)
//        secureRandom = SecureRandom(seed.toString().toByteArray())
        secureRandom = Random(seed)
    }

    fun rand(min: Int = 0, max: Int = 14): Int {
        return secureRandom.nextInt(min, max)
    }

}