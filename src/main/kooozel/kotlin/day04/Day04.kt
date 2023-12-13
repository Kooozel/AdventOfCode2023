package main.kooozel.kotlin.day04

import main.kooozel.kotlin.readInput

fun main() {
    fun getGameNumber(input: String): Int {
        val regex = Regex("""Card (\d+):""")
        val matchResult = regex.find(input)

        return matchResult?.groupValues?.get(1)?.toInt() ?: 99999
    }

    fun parseInput(input: String): List<String> {
        return input.split("|")
    }

    fun getWiningNumbers(input: String): List<Int> {
        return input.split(":")[1].trim().split(" ").filter { it != "" }.map { it.toInt() }
    }

    val base = 2


    fun part1(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            val gameNumber = getGameNumber(s)
            val input = parseInput(s)
            val winningNumbers = getWiningNumbers(input.get(0))
            val myNumbers = input.get(1).split(" ").filter { it != "" }.map { it.toInt() }
            val common = myNumbers.intersect(winningNumbers)

            val result = Math.pow(base.toDouble(), (common.size - 1).toDouble()).toInt()
            sum += result
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val list = mutableListOf<Card>()
        println(input.size)
        for (s in input) {
            val gameNumber = getGameNumber(s)
            val input = parseInput(s)
            val winningNumbers = getWiningNumbers(input.get(0))
            val myNumbers = input.get(1).split(" ").filter { it != "" }.map { it.toInt() }
            val common = myNumbers.intersect(winningNumbers).size
            val card = Card(gameNumber, 1, common)
            list.add(card)
        }

        for((index, card) in list.withIndex()) {
            val oneWinning = card.oneWinning
            for (i in index + 1 until index + oneWinning + 1) {
                    val cardToAdd = list[i]
                    cardToAdd.copy += 1 * card.copy
            }
        }

        return list.sumOf { it.copy }
    }

    val testInput = readInput("04")
    val input = readInput("04")
    println(part2(input))

}

class Card(@get:JvmName("getNumber") var number: Int, var copy: Int, var oneWinning: Int) {
    override fun toString(): String {
        return "Card(number=$number, copy=$copy, oneWinning=$oneWinning)"
    }

}