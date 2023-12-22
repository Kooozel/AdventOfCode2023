package main.kooozel.kotlin.day21

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.Point
import java.util.*


class Day21 : Day("21") {
    private val input = inputListTest
    private val tile = input.flatMapIndexed { index, row ->
        row.mapIndexed { column, it ->
            Garden(
                ItemType.fromChar(it),
                Point(index.toLong(), column.toLong())
            )
        }
    }

    fun oneStep(start: Point): MutableSet<Point> {
        var result = mutableSetOf<Point>()
        for (direction in Direction.entries) {
            val newCoords = start.plus(direction)

            val newPoint = tile.find { it.coords == newCoords && it.type != ItemType.ROCK }
            if (newPoint != null) {
                result.add(newCoords)
            }
        }

        visited[start] = result.size
        return result
    }

    val visited = mutableMapOf<Point, Int>()

    override fun partOne(): Any {
        val start = tile.first { it.type == ItemType.START }
        val queue = LinkedList<Set<Point>>()
        queue.add(setOf(start.coords))
        var step = 10
        while (queue.isNotEmpty() && step > 0) {
            val process = queue.remove()
            val next = mutableSetOf<Point>()
            for (point in process) {
                val result = oneStep(point)
                next.addAll(result)
            }
            println(next.size)
            queue.add(next.filter { it != visited.keys }.toSet())
            step--
        }
        return tile.size
    }

    override fun partTwo(): Any {
        return input.size
    }
}

data class Garden(val type: ItemType, val coords: Point)

enum class ItemType {
    START, PLOT, ROCK;

    companion object {
        fun fromChar(char: Char): ItemType {
            return when (char) {
                '#' -> ROCK
                '.' -> PLOT
                'S' -> START
                else -> throw IllegalArgumentException("Unknow item type")
            }
        }
    }
}