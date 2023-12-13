package main.kooozel.kotlin.day01

import main.kooozel.kotlin.readInput

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            val first = s.first { c -> c.isDigit() }
            val last = s.findLast { c -> c.isDigit() }
            val number = first.toString().plus(last)
            sum += number.toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            val map = mutableMapOf<Int, String>()
            val first = s.firstOrNull { c -> c.isDigit() }
            if (first != null) {
                map[s.indexOf(first)] = first.toString()
            }
            val last = s.findLast { c-> c.isDigit() }
            if (last != null) {
                map[s.lastIndexOf(last)] = last.toString()
            }
            Numbers.entries.forEach { number ->
                val indexText = s.indexOf(number.name,0, true)
                val indexText2 = s.lastIndexOf(number.name,s.length, true)
                if (indexText != -1) {
                    map[indexText] = (number.ordinal + 1).toString()
                }
                if (indexText2 != -1) {
                    map[indexText2] = (number.ordinal + 1).toString()
                }

            }
            val firstNumber = map[map.keys.min()]
            val lastNumber = map[map.keys.max()]
            val number = firstNumber.toString().plus(lastNumber)
            sum += number.toInt()
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("01")
    println(part2(testInput))

    val input = readInput("01")
    println(part2(input))
}

enum class Numbers {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
}

