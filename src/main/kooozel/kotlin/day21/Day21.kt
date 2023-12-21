package main.kooozel.kotlin.day21

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.Point
import java.util.LinkedList

class Day21 : Day("21") {
    val input = inputList
    private val grid = input.flatMapIndexed { index, row ->
        row.mapIndexed { column, it ->
            Garden(
                ItemType.fromChar(it),
                Point(index.toLong(), column.toLong())
            )
        }
    }
    fun oneStep(start: Point, i: Int): MutableSet<Pair<Point, Int>> {
        val setOfPoints = mutableSetOf<Pair<Point, Int>>()
        val newI = i + 1
        for (direction in Direction.entries) {
            val newCoords = start.plus(direction)

            // Wrap around the grid if coordinates go beyond the boundaries
            val wrappedCoords = Point(
                (newCoords.x + maxX) % maxX,
                (newCoords.y + maxY) % maxY
            )

            val newPoint = grid.find { it.coords == wrappedCoords }
            if (newPoint != null && newPoint.type != ItemType.ROCK) {
                setOfPoints.add(Pair(wrappedCoords, newI))
            }
        }
        return setOfPoints
    }


    val steps = 64
    val maxX = grid.maxOf { it.coords.x }
    val maxY = grid.maxOf { it.coords.y }
    override fun partOne(): Any {
        val start = grid.first { it.type == ItemType.START }
        val queue = LinkedList<Pair<Point, Int>>()
        queue.add(Pair(start.coords, 0))
        while (queue.isNotEmpty() && !queue.all { it.second == steps }) {
            val (point, i) = queue.remove()
            val result = oneStep(point, i)
            for ((newPoint, newI) in result) {
                if (newI <= steps && !queue.contains(Pair(newPoint, newI))) {
                    queue.add(Pair(newPoint, newI))
                }
            }
        }
        return queue.size
    }

    override fun partTwo(): Any {
        println(maxX)
        println(maxY)
        println(26501365 % 131)
        return grid.size
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
