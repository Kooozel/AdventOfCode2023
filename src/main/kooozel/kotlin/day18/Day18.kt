package main.kooozel.kotlin.day18

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.Point
import main.kooozel.kotlin.Point.calculateManhattanDistance
import main.kooozel.kotlin.plus

import kotlin.math.abs

class Day18: Day("18") {
    val input = inputListTest


    private fun readInstruction(string: String): Instruction {
        val (direction, height, color) = string.split(" ")

        return Instruction(Direction.from(direction), height.toLong(), (color.drop(1).dropLast(1)))
    }


    private fun getPolygon(input: List<Instruction>, start: Point): MutableList<Point> {
        return input.fold(mutableListOf(start)) { acc, instruction ->
            acc.apply { add(last().plus(instruction.direction ,instruction.height)) }
        }
    }

    override fun partOne(): Any {
        val instructions = input.map { readInstruction(it) }
        return calculateTrench(instructions)
    }

    private fun calculateTrench(instructions: List<Instruction>): Long {
        val polygon = getPolygon(instructions, Point(0,0))
        val length = length(polygon)
        val area = area(polygon)
        return area + (length / 2) + 1L
    }

    private fun length(polygon: MutableList<Point>) =
        polygon.zipWithNext { point1, point2 -> calculateManhattanDistance(point1, point2) }
            .fold(0L) { acc, distance -> acc + distance }
            .plus(calculateManhattanDistance(polygon.last(), polygon.first()))

    private fun area(polygon: List<Point>): Long {
        val n = polygon.size
        var area = 0L
        for (i in 0 until n - 1) {
            area += polygon[i].x * polygon[i + 1].y - polygon[i + 1].x * polygon[i].y
        }
        return abs(area) / 2
    }

    override fun partTwo(): Any {
        val instructions = input.map { readInstruction(it) }.map { toNewInstruction(it) }
        return calculateTrench(instructions)
    }

    private fun toNewInstruction(old: Instruction): Instruction {
        val height = old.color.drop(1).take(5).toLong(16)
        val direction = when (old.color.takeLast(1)) {
            "0" -> Direction.RIGHT
            "1" -> Direction.DOWN
            "2" -> Direction.LEFT
            "3" -> Direction.UP
            else -> throw IllegalArgumentException("wrong last char")
        }
        return Instruction(direction, height, old.color)

    }
}

private data class Instruction(val direction: Direction, val height: Long, val color: String) {
    override fun toString(): String {
        return "Instruction(direction='$direction', height=$height, color=$color)"
    }
}