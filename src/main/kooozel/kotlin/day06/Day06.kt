package main.kooozel.kotlin.day06

import main.kooozel.kotlin.Day


class Day06 : Day("06") {
    private val input = inputList;


    private fun parseInput(input: List<String>): List<Pair<Int,Int>> {
        val pattern = Regex("\\d+")
        val times = pattern.findAll(input[0]).map { it.value.toInt() }
        val distances = pattern.findAll(input[1]).map { it.value.toInt() }
        return times.zip(distances).toList()
    }

    private fun parseInput2(input: List<String>): List<Pair<Long,Long>> {
        val pattern = Regex("\\d+")
        val times = pattern.findAll(input[0]).map { it.value.toLong() }
        val distances = pattern.findAll(input[1]).map { it.value.toLong() }
        return times.zip(distances).toList()
    }

    val SPEED = 1;

    private fun calculate(pair: Pair<Int, Int>, ways: MutableList<Int>) {
        var holdTime = 0;
        var waysToWin = 0
        while (holdTime <= pair.first) {
            val distance = holdTime * SPEED * (pair.first - holdTime)
            if (distance > pair.second) {
                waysToWin++
            }
            holdTime++
        }

        ways.add(waysToWin)
        println("PairDone + ${waysToWin}")
    }


    override fun partOne(): Any {
        val pairs = parseInput(input)
        val ways = mutableListOf<Int>()
        for (pair in pairs) {
            calculate(pair, ways)
        }

        return ways.fold(1) { acc, value -> acc * value }
    }

    override fun partTwo(): Any {
        val pairs = parseInput2(input)
        val ways = mutableListOf<Int>()
        for (pair in pairs) {
            var holdTime = 0;
            var waysToWin = 0
            while (holdTime <= pair.first) {
                val distance = holdTime * SPEED * (pair.first - holdTime)
                if (distance > pair.second) {
                    waysToWin++
                }
                holdTime++
            }

            ways.add(waysToWin)
            println("PairDone + ${waysToWin}")
        }
        return ways[0]
    }
}