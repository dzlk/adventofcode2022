package day3

import utils.Resource
import java.io.File

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


fun main(args: Array<String>) {
    val file =  Resource.getFile("day3/input")?: return

    val priorityCounter = PriorityCounter()
    val groupPriorityCounter = GroupPriorityCounter()

    file.forEachLine {
        priorityCounter.push(it)
        groupPriorityCounter.push(it)
    }

    println("Priority total: ${priorityCounter.getTotal()}")
    println("Group Priority total: ${groupPriorityCounter.getTotal()}")
}

open class CharCounter {
    private var total = 0

    private fun scoreChar(char: Char): Int {
        if (char < 'a') {
            return char - 'A' + 1 + 26
        }

        return char - 'a' + 1
    }

    fun count(char: Char) {
        total += scoreChar(char)
    }

    fun getTotal(): Int {
        return total
    }
}

class PriorityCounter : CharCounter() {
    fun push(data: String) {
        val r = makeRucksack(data)
        val chars = r.first.intersect(r.second)
        if (chars.isEmpty()) {
            return
        }

        val char = chars.elementAt(0)

        count(char)
    }
}

class GroupPriorityCounter : CharCounter() {
    private val group = mutableListOf<String>()

    fun push(data: String) {
        group.add(data)

        if (group.count() == 3) {
            val chars = group[0].toSet()
                .intersect(group[1].toSet())
                .intersect(group[2].toSet())

            if (chars.isNotEmpty()) {
                val char = chars.elementAt(0)
                count(char)
            }

            group.clear()
        }
    }
}