package main.kooozel.kotlin.day08

import main.kooozel.kotlin.Day

class Day08 : Day("08") {
    val currentInput = inputString

    private fun parseInput(currentInput: List<String>): LinkedHashMap<String, Pair<String, String>> {
        val map = linkedMapOf<String, Pair<String, String>>()
        for (s in currentInput) {
            val parts = s.split(" = ", "(", ")", ", ")
            map[parts[0]] = Pair(parts[2], parts[3])
        }
        return map
    }

    override fun partOne(): Any {
        val split = inputStringTest.split("\n\n")
        val orders = split[0]
        val map = parseInput(split[1].split("\n"));

        val position = "AAA"
        val step = 0

        return findPosition(orders, map, position, step)
    }

    private fun findPosition(
        orders: String,
        map: LinkedHashMap<String, Pair<String, String>>,
        position: String,
        step: Int
    ): Int {
        var step1 = step
        var sum = 0
        var newPosition = position
        while (step1 < orders.length) {
            val order = orders[step1]
            newPosition = step(map, newPosition, order)
            step1++
            if (newPosition.last() == 'Z') {
                sum += step1
                break
            }

            if (step1 == orders.length) {
                sum += step1
                step1 = 0
                println(sum)
            }
        }
        return sum
    }

    private fun step(
        map: LinkedHashMap<String, Pair<String, String>>,
        position: String,
        order: Char,

        ): String {
        val pair = map.get(position)
        if (pair != null) {
            when (order) {
                'R' -> {
                    return pair.second
                }

                'L' -> {
                    return pair.first
                }

                else -> {
                    return position
                }
            }
        }
        throw Exception()
    }


    override fun partTwo(): Any {
        val split = currentInput.split("\n\n")
        val orders = split[0]
        val map = parseInput(split[1].split("\n"));

        val position = map.keys.filter { it.last() == 'A' }
        val step = 0
        val list = mutableListOf<Int>()
        for (pos in position) {
            list.add(findPosition(orders,map,pos,step))
        }
        return findLCMOfListOfNumbers(list.map { it.toLong() })
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0.toLong() && lcm % b == 0.toLong()) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }
}