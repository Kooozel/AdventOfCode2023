package main.kooozel.kotlin.day14

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.getGridMap
import java.awt.Point

class Day14 : Day("14") {
    val input = inputString


    val map = getGridMap(input)

    private fun solveOne(
        map: MutableMap<Point, Char>,
        direction: Boolean,
        horizontal: Boolean
    ): MutableMap<Point, Char> {
        val max = map.keys.maxOf { if (horizontal) it.getX() else it.getY() }.toInt()
        for (i in 0 until max + 1) {
            val line = if (horizontal) getRow(map, i) else getColumn(map, i)
            shift(map, line, i, direction, horizontal)
        }
        return map
    }

    private fun shift(map: MutableMap<Point, Char>, line: String, i: Int, direction: Boolean, horizontal: Boolean) {
        val sorted = splitSortJoin(line, direction)
        map.entries.filter { if (horizontal) it.key.x == i else it.key.y == i }
            .forEachIndexed { index, mutableEntry -> mutableEntry.setValue(sorted[index]) }
    }

    fun splitSortJoin(input: String, direction: Boolean): String {
        // Split the string by #
        val parts = input.split("#")

        // Sort the characters within each part
        val sortedParts =
            parts.map { it.toCharArray().apply { if (direction) sortDescending() else sort() }.joinToString("") }

        // Join the sorted parts with #
        return sortedParts.joinToString("#")
    }

    private fun getColumn(map: MutableMap<Point, Char>, index: Int) =
        map.entries.filter { it.key.y == index }.sortedBy { it.key.getX() }.map { it.value }.joinToString("")

    private fun getRow(map: MutableMap<Point, Char>, index: Int) =
        map.entries.filter { it.key.x == index }.sortedBy { it.key.getY() }.map { it.value }.joinToString("")

    override fun partOne(): Any {
        val resultMap = solveOne(map, direction = true, horizontal = false)
        return calc(resultMap)
    }

    private fun calc(map: MutableMap<Point, Char>): Long {
        val max = map.keys.maxOf { it.getX() }.toInt()
        var sum = 0L
        for (i in 0 until max + 1) {
            val row = map.entries.filter { it.key.x == i }
            sum += row.count { it.value == 'O' }.toLong() * (max - i + 1)
//            println(row.map { it.value }.joinToString(""))
        }
        println("=".repeat(10))
        return sum
    }

    override fun partTwo(): Any {
        //North true false
        //West  true true
        //South false false
        //East  false true


        val directions = listOf(true, true, false, false)
        val horizontals = listOf(false, true, false, true)

        var resultMap = map
        var numbers = mutableListOf<Long>()
        for (i in 0 until 1000) {
            for ((direction, horizontal) in directions.zip(horizontals)) {
                resultMap = solveOne(resultMap, direction, horizontal)
            }
            println(if (i % 10 == 0) "${i/10} % done" else "")
            numbers.add(calc(resultMap))
        }

        fun findCycle(numbers: List<Long>): Int {
            for (i in 1 until  numbers.size/2) {
               val chuncked = numbers.chunked(i).dropLast(1)
               if (chuncked.all { it == chuncked[0]}) return i
            }

            return -1
        }

        val n = 500
        numbers = numbers.drop(n).toMutableList()
        val cycle = findCycle(numbers)



        return  numbers[(1000000000- n -1) % cycle]

    }
}