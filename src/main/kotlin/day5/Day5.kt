package day5

import utils.Resource

class Day5 {
}


fun main() {
    val file = Resource.getFile("day5/input") ?: return

    val deque = ArrayDeque<String>()
    var storage: Storage? = null

    file.forEachLine {
        if (storage == null) {
            if (it == "") {
                println(deque.removeLast())

                storage = Storage.create(deque)
                storage?.print()
                storage?.printTop()

            } else {
                deque.add(it)
            }

            return@forEachLine
        }

        val cmd = CommandMove.parse(it) ?: throw Exception("wrong command")
        storage?.move(cmd)
    }

    storage?.print()
    storage?.printTop()
}

data class CommandMove(val count: Int, val from: Int, val to: Int) {

    fun print() {
        println("$count crates $from --> $to")
    }
    companion object {
        private val cmdRe = """move (\d+) from (\d+) to (\d+)""".toRegex()
        fun parse(value: String): CommandMove? {
            val match = cmdRe.find(value)
            if (match == null || match.groups.size < 4) {
                return null
            }

            return try {
                CommandMove(
                    match.groups[1]!!.value.toInt(),
                    match.groups[2]!!.value.toInt(),
                    match.groups[3]!!.value.toInt()
                )
            } catch (e: java.lang.Exception) {
                null
            }
        }
    }
}

class Storage(private val size: Int) {
    private val crates: List<ArrayDeque<String>> = List<ArrayDeque<String>>(size) { ArrayDeque<String>() }
    fun push(crates: List<String>) {
        assert(this.size == crates.count()) { "Wrong input" }

        for (i in 0 until size) {
            if (crates[i].isEmpty()) {
                continue
            }
            this.crates[i].addLast(crates[i])
        }
    }

    fun move(cmd: CommandMove) {
        val from = cmd.from - 1
        val to = cmd.to - 1
        assert(from <= size && to <= size) { "wrong position in command: ${cmd.print()}" }
        assert(cmd.count <= crates[from].size) { "wrong count in command: ${cmd.print()}" }

        (0 until cmd.count).forEach {
            crates[to].addLast(
                crates[from].removeLast()
            )
        }
    }

    fun printTop() {
        var top = ""
        this.crates.forEach {
            top += it.lastOrNull() ?: ""
        }

        top = top.replace("[", "")
            .replace("]", "")

        println("Top crates: $top")
        println()
    }

    fun print() {
        var empty = false
        var index = 0
        while (!empty) {
            empty = true
            this.crates.forEach {
                if (it.size <= index) {
                    print(" ".repeat(4))
                } else {
                    print(it[index] + " ")
                    empty = false
                }
            }
            index++
            println()
        }

    }

    companion object {
        private val crateRe = """\[(\d+)\]""".toRegex()
        fun create(deque: ArrayDeque<String>): Storage? {
            var storage: Storage? = null
            while (deque.size > 0) {
                val line = deque.removeLast()
                val crates = line.chunked(4).map { it.trim() }
                if (storage == null) {
                    storage = Storage(crates.count())
                }

                storage.push(crates)
            }

            return storage
        }
    }
}