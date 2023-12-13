package main.kooozel.kotlin.day13

import main.kooozel.kotlin.Day
import org.apache.commons.text.similarity.LevenshteinDistance
import java.awt.Point

class Day13 : Day("13") {
    val input = inputStringTest.split("\n\n")

    private fun parseInput(string: String): MutableMap<Point, Pattern> {
        val map = mutableMapOf<Point, Pattern>()
        string.split("\n").forEachIndexed { index, s ->
            for (i in s.indices) {
                map[Point(index, i)] = Pattern(Obstacle.fromChar(s[i]))
            }
        }
        return map
    }

    private fun solveOne(string: String) {
        val map = parseInput(string)
        checkVerticalPattern(map)
        checkHorizontalPattern(map)

    }

    private fun checkHorizontalPattern(map: MutableMap<Point, Pattern>) {
        val max = map.keys.maxOf { it.getX() }.toInt()
        for (i in 1 until max + 1) {
            checkHorizontalReflections(map, i, 0, max)
        }
    }

    private fun checkHorizontalReflections(
        map: MutableMap<Point, Pattern>,
        i: Int,
        next: Int,
        max: Int,
    ) {
        var newNext = next
        val up = getRow(map, i - (1 + newNext))
        val down = getRow(map, i + newNext)
        if (up == down) {
            println("Row Match $i")
            newNext++

            if (i - (1 + newNext) < 0 || (i+ newNext) > max) {
                println("Full match row $i")
                result.add(i * 100L)
                return
            }
            return checkHorizontalReflections(map, i, newNext, max)
        } else {
            return
        }
    }

    val result = mutableListOf<Long>()

    private fun checkVerticalPattern(map: MutableMap<Point, Pattern>) {
        val max = map.keys.maxOf { it.getY() }.toInt()
        for (i in 1 until max + 1) {
            checkReflections(map, i, 0, max)
        }
    }

    private fun checkReflections(
        map: MutableMap<Point, Pattern>,
        i: Int,
        next: Int,
        max: Int,
    ) {
        var newNext = next
        val left = getColumn(map, i - (1 + newNext))
        val right = getColumn(map, i + newNext)
        if (left == right) {
            println("Column Match $i")
            newNext++

            if (i - (1 + newNext) < 0 || (i+ newNext) > max) {
                println("Full match column $i")
                result.add(i.toLong())
                return
            }
            return checkReflections(map, i, newNext, max)
        } else {
            return
        }
    }

//    private fun b(left: String, right: String, map: MutableMap<Point, Pattern>): Int {
//            require(left.length == right.length) { "Input strings must have the same length" }
//
//            var differences = 0
//            val mutableList = mutableListOf<Int>()
//
//            for (i in left.indices) {
//                if (left[i] != right[i]) {
//                    mutableList.add(i)
//                    differences++
//                }
//            }
//
//            return differences
//
//    }

    private fun getColumn(map: MutableMap<Point, Pattern>, index: Int) =
        map.entries.filter { it.key.y == index }.sortedBy { it.key.getX() }.map { it.value.type.char }.joinToString("")
    private fun getRow(map: MutableMap<Point, Pattern>, index: Int) =
        map.entries.filter { it.key.x == index }.sortedBy { it.key.getY() }.map { it.value.type.char }.joinToString("")

    override fun partOne(): Any {
        for (s in input) {
            solveOne(s)
            println("done")
        }
        return result.sum()
    }

    override fun partTwo(): Any {
        result.clear()
        for (s in input) {
            solveOne(s)
            println("done")
        }
        return result.sum()
    }

    class Pattern(val type: Obstacle) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Pattern

            return type == other.type
        }

        override fun hashCode(): Int {
            return type.hashCode()
        }
    }

    enum class Obstacle(val char: Char) {
        ROCK('#'), ASH('.');

        companion object {
            fun fromChar(char: Char): Obstacle {
                if (char == '#') return ROCK
                if (char == '.') return ASH
                else throw Error()
            }
        }

        override fun toString(): String {
            return "Obstacle(char=$char)"
        }
    }
}