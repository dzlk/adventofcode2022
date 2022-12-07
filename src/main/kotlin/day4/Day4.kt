package day4

import utils.Resource
import java.text.ParseException

class Day4 {
}

data class Section(val start: Int, val end: Int)

operator fun Section.contains(other: Section): Boolean = this.start <= other.start && this.end >= other.end
fun Section.overlaps(other: Section): Boolean =
    other.start in this.start..this.end ||
            other.end in this.start..this.end

fun parseSection(value: String): Section? {
    val values = value.split("-")
    if (values.count() != 2) {
        return null
    }

    return try {
        Section(
            start = values[0].toInt(),
            end = values[1].toInt()
        )
    } catch (e: NumberFormatException) {
        null
    }
}

fun parseSections(value: String): Pair<Section, Section>? {
    val pairs = value.split(",")
    if (pairs.count() != 2) {
        return null
    }

    val first = parseSection(pairs[0])
    val second = parseSection(pairs[1])
    if (first == null || second == null) {
        return null
    }

    return first to second
}

fun main() {
    val file = Resource.getFile("day4/input") ?: return

    var badPairs = 0
    var overlapsPairs = 0

    file.forEachLine {
        val pair = parseSections(it) ?: throw Exception("input format is bad")

        if (pair.first in pair.second || pair.second in pair.first) {
            badPairs++
            overlapsPairs++

            return@forEachLine
        }

        if (pair.first.overlaps(pair.second)) {
            overlapsPairs++
        }
    }

    println("Bad pairs: $badPairs")
    println("Bad pairs: $overlapsPairs")
}