package main.kooozel.kotlin.day18

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.plus
import java.awt.Point
import kotlin.math.abs

class Day18: Day("18") {
    val input = inputList


    private fun readInstruction(string: String): Instruction {
        val (direction, height, color) = string.split(" ")

        return Instruction(Direction.from(direction), height.toLong(), (color.drop(1).dropLast(1)))
    }


    private fun getPolygon(input: List<Instruction>, start: Point): MutableList<Pair<Long, Long>> {
        return input.fold(mutableListOf(Pair(start.x.toLong(), start.y.toLong()))) { acc, instruction ->
            acc.apply { add(instruction.direction.plus(last(), instruction.height)) }
        }
    }

    override fun partOne(): Any {
        val instructions = input.map { readInstruction(it) }
        return calculateTrench(instructions)
    }

    private fun calculateTrench(instructions: List<Instruction>): Long {
        val polygon = getPolygon(instructions, Point(0, 0))
        val length = length(polygon)
        val area = area(polygon)
        return area + (length / 2) + 1L
    }

    private fun length(polygon: MutableList<Pair<Long, Long>>) =
        polygon.zipWithNext { point1, point2 -> calculateManhattanDistance(point1, point2) }
            .fold(0L) { acc, distance -> acc + distance }
            .plus(calculateManhattanDistance(polygon.last(), polygon.first()))

    private fun area(polygon: List<Pair<Long, Long>>): Long {
        val n = polygon.size
        var area = 0L
        for (i in 0 until n - 1) {
            area += polygon[i].first * polygon[i + 1].second - polygon[i + 1].first * polygon[i].second
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
            "0" -> Direction.EAST
            "1" -> Direction.SOUTH
            "2" -> Direction.WEST
            "3" -> Direction.NORTH
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

fun calculateManhattanDistance(point1: Pair<Long, Long>, point2: Pair<Long, Long>): Long {
    return abs(point2.first - point1.first) + abs(point2.second - point1.second)
}
