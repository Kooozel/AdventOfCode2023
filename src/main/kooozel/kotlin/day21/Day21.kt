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
    private val maxX = tile.maxOf { it.coords.x }
    private val maxY = tile.maxOf { it.coords.y }

    private fun findPointType(point: Point): Garden? {
        val coords = tile.map { it.coords }
        if (point in coords) {
            return tile.find { it.coords == point && it.type != ItemType.ROCK }
        } else {
            return tile.find { it.coords == recalculate(point) && it.type != ItemType.ROCK }
        }

    }

    private fun recalculate(point: Point): Point {

        // -1 -> 10
        // -2 -> 9
        // 1 -> 0
        //
        var x = point.x % maxX
        if (point.x !in 0..maxX) {
            if (x > 0) {
                x -= 1
            } else if (x < 0) {
                x += (maxX + 1)
            }
        }
        var y = point.y % maxY
        if (point.y !in 0..maxY) {
            if (y > 0) {
                y -= 1
            } else if (y < 0) {
                y += (maxY + 1)
            }
        }
        return Point(x, y)
    }


    private fun oneStep(start: Point): MutableSet<Point> {
        val result = mutableSetOf<Point>()
        for (direction in Direction.entries) {
            val newCoords = start.plus(direction)

            val newPoint = findPointType(newCoords)
            if (newPoint != null) {
                result.add(newCoords)
            }
        }

        visited[start] = result.size
        return result
    }

    private val visited = mutableMapOf<Point, Int>()
    private val  resultList = mutableListOf<Int>()

    override fun partOne(): Any {
        val start = tile.first { it.type == ItemType.START }
        val queue = LinkedList<Set<Point>>()
        queue.add(setOf(start.coords))
        var step = 50
        while (queue.isNotEmpty() && step > 0) {
            val process = queue.remove()
            val next = mutableSetOf<Point>()
            for (point in process) {
                val result = oneStep(point)
                next.addAll(result)
            }
            resultList.add(next.size)
            queue.add(next.filter { it != visited.keys }.toSet())
            step--
        }
        return resultList.last()
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