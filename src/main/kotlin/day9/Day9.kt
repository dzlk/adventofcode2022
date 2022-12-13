package day9

import utils.Resource
import kotlin.math.abs

class Day9 {}

fun main() {
    val file = Resource.getFile("day9/input") ?: throw Exception("No file input")

    val rope = Rope()
    rope.printMoving = false


    file.forEachLine {
        val (p1, p2) = it.split(" ")
        val cmd = Cmd(Direction.fromString(p1)!!, p2.toInt())

        rope.move(cmd)
    }

    rope.printMoves(true)
    println("Trail length: ${rope.trailLength()}")
}

enum class Direction {
    Right, Left, Up, Down;

    companion object {
        fun fromString(value: String): Direction? {
            return when (value) {
                "R" -> Right
                "L" -> Left
                "U" -> Up
                "D" -> Down
                else -> null
            }
        }
    }
}

data class Cmd(
    val direction: Direction,
    val value: Int
)

data class Point(var x: Int = 0, var y: Int = 0) {
}

data class Edge(
    var first: Point = Point(0, 0),
    var second: Point = Point(0, 0)
)

class Rope() {
    private val start = Point()

    private var edge = Edge()
    private var head = Point()
    private var tail = Point()

    private val trail = mutableSetOf(start)

    var printMoving = true

    fun move(cmd: Cmd) {
        if (printMoving) println(cmd)

        when (cmd.direction) {
            Direction.Right -> moveX(cmd.value)
            Direction.Left -> moveX(-cmd.value)
            Direction.Up -> moveY(cmd.value)
            Direction.Down -> moveY(-cmd.value)
        }
    }

    private fun moveX(value: Int = 0) {
        val offset = if (value > 0) 1 else -1

        (0 until abs(value)).forEach {
            head.x += offset

            moveTail()

            when {
                head.x < edge.first.x -> edge.first.x = head.x
                head.x > edge.second.x -> edge.second.x = head.x
            }

            if (printMoving) printMoves()
        }
    }

    private fun moveY(value: Int) {
        val offset = if (value > 0) 1 else -1

        (0 until abs(value)).forEach {
            head.y += offset

            moveTail()

            when {
                head.y > edge.first.y -> edge.first.y = head.y
                head.y < edge.second.y -> edge.second.y = head.y
            }

            if (printMoving) printMoves()
        }
    }

    private fun moveTail() {
        val diffX = head.x - tail.x
        val diffY = head.y - tail.y

        if (abs(diffX) == 2) {
            tail.x += diffX / 2

            if (abs(diffY) == 1) {
                tail.y += diffY
            }
        }

        if (abs(diffY) == 2) {
            tail.y += diffY / 2

            if (abs(diffX) == 1) {
                tail.x += diffX
            }
        }

        trail.add(tail.copy())
    }

    fun printMoves(showTrail: Boolean = false) {
        val defSymbol = "."

        println("-".repeat(10))
        for (y in edge.first.y downTo edge.second.y) {
            for (x in edge.first.x..edge.second.x) {
                val point = Point(x, y)

                if (showTrail) {
                    print(
                        when (point) {
                            start -> "s"
                            in trail -> "#"
                            else -> defSymbol
                        }
                    )
                } else {
                    print(
                        when (point) {
                            head -> "H"
                            tail -> "T"
                            start -> "s"
                            else -> defSymbol
                        }
                    )
                }


            }
            println()
        }
        println("=".repeat(10))
    }

    fun trailLength() = trail.size

}