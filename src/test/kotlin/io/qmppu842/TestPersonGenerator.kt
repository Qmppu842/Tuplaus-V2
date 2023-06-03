package io.qmppu842

import java.util.concurrent.atomic.AtomicInteger

private var id: AtomicInteger = AtomicInteger(0)

fun newPerson(): String {
    return "Tester_${id.getAndIncrement()}"
}

