package main.kooozel.kotlin.day13

import main.kooozel.kotlin.Day
import java.awt.Point

class Day13 : Day("13") {
    val input = inputString.split("\n\n")

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
        checkHorizontalPattern(map)
        checkVerticalPattern(map)

    }

    private fun checkHorizontalPattern(map: MutableMap<Point, Pattern>) {
        val max = map.keys.maxOf { it.getX() }.toInt()
        for (i in 1 until max + 1) {
            val up = getRows(map, 0,i ).reversed()
            val down = getRows(map, i, max + 1)
            if (up.zip(down).all { it.first == it.second }) result.add(i*100L)
        }
    }

    private fun checkHorizontalPattern2(map: MutableMap<Point, Pattern>) {
        val max = map.keys.maxOf { it.getX() }.toInt()
        for (i in 1 until max + 1) {
            val up = getRows(map, 0,i ).reversed()
            val down = getRows(map, i, max + 1)
            var difference = 0
            up.zip(down).forEach {
                difference += calculateDiff(it.first, it.second)
            }
            if (difference == 1) result.add(i*100L)
        }
    }

    private fun getRows(map: MutableMap<Point, Pattern>, i: Int, end: Int): MutableList<String> {
        val list = mutableListOf<String>()
        for (index in i until end) {
            list.add(getRow(map, index))
        }
        return list
    }

    val result = mutableListOf<Long>()

    private fun checkVerticalPattern2(map: MutableMap<Point, Pattern>) {
        val max = map.keys.maxOf { it.getY() }.toInt()
        for (i in 1 until max + 1) {
            val left = getColumns(map, 0, i).reversed()
            val right = getColumns(map, i, max + 1)
            var difference = 0
            left.zip(right).forEach {
                difference += calculateDiff(it.first, it.second)
            }
            if (difference == 1) result.add(i.toLong())
        }
    }

    private fun checkVerticalPattern(map: MutableMap<Point, Pattern>) {
        val max = map.keys.maxOf { it.getY() }.toInt()
        for (i in 1 until max + 1) {
            val left = getColumns(map, 0,i ).reversed()
            val right = getColumns(map, i, max + 1)
            if (left.zip(right).all { it.first == it.second }) result.add(i.toLong())
        }
    }

    private fun calculateDiff(left: String, right: String): Int {
            require(left.length == right.length) { "Input strings must have the same length" }

            var differences = 0

            for (i in left.indices) {
                if (left[i] != right[i]) {
                    differences++
                }
            }

            return differences

    }
    private fun getColumns(map: MutableMap<Point, Pattern>, i: Int, end: Int): MutableList<String> {
        val list = mutableListOf<String>()
        for (index in i until end) {
            list.add(getColumn(map, index))
        }
    return list
    }


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

    private fun solveOne2(s: String) {
        val map = parseInput(s)
        checkHorizontalPattern2(map)
        checkVerticalPattern2(map)
    }

    override fun partTwo(): Any {
        result.clear()
        for (s in input) {
            solveOne2(s)
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