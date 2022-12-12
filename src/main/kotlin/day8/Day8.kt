package day8

import utils.Resource

class Day8 {
}

data class Tree(
    val value: Int,
    var tall: Boolean = false,

    var left: Int = 0,
    var right: Int = 0,
    var top: Int = 0,
    var bottom: Int = 0,
) {
    val cost: Int
        get() = left * right * top * bottom
}

class Grid() {
    private val data: MutableList<MutableList<Tree>> = mutableListOf()

    fun count(): Int = data.count()
    fun isSquare(): Boolean = data.count() == (data.getOrNull(0)?.count() ?: 0)

    fun addLineFromString(line: String) {
        val newLine = mutableListOf<Tree>()
        line.forEach { ch ->
            newLine.add(Tree(ch.toString().toInt()))
        }
        data.add(newLine)
    }

    fun get(i: Int, j: Int): Tree = data[i][j]

    fun lGet(i: Int, j: Int): Tree = data[j][i]
    fun rGet(i: Int, j: Int): Tree = data[j][count() - 1 - i]
    fun tGet(i: Int, j: Int): Tree = data[i][j]
    fun bGet(i: Int, j: Int): Tree = data[count() - 1 - i][j]
}


fun main() {
    val file = Resource.getFile("day8/input") ?: throw Exception("Input not found")

    val grid = Grid()

    file.forEachLine {
        grid.addLineFromString(it)
    }

    val size = grid.count()
    assert(grid.isSquare()) { "Input not grid" }


    var trees = 0
    val compareAndCountTree = { prev: Int, tree: Tree ->
        if (prev < tree.value) {
            if (!tree.tall) {
                trees++
                tree.tall = true
            }
        }

        prev.coerceAtLeast(tree.value)
    }

    (1 until size - 1).forEach { j ->
        var left = grid.lGet(0, j).value
        var right = grid.rGet(0, j).value
        var top = grid.tGet(0, j).value
        var bottom = grid.bGet(0, j).value

        (1 until size - 1).forEach { i ->
            left = compareAndCountTree(left, grid.lGet(i, j))
            right = compareAndCountTree(right, grid.rGet(i, j))
            top = compareAndCountTree(top, grid.tGet(i, j))
            bottom = compareAndCountTree(bottom, grid.bGet(i, j))
        }
    }

    println("trees: $trees")
    println("trees total: ${trees + (4 * size - 4)}")

    val countDistance: (MutableMap<Int, Int>, Tree, Int) -> Int = { map, tree, index ->
        val distance = map
            .filterKeys { it >= tree.value }
            .toSortedMap(compareBy { map[it] })
            .values
            .lastOrNull() ?: 0

        map[tree.value] = index

        index - distance
    }

    (1 until size - 1).forEach { j ->
        val leftMap = mutableMapOf<Int, Int>()
        val rightMap = mutableMapOf<Int, Int>()
        val topMap = mutableMapOf<Int, Int>()
        val bottomMap = mutableMapOf<Int, Int>()

        (1 until size - 1).forEach { i ->
            grid.lGet(i, j).let {
                it.left = countDistance(leftMap, it, i)
            }

            grid.rGet(i, j).let {
                it.right = countDistance(rightMap, it, i)
            }

            grid.tGet(i, j).let {
                it.top = countDistance(topMap, it, i)
            }

            grid.bGet(i, j).let {
                it.bottom = countDistance(bottomMap, it, i)
            }
        }
    }

    var bestTree: Tree = grid.get(0, 0)
    (1 until size - 1).forEach { j ->
        (1 until size - 1).forEach { i ->
            val tree = grid.get(i, j)
            if (bestTree.cost < tree.cost) {
                bestTree = tree
            }
        }
    }

    println("Best tree: $bestTree with cost: ${bestTree.cost}")
}

