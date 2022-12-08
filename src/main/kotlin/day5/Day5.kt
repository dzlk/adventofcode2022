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
        storage?.moveBulk(cmd)
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

    private fun checkCmd(cmd: CommandMove) {
        val from = cmd.from - 1
        val to = cmd.to - 1
        assert(from <= size && to <= size) { "wrong position in command: ${cmd.print()}" }
        assert(cmd.count <= crates[from].size) { "wrong count in command: ${cmd.print()}" }
    }

    fun moveByOne(cmd: CommandMove) {
        checkCmd(cmd)
        (0 until cmd.count).forEach { _ ->
            crates[cmd.to - 1].addLast(
                crates[cmd.from - 1].removeLast()
            )
        }
    }

    fun moveBulk(cmd: CommandMove) {
        checkCmd(cmd)

        val tmp = ArrayDeque<String>()

        (0 until cmd.count).forEach { _ ->
            tmp.addLast(
                crates[cmd.from - 1].removeLast()
            )
        }

        while (tmp.isNotEmpty()) {
            crates[cmd.to - 1].addLast(
                tmp.removeLast()
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