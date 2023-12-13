package main.kooozel.kotlin.day03

import main.kooozel.kotlin.readInput
import javax.xml.stream.events.Characters

fun main() {
    val dot = '.'

    fun extractNumber(row: String, j: Int): Pair<Int, Int> {
        var number = row[j].toString()
        var nextIndex = j + 1
        var prevIndex = j - 1
        while (nextIndex < row.length && row[nextIndex].isDigit()) {
            number = number.plus(row[nextIndex])
            nextIndex++
        }
        while (prevIndex > -1 && row[prevIndex].isDigit()) {
            number = row[prevIndex].toString().plus(number)
            prevIndex--
        }
        return Pair(number.toInt(), nextIndex)
    }

    fun checkRow(nextRow: String, j: Int): Boolean {
        if (nextRow[j - 1] != dot && !nextRow[j - 1].isDigit()) return true
        // down
        if (nextRow[j] != dot && !nextRow[j].isDigit()) return true
        // down right
        if (nextRow[j + 1] != dot && !nextRow[j + 1].isDigit()) return true
        return false
    }

    fun checkRowB(nextRow: String, j: Int): MutableSet<Pair<Int, Int>> {
        val list = mutableSetOf<Pair<Int, Int>>()
        if (nextRow[j - 1].isDigit()) {
            list.add(extractNumber(nextRow, j-1))
        }
        // down
        if (nextRow[j].isDigit()) {
            list.add(extractNumber(nextRow, j))
        }
        // down right
        if (nextRow[j + 1].isDigit()) {
            list.add(extractNumber(nextRow, j+1))
        }
        return list
    }

    fun validate(
        row: String,
        j: Int,
        input: List<String>,
        i: Int
    ): Boolean {
        val prev = j-1
        if (prev >= 0 && j + 1 < row.length) {

            if (row[j - 1] != dot && !row[j - 1].isDigit()) return true
            //right
            if (row[j + 1] != dot && !row[j + 1].isDigit()) return true
            //up left
            if (i - 1 >= 0) {
                val previousRow = input[i - 1]
                if (checkRow(previousRow, j)) return true
            }
            // down left
            if (i + 1 < input.size) {
                val nextRow = input[i + 1]
                if (checkRow(nextRow, j)) return true
            }
        }
        return false
    }

    fun validateB(
        row: String,
        j: Int,
        input: List<String>,
        i: Int
    ): MutableSet<Pair<Int, Int>> {
        val prev = j-1
        val set = mutableSetOf<Pair<Int,Int>>()
        if (prev >= 0 && j + 1 < row.length) {
            if (row[j - 1].isDigit()) set.add(extractNumber(row, j-1))
            //right
            if (row[j + 1].isDigit()) set.add(extractNumber(row, j+1))
            //up left
            if (i - 1 >= 0) {
                val previousRow = input[i - 1]
                set.addAll( checkRowB(previousRow, j))
            }
            // down left
            if (i + 1 < input.size) {
                val nextRow = input[i + 1]
                set.addAll( checkRowB(nextRow, j))
            }
        }
        return set
    }



    fun part1(input: List<String>): Int {
        var sum = 0
        for (i in input.indices) {
            val row = input[i]
            val set = mutableListOf<Int>()
            var j = 0
            while (j < row.length){
                if (row[j].isDigit()) {
                    var isValid = false
                    isValid = validate(row, j, input, i)

                    if (isValid) {
                        val pair = extractNumber(row, j)
                        set.add(pair.first)
                        j = pair.second
                    }
                }
                j ++
            }
            sum += set.sumOf { it }
        }



        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (i in input.indices) {
            val row = input[i]
            val set = mutableSetOf<Pair<Int, Int>>()
            for(j in 0 until row.length) {
                if (row[j] == '*') {
                   val result = validateB(row, j, input, i)
                    println(result)
                    if (result.size == 2) {
                        println(result.map { p -> p.first }.toList().fold(1) { acc, value -> acc * value })
                        sum += result.map { p -> p.first }.toList().fold(1) { acc, value -> acc * value }
                    }
                }
            }
        }



        return sum    }

    val testInput = readInput("03")
    val input = readInput("03")
//    println(part1(input))
    println(part2(input))

}

