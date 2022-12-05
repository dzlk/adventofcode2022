package day2

import java.io.File

class Day2 {}
enum class Choose(val value: Int) {
    Rock(1),
    Paper(2),
    Scissors(3)
}

val ChooseMapper = mapOf<String, Choose>(
    "A" to Choose.Rock,
    "X" to Choose.Rock,

    "B" to Choose.Paper,
    "Y" to Choose.Paper,

    "C" to Choose.Scissors,
    "Z" to Choose.Scissors
)

fun parseTurn(value: String): Pair<Choose, Choose>? {
    val parts = value.split(" ")
    if (parts.count() != 2) {
        return null
    }

    val lhs = ChooseMapper[parts[0]]
    val rhs = ChooseMapper[parts[1]]

    if (lhs == null || rhs == null) {
        return null
    }

    return lhs to rhs
}

fun costTurn(turn : Pair<Choose, Choose>) : Int {
    val value = turn.second.value

    if (turn.first == turn.second) {
        return value + 3
    }

    if ((turn.first == Choose.Rock && turn.second == Choose.Paper) ||
        (turn.first == Choose.Paper && turn.second == Choose.Scissors) ||
        (turn.first == Choose.Scissors && turn.second == Choose.Rock)) {
        return value + 6
    }

    return value
}

fun main(args: Array<String>) {
    val resource = Day2::class.java.classLoader.getResource("day2/input") ?: return
    val file = File(resource.toURI())

    var total = 0

    file.forEachLine {
        val turn = parseTurn(it) ?: throw Exception("parse error: $it")

        total += costTurn(turn)
    }

    println("Score: $total")
}