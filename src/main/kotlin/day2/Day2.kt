package day2

import java.io.File

class Day2 {}
enum class Choose(val value: Int) {
    Rock(1),
    Paper(2),
    Scissors(3)
}

val ChooseMapper = mapOf(
    "A" to Choose.Rock,
    "X" to Choose.Rock,

    "B" to Choose.Paper,
    "Y" to Choose.Paper,

    "C" to Choose.Scissors,
    "Z" to Choose.Scissors
)

val WinMapper = mapOf(
    Choose.Rock to Choose.Paper,
    Choose.Paper to Choose.Scissors,
    Choose.Scissors to Choose.Rock
)

val LoseMapper = mapOf(
     Choose.Paper to Choose.Rock,
     Choose.Scissors to Choose.Paper,
     Choose.Rock to Choose.Scissors
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

    if (WinMapper[turn.first] == turn.second) {
        return value + 6
    }

    return value
}

fun main(args: Array<String>) {
    val resource = Day2::class.java.classLoader.getResource("day2/input") ?: return
    val file = File(resource.toURI())

    var total = 0
    var total2 = 0

    file.forEachLine {
        val turn = parseTurn(it) ?: throw Exception("parse error: $it")
        total += costTurn(turn)

        when {
            // need lose
            turn.second == ChooseMapper["X"] -> {
                total2 += costTurn(turn.first to LoseMapper[turn.first]!!)
            }

            // need to end the round in a draw
            turn.second == ChooseMapper["Y"] -> {
                total2 += costTurn(turn.first to turn.first)
            }

            // need win
            turn.second == ChooseMapper["Z"] -> {
                total2 += costTurn(turn.first to WinMapper[turn.first]!!)
            }
        }
    }

    println("Score: $total")
    println("Score2: $total2")
}