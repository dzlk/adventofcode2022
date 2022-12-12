package day8

import utils.Resource

class Day8 {
}

fun main() {
    val file = Resource.getFile("day8/input") ?: throw Exception("Input not found")

    val grid = mutableListOf<MutableList<Pair<Int, Boolean>>>()

    file.forEachLine {
        val line = mutableListOf<Pair<Int, Boolean>>()
        it.forEach {
            line.add(it.toString().toInt() to false)
        }

        if (grid.isNotEmpty() && grid[0].count() != it.count()) {
            throw Exception("Wrong input")
        }
        grid.add(line)
    }

    val size = grid.count()
    assert(size == grid[0].count()) { "Input not grid" }


    var trees = 0
    val compareAndCountTree = { prev: Int, i: Int, j: Int ->
        if (prev < grid[i][j].first) {
            if (!grid[i][j].second) {
                trees++
                grid[i][j] = grid[i][j].first to true
            }
        }

        prev.coerceAtLeast(grid[i][j].first)
    }

    (1 until size - 1).forEach { j ->
        var left = grid[j][0].first
        var right = grid[j][size - 1].first
        var top = grid[0][j].first
        var bottom = grid[size - 1][j].first

        (1 until size - 1).forEach { i ->
            left = compareAndCountTree(left, j, i)
            right = compareAndCountTree(right, j, size - 1 - i)
            top = compareAndCountTree(top, i, j)
            bottom = compareAndCountTree(bottom, size - 1 - i, j)
        }
    }

    println("trees: $trees")
    println("trees total: ${trees + (4 * size - 4)}")
}

