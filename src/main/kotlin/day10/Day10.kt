package day10

import utils.Resource
import kotlin.math.sign

class Day10 {
}

fun main() {
    val file = Resource.getFile("day10/input")?: throw Exception("No input")

    val meaningful = setOf(20, 60, 100, 140, 180, 220)
    var signalSum = 0

    val commutator = Commutator() {
        if (it.tick in meaningful) {
            val signal = it.tick * it.value
            println("Tick: ${it.tick}; X: ${it.value}; Signal: $signal")
            signalSum += signal
        }
    }

    file.forEachLine {
//        println("cmd $it")

        val parts = it.split(" ")
        when(Cmd.valueOf(parts[0])) {
            Cmd.addx -> commutator.addx(parts[1]!!.toInt())
            Cmd.noop -> commutator.noop()
        }
    }

    println("Signal sum: $signalSum")
}

enum class Cmd {
    addx, noop;
}
class Commutator(val announce: (Commutator) -> Unit) {
    var value = 1
        private set
    var tick = 0
        private set

    fun addx(value: Int) {
        tick()
        tick()
        this.value += value
    }

    fun noop() {
        tick()
    }

    private fun tick() {
        tick += 1
        this.announce(this)
    }
}