package main.kooozel.kotlin.day10

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction

class Day10 : Day("10") {
    val input = inputListTest
    val direction = Direction.DOWN
    override fun partOne(): Any {

        val map = parseInput(input)
        var start = map.entries.first { it.value == PipeType.START }
        var newStart = moveNext(map, start, direction)
        var step = 1
        while (newStart != null && newStart.first.value != PipeType.START) {
            step += 1
            newStart = moveNext(map, newStart.first, newStart.second)
        }


        return step / 2
    }


    private fun moveNext(
        map: Map<Pair<Int, Int>, PipeType>,
        start: Map.Entry<Pair<Int, Int>, PipeType>,
        direction: Direction,
    ): Pair<Map.Entry<Pair<Int, Int>, PipeType>, Direction>? {
        var next: PipeType? = null
        val startCord = start.key
        var nextDirection: Direction? = null
        var key: Pair<Int, Int>? = null
        when (direction) {
            Direction.UP -> {
                key = Pair((startCord.first - 1), startCord.second)
                next = map.get(key)
                if (next != null && next.connect.contains(Direction.DOWN)) nextDirection =
                    next.connect.first { it != Direction.DOWN }
            }

            Direction.LEFT -> {
                key = Pair((startCord.first), startCord.second - 1)
                next = map.get(key)
                if (next != null && next.connect.contains(Direction.RIGHT)) nextDirection =
                    next.connect.first { it != Direction.RIGHT }
            }

            Direction.RIGHT -> {
                key = Pair((startCord.first), startCord.second + 1)
                next = map.get(key)
                if (next != null && next.connect.contains(Direction.LEFT)) nextDirection =
                    next.connect.first { it != Direction.LEFT }
            }

            Direction.DOWN -> {
                key = Pair((startCord.first + 1), startCord.second)
                next = map.get(key)
                if (next != null && next.connect.contains(Direction.UP)) nextDirection =
                    next.connect.first { it != Direction.UP }
            }
        }
        return if (next != null && nextDirection != null) {
            Pair(map.entries.first { it.key == key }, nextDirection)
        } else {
            println("path End")
            null
        }
    }

    private fun parseInput(input: List<String>): MutableMap<Pair<Int, Int>, PipeType> {
        val map = mutableMapOf<Pair<Int, Int>, PipeType>()
        for (i in input.indices) {
            val string = input[i]
            for (j in string.indices) {
                val char = string[j]
                val pipeType = PipeType.from(char)
                if (pipeType != null) map[Pair(i, j)] = pipeType
            }
        }
        return map
    }

    override fun partTwo(): Any {
        val map = parseInput(input)
        val start = map.entries.first { it.value == PipeType.START }
        var newStart = moveNext(map, start, direction)
        val list = mutableListOf(start.key)
        while (newStart != null && newStart.first.value != PipeType.START) {
            list.add(newStart.first.key)
            newStart = moveNext(map, newStart.first, newStart.second)
        }
        val coordinatesOfMis = mutableListOf<Pair<Int, Int>>()
        val groupPair = list.groupBy { it.first }
        var missingSpaces = 0
        for (entry in groupPair) {
            val coordinates = entry.value.map { it.second }.sorted()
            val missingNumber = findMissingNumbers(coordinates)
            for (number in missingNumber) {
                coordinatesOfMis.add(Pair(entry.key, number))
            }
        }

        val result = coordinatesOfMis.filter { isValid(it, list) }

        printResult(map, list, result.toMutableList())

        return result.size
    }

    private fun isValid(it: Pair<Int, Int>, list: MutableList<Pair<Int, Int>>): Boolean {
        var isInside = false

        for (i in list.indices) {
            val vertexI = list[i]
            val vertexJ = list[(i + 1) % list.size]

            val yRangeCheck = (vertexI.second > it.second) != (vertexJ.second > it.second)
            if (vertexI.second != vertexJ.second) {
                val xRangeCheck =
                    it.first < (vertexJ.first - vertexI.first) * (it.second - vertexI.second) / (vertexJ.second - vertexI.second) + vertexI.first

                if (yRangeCheck && xRangeCheck) {
                    isInside = !isInside
                }
            }
        }

        return isInside
    }

    fun findMissingNumbers(numbers: List<Int>): List<Int> {
        if (numbers.isEmpty()) {
            return emptyList()
        }

        val minNumber = numbers.minOrNull()!!
        val maxNumber = numbers.maxOrNull()!!
        val fullRange = minNumber..maxNumber
        val presentNumbers = numbers.toSet()

        return fullRange.filterNot { it in presentNumbers }
    }


    private fun printResult(
        map: MutableMap<Pair<Int, Int>, PipeType>,
        list: MutableList<Pair<Int, Int>>,
        coordinatesOfMis: MutableList<Pair<Int, Int>>
    ) {
        for (i in 0 until input.size) {
            var s = ""
            for (j in 0 until input[i].length) {
                val pair = Pair(i, j)
                if (list.contains(pair)) s = s.plus("*")
                else if (coordinatesOfMis.contains(pair)) s = s.plus("I")
                else if (map.containsKey(pair)) s = s.plus("O")
                else s = s.plus('.')
            }
            println(s)
        }
    }


}

class Pipe(val pipeType: PipeType, val coordinates: Pair<Int, Int>) {
    override fun toString(): String {
        return "Pipe(pipeType=$pipeType, coordinates=$coordinates)"
    }
}

enum class PipeType(val symbol: Char, val connect: List<Direction>) {
    VERTICAL('|', listOf(Direction.UP, Direction.DOWN)),
    HORIZONTAL('-', listOf(Direction.LEFT, Direction.RIGHT)),
    NE_BEND('L', listOf(Direction.UP, Direction.RIGHT)),
    NW_BEND('J', listOf(Direction.UP, Direction.LEFT)),
    SW_BEND('7', listOf(Direction.DOWN, Direction.LEFT)),
    SE_BEND('F', listOf(Direction.DOWN, Direction.RIGHT)),
    START('S', listOf(Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.LEFT));

    override fun toString(): String {
        return "PipeType(symbol=$symbol, connect=$connect)"
    }

    companion object {
        infix fun from(char: Char): PipeType? = entries.firstOrNull { it.symbol == char }
    }
}

