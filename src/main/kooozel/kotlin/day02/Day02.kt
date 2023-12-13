package main.kooozel.kotlin.day02

import main.kooozel.kotlin.readInput
import kotlin.math.min

fun main() {
    fun getGameNumber(input: String): Int? {
        val regex = Regex("""Game (\d+):""")
        val matchResult = regex.find(input)

        return matchResult?.groupValues?.get(1)?.toInt()
    }

    fun getDraws(input: String): List<String> {
        return input.split(":")[1].split(";")
    }

//    fun getGames(input: List<String>): MutableMap<String, Int> {
//        val colorPattern = Regex("""(\d+)\s+(\w+)""")
//        val colorMap = mutableMapOf<String, Int>()
//
//        input.forEach { input ->
//            colorPattern.findAll(input).forEach { matchResult ->
//                val quantity = matchResult.groupValues[1].toInt()
//                val color = matchResult.groupValues[2]
//
//                // Update the total quantity for each color in the map
//                colorMap[color] = colorMap.getOrDefault(color, 0) + quantity
//            }
//        }
//
//        return colorMap
//    }

    fun getGames(input: String): MutableMap<String, Int> {
        val colorPattern = Regex("""(\d+)\s+(\w+)""")
        val colorMap = mutableMapOf<String, Int>()
        colorPattern.findAll(input).forEach { matchResult ->
            val quantity = matchResult.groupValues[1].toInt()
            val color = matchResult.groupValues[2]

            // Update the total quantity for each color in the map
            colorMap[color] = colorMap.getOrDefault(color, 0) + quantity
        }

        return colorMap
    }



    fun part1(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            var isValid = true
            val gameNumber = getGameNumber(s)
            val draws = getDraws(s)


            draws.forEach{ draw ->
                val map = getGames(draw)
                Colors.colorMap.forEach { entry ->
                    val value = map[entry.key.name.lowercase()]
                    if (value != null) {
                        if (value > entry.value ) {
                            isValid = false
                        }
                    }
                }
            }

            if (isValid) {
                if (gameNumber != null) {
                    sum += gameNumber
                }
            }

        }
        return sum


    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (s in input) {
            val gameNumber = getGameNumber(s)
            val draws = getDraws(s)
            val minimumMap = mutableMapOf<Colors, Int>(
                Colors.RED to 0,
                Colors.BLUE to 0,
                Colors.GREEN to 0
            )

            draws.forEach{ draw ->
                val map = getGames(draw)
                for (color in Colors.values()) {
                    val value = map[color.name.lowercase()]
                    val min = minimumMap[color]
                    if (value != null && min != null) {
                        if (value > min)
                        minimumMap.replace(color, value)
                    }
                }

            }

            val result = minimumMap.values.fold(1) { acc, value -> acc * value }
//            println(result)
            sum += result

        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("02")
    println(part2(testInput))

    val input = readInput("02")
    println(part2(input))

}

enum class Colors {
    RED, BLUE, GREEN;

    companion object {
        val colorMap: Map<Colors, Int> = mapOf(
            RED to 12,
            GREEN to 13,
            BLUE to 14
        )
    }
}

data class ColorInfo(val color: String, val quantity: Int)