package day7

import utils.Resource

class Day7 {
}

fun main() {
    val file = Resource.getFile("day7/input") ?: throw Exception("Input not found")

    val shell = Shell()

    file.forEachLine {
        shell.parse(it)
        println("$it")
        println(shell.path.path)
    }

    shell.cdRoot()

    println("-".repeat(10) + " FS")
    val dirs = shell.dirsSize.toSortedMap()
    dirs.forEach { println("%10s\t%s".format(it.value, it.key)) }

    println()
    println("-".repeat(10) + " Small files")
    dirs.filter { it.value < 100000 }
        .forEach { println("%10s\t%s".format(it.value, it.key)) }

    println()
    println("-".repeat(10) + " Find file")
    val freeSpace = 70_000_000 - dirs.getOrDefault("", 0)
    val needSpace = freeSpace - 30_000_000
    println("Free space: $freeSpace; need space for update: $needSpace")

    dirs.toSortedMap(compareBy { dirs[it] })
        .entries
        .find {
            val diff = needSpace + it.value

            println("%-10s\t%10s\t%s".format(diff, it.value, it.key))


            return@find diff > 0
        }
}

enum class Cmd { ls, cd }

class Path() {
    private val dirs = ArrayDeque<String>()
    var path: String = ""
        private set

    val depth: Int
        get() = dirs.size

    fun up() {
        dirs.removeLast()
        path = dirs.joinToString("/")
    }

    fun push(dir: String) {
        dirs.addLast(dir)
        path = dirs.joinToString("/")
    }
}

class Shell() {
    val path = Path()
    val dirsSize = mutableMapOf<String, Int>()

    private var lastCmd = Cmd.cd
    private var accSize = 0

    private val fileRe = """^(\d+) \w+""".toRegex()

    fun parse(str: String) {
        when {
            str == "$ cd /" -> cdRoot()
            str == "$ cd .." -> cdUp()
            str.startsWith("$ cd") -> cd(str)
            str.startsWith("$ ls") -> ls(str)
            else -> output(str)
        }
    }

    fun cdRoot() {
        while (path.depth > 0) {
            cdUp()
        }
    }

    private fun cdUp() {
        this.flush()

        val dir = path.path
        path.up()
        dirsSize[path.path] =
            dirsSize.getOrDefault(path.path, 0) +
        dirsSize.getOrDefault(dir, 0)

        lastCmd = Cmd.cd
    }

    private fun cd(cmd: String) {
        this.flush()

        path.push(
            cmd.removePrefix("$ cd ").trim()
        )

        lastCmd = Cmd.cd
    }

    private fun ls(cmd: String) {
        this.flush()

        lastCmd = Cmd.ls
    }

    private fun output(str: String) {
        if (lastCmd != Cmd.ls) {
            return
        }

        accSize += fileRe.find(str)?.groupValues?.get(1)?.toInt() ?: 0
    }

    fun flush() {
        if (lastCmd != Cmd.ls) {
            return
        }

        dirsSize[path.path] = accSize
        accSize = 0;
    }
}

