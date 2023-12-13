package main.kooozel.kotlin.day09

import main.kooozel.kotlin.Day

class Day09 : Day("09") {
    val input = inputList

    fun calculateDifferences(input: List<Int>, total: MutableList<List<Int>>): MutableList<List<Int>> {
        val result = mutableListOf<Int>()
        for (i in 1 until input.size) {
            result.add(input[i] - input[i - 1])
        }
        total.add(result)
        return if (result.all { it == 0 }) total else calculateDifferences(result, total)
    }


    override fun partOne(): Any {
        var sum = 0
        for (s in input) {
            val numbers = s.split(" ").map { it.toInt() }
            val total = mutableListOf(numbers)
            calculateDifferences(numbers, total)
            val last = total.reversed().map { it.last() }
            val result = calculateResult(last)
            sum += result

        }



        return sum
    }

    fun calculateDifferencesB(input: List<Int>, total: MutableList<List<Int>>): MutableList<List<Int>> {
        val result = mutableListOf<Int>()
        for (i in input.size - 1 until 0) {
            result.add(input[i] - input[i + 1])
        }
        total.add(result)
        return if (result.all { it == 0 }) total else calculateDifferencesB(result, total)
    }

    private fun calculateResult(last: List<Int>): Int {
        return last.fold(0) { acc, i -> acc + i }
    }

    override fun partTwo(): Any {
        var sum = 0
        for (s in input) {
            val numbers = s.split(" ").map { it.toInt() }
            val total = mutableListOf(numbers)
            calculateDifferences(numbers, total)
            val first = total.reversed().map { it.first() }
            val result = first.fold(0, {acc, i ->  i - acc })
//            println(result)
            sum += result

        }



        return sum
    }
}