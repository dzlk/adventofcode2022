package day1

import java.io.File

class Day1

fun main(args: Array<String>) {
    val resource = Day1::class.java.classLoader.getResource("day1/input") ?: return
    val file = File(resource.toURI())

    val counter = Counter()

    file.forEachLine {
        var value : Int? = null
        try {
            value = it.toInt()
        } catch (e : NumberFormatException) {
            // do nothing
        }

        counter.count(value)
    }

    println("max: " + counter.getMax())
    println("sum: " + counter.getTopSum())
}

class Counter {
    private var curr : Int = 0
    private var top : MutableList<Int> = listOf(0, 0, 0).toMutableList()

    private fun updateTop() {
        if (curr <= top[top.count() - 1]) {
            curr = 0
            return
        }

        top.forEachIndexed { index, value ->
            if (curr > value) {
                top[index] = curr
                curr = value
            }
        }

        curr = 0
    }

    fun count(value: Int?) {
        if (value == null) {
            updateTop()
            return
        }

        curr += value
    }

    fun getMax() : Int {
        updateTop()

        return top[0]
    }

    fun getTopSum() : Int {
        return top.reduce { acc, value -> acc + value }
    }
}