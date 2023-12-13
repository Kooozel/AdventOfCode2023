package main.kooozel.kotlin.day11

import main.kooozel.kotlin.Day
import java.awt.Point
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Day11: Day("11") {
    val input = inputList

    val galaxies = parseInput(input.toMutableList())

    private fun parseInput(input: MutableList<String>): MutableList<Point> {
        val galaxies = mutableListOf<Point>()

        val spacesHorizontal = mutableSetOf<Int>()
        val spacesVertical = mutableSetOf<Int>()

        for (char in input[0].indices) {
            if (input.map { it[char] }.all { c -> c == '.' }) {
                spacesVertical.add(char)
            }
        }

        for (s in input.indices) {
            val string = input[s]
            if (string.all { char -> char == '.' }) {
                spacesHorizontal.add(s)
            }

            string.forEachIndexed { index, char ->
                if (char == '#') {
                    val minVertical = spacesVertical.count{it < index}
                    val minHorizontal = spacesHorizontal.count{it < s}

                    val i = 1000000
                    val point = Point(s  + minHorizontal* i - minHorizontal, index + minVertical* i - minVertical)
                    galaxies.add(point)
                }
            }
        }


        return galaxies
    }


    override fun partOne(): Any {
        val uniquePairs = mutableListOf<Pair<Point, Point>>()

        for (i in galaxies.indices) {
            for (j in i + 1 until galaxies.size) {
                val pair = Pair(galaxies[i], galaxies[j])
                uniquePairs.add(pair)
            }
        }

        var sum = 0L

        for (pair in uniquePairs) {
            //find shortest path
            sum += findShortest(pair)
        }



        return sum
    }

    private fun findShortest(pair: Pair<Point, Point>): Long {
        val start = pair.first
        val end = pair.second

        val dx = abs(end.getX() - start.getX())
        val dy = abs(end.getY() - start.getY())


        return  (dx + dy).toLong()
    }

    override fun partTwo(): Any {
        return input.size
    }

}
