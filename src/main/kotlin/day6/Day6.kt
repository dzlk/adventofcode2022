package day6

import utils.Resource

class Day6 {
}

fun main() {
    val file = Resource.getFile("day6/input") ?: return


    val data = file.readText()

    println("start-of-packet: ${getStartOfMarker(data, 4)}")
    println("start-of-message: ${getStartOfMarker(data, 14)}")
}

fun getStartOfMarker(data: String, distinct: Int): Int {
    assert(data.count() >= distinct) { "Data is short" }

    val chars = mutableMapOf<Char, Int>()
    val addToChars = { char: Char -> chars[char] = (chars[char] ?: 0) + 1 }

    data.forEachIndexed { index, char ->
        if (index < distinct) {
            addToChars(char)
        } else {
            addToChars(char)
            val prevChar = data[index - distinct]

            var count = chars[prevChar]?: throw Exception("Not found prev char, index: $index, prevChar: $prevChar")
            count--

            if (count == 0) {
                chars.remove(prevChar)
            } else {
                chars[prevChar] = count
            }
        }

        if (chars.size == distinct) {
            return index + 1
        }
    }

    assert(false) { "Marker not found" }
    return -1
}