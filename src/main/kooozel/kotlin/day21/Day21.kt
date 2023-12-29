package main.kooozel.kotlin.day21

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.Point
import java.util.*
import kotlin.math.sqrt


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
    private val maxX = tile.maxOf { it.coords.x } + 1
    private val maxY = tile.maxOf { it.coords.y } + 1

    private fun findPointType(point: Point): Garden? {
        val coords = tile.map { it.coords }
        return if (point in coords) {
            tile.find { it.coords == point && it.type != ItemType.ROCK }
        } else {
            val abstractPoint = recalculate(point)
            tile.find { it.coords == abstractPoint && it.type != ItemType.ROCK }
        }

    }

    fun recalculate(point: Point): Point {

        // -1 -> 10
        // -2 -> 9
        // 1 -> 0
        //
        var x = point.x
        if (x !in 0..<maxX) {
            x = (x % maxX + maxX) % maxX
        }
        var y = point.y
        if (y !in 0..<maxY) {
            y = (y % maxY + maxY) % maxY
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
        visited.add(start)

        return result
    }

    private val visited = mutableListOf<Point>()
    private val resultList = mutableListOf<Int>()
    private val intialSteps = 10

    override fun partOne(): Any {
        val start = tile.first { it.type == ItemType.START }
        val queue = LinkedList<Set<Point>>()
        queue.add(setOf(start.coords))
        var step = intialSteps
        var i = 0
        while (queue.isNotEmpty() && step > 0) {
            val process = queue.remove()
            val next = mutableSetOf<Point>()
            for (point in process) {
                val result = oneStep(point)
                next.addAll(result)
            }
            val toAdd = next.filter { it !in visited }.toSet()
            resultList.add(toAdd.size)
            queue.add(toAdd)
            step--
            i++
            println(
                "Result after ${i} steps: ${
                    if (i % 2 == 0) resultList.filterIndexed { index, _ -> index % 2 != 0 }
                        .sum() + 1 else resultList.filterIndexed { index, _ -> index % 2 == 0 }.sum()
                }"
            )
        }
        if (intialSteps % 2 == 0) {
            return resultList.filterIndexed { index, _ -> index % 2 != 0 }.sum() + 1
        } else {
            return resultList.filterIndexed { index, _ -> index % 2 == 0 }.sum()
        }
    }

    val r0 = 3725
    val r1 = 32896
    val r2 = 91055

    override fun partTwo(): Any {

        val a = (r2 + r0 - 2* r1) / 2
        val b = r1 - r0 - a
        val c = r0

        // solve ax2 + bx + c , where x = 202300
        return input.size
    }

    class QuadraticEquationSolver(val a: Double, val b: Double, val c: Double) {
        fun solveForRealRoots(): Pair<Double, Double>? {
            val discriminant = b * b - 4 * a * c
            if (discriminant < 0) {
                // No real roots
                return null
            }
            val root1 = (-b + sqrt(discriminant)) / (2 * a)
            val root2 = (-b - sqrt(discriminant)) / (2 * a)
            return Pair(root1, root2)
        }
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