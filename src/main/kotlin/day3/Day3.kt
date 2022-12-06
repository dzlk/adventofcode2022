package day3

import day2.Day2
import java.io.File
import kotlin.collections.MutableMap

class Day3 {
}

fun makeRucksack(value: String): Pair<Set<Char>, Set<Char>> {
    if (value.isEmpty()) {
        return setOf<Char>() to setOf()
    }

    val count = value.count()
    val mid = (count / 2)

    return value.slice(0 until mid).toSet() to
            value.slice(mid until count).toSet()
}

fun String.intersect(other: String): MutableList<Char> {
    val lhs = this.groupingBy { it }.eachCount().toMutableMap()
    val chars: MutableList<Char> = mutableListOf()

    other.forEach {
        if (lhs[it] == null) {
            return@forEach
        }

        chars.add(it)
        lhs[it]!!.minus(1)
        if (lhs[it] == 0) {
            lhs.remove(it)
        }
    }


    return chars
}

fun scoreChar(char: Char): Int {
    if (char < 'a') {
        return char - 'A' + 1 + 26
    }

    return char - 'a' + 1
}

fun main(args: Array<String>) {
    val resource = Day3::class.java.classLoader.getResource("day3/input") ?: return
    val file = File(resource.toURI())

    val priorityCounter = PriorityCounter()

    file.forEachLine {
        priorityCounter.push(it)
    }

    println("Priority total: ${priorityCounter.getTotal()}")
}

class PriorityCounter {
    private var total = 0

    fun push(data: String) {
        val r = makeRucksack(data)
        val chars = r.first.intersect(r.second)
        if (chars.isEmpty()) {
            return
        }

        val char = chars.elementAt(0)
        total += scoreChar(char)
    }

    fun getTotal(): Int {
        return total
    }
}