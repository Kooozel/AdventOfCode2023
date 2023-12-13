package main.kooozel.kotlin.day07

import main.kooozel.kotlin.Day
import kotlin.streams.toList

private const val J = 'J'.toInt()

class Day07 : Day("07") {
    val strenght = listOf<Char>('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val strenght2 = listOf<Char>('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')


    private fun parseHands(): List<Hand> {
        val hands = mutableListOf<Hand>()
        for (s in inputList) {
            val split = s.split(" ")

            val power = getPower(split)

            hands.add(Hand(split[0], split[1].toInt(), power[0], if (power.size > 1) power[1] else 0))
        }
        return hands
    }

    private fun parseHands2(): List<Hand> {
        val hands = mutableListOf<Hand>()
        for (s in inputList) {
            val split = s.split(" ")

            val power = getPower2(split)
            println(s)
            hands.add(Hand(split[0], split[1].toInt(), power[0], if (power.size > 1) power[1] else 0))
        }
        return hands
    }

    private fun getPower(split: List<String>): List<Int> {
        val countByValue = split[0].chars().toList().groupingBy { it }.eachCount()
        return countByValue.values.sortedDescending()
    }

    private fun getPower2(split: List<String>): List<Int> {
        val countByValue = split[0].chars().toList().groupingBy { it }.eachCount().toMutableMap<Int,Int>()
        if (countByValue.containsKey(J)) {
            val valueOfJ = countByValue[J];
            val max = countByValue.maxBy { it.value }
            if (max.key != J) {
                countByValue.replace(max.key, max.value, max.value + valueOfJ!!)
            } else if (max.value == 5) {
                println("Joker Poker")
                return countByValue.values.sortedDescending()
            }

             else {
                val secondMax = countByValue.filter { it.key != J }.maxBy { it.value }
                countByValue.replace(secondMax.key, secondMax.value, secondMax.value + valueOfJ!!)
            }
            countByValue.remove(J)
        }
        return countByValue.values.sortedDescending()
    }


    override fun partOne(): Any {
        val hands = parseHands()
        val sorted = hands.sortedWith(
            compareBy(Hand::power)
                .thenBy(Hand::secondPower)
                .then { a, b ->
                    var i = 0
                    var result = compareCards(a.cards[i], b.cards[i])

                    while (result == 0 && i + 1 < a.cards.length) {
                        i++
                        result = compareCards(a.cards[i], b.cards[i])
                    }

                    result
                }
        )
        val result = sorted.map(Hand::oneWinning).foldIndexed(0) { index, acc, i -> acc + (i * (index + 1)) }

        return result
    }

    private fun compareCards(cards: Char, cards1: Char): Int {
        return strenght.indexOf(cards1)  - strenght.indexOf(cards)
    }

    private fun compareCards2(cards: Char, cards1: Char): Int {
        return strenght2.indexOf(cards1)  - strenght2.indexOf(cards)
    }


    override fun partTwo(): Any {
        val hands = parseHands2()
        val sorted = hands.sortedWith(
            compareBy(Hand::power)
                .thenBy(Hand::secondPower)
                .then { a, b ->
                    var i = 0
                    var result = compareCards2(a.cards[i], b.cards[i])

                    while (result == 0 && i + 1 < a.cards.length) {
                        i++
                        result = compareCards2(a.cards[i], b.cards[i])
                    }

                    result
                }
        )
        val result = sorted.map(Hand::oneWinning).foldIndexed(0) { index, acc, i -> acc + (i * (index + 1)) }
        return result
    }
}

class Hand(var cards: String, var oneWinning: Int, var power: Int, var secondPower: Int) {
    override fun toString(): String {
        return "Hand(cards='$cards', oneWinning=$oneWinning, power=$power, secondPower=$secondPower)"
    }

}