package main.kooozel.kotlin.day16

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.getGridMap
import java.awt.Point

class Day16 : Day("16") {
    val input = inputString

    val grid = getGridMap(input)
    val newGrid = mutableSetOf<Point>()
    val resultList = mutableListOf<Int>()
    val beamSet = mutableSetOf<Pair<Point, Direction>>()
    override fun partOne(): Any {
        launchBeam(Point(0, 0), Direction.EAST)
        printMap(grid)

        return newGrid.size
    }

    private fun printMap(grid: MutableMap<Point, Char>) {
        for (i in 0 until grid.keys.maxOf { it.x } + 1) {

            val s = grid.entries.filter { it.key.x == i }.map { if (it.key in newGrid) "#" else "." }.joinToString("")
            println(s)
        }
    }

    var beamNumber = 0;

    private fun launchBeam(start: Point, direction: Direction) {
        val points = mutableSetOf<Point>() // Use a set to quickly check for duplicates
        var current = start
        var nextDirection = direction
        if (!beamSet.add(Pair(start, direction))) {
            println("Beam already started ")
        } else {
            beamNumber++
            var beamCycle = 0
            while (grid[current] != null) {
                if (!points.add(current)) beamCycle++
                val next = step(current, nextDirection, beamNumber)

                if (next == null || next == start || beamCycle == 100) {
                    break
                }

                current = next.first
                nextDirection = next.second
            }
            newGrid.addAll(points)
        }
    }

    private fun step(current: Point, direction: Direction, beamNumber: Int): Pair<Point, Direction>? {
        val nextPoint = getNext(current, direction)
        var nextDirection: Direction? = null
        when (grid[nextPoint]) {
            '.' -> nextDirection = dot(direction)
            '/' -> nextDirection = mirror(direction, true)
            '\\' -> nextDirection = mirror(direction, false)
            '-' -> nextDirection = splitter(direction, true, nextPoint)
            '|' -> nextDirection = splitter(direction, false, nextPoint)
            null -> return null
        }
        return if (nextDirection == null) {
            println("Beam End")
            null
        } else {
            println("$beamNumber $nextPoint $nextDirection")
            Pair(nextPoint, nextDirection)
        }
    }

    private fun splitter(direction: Direction, rotation: Boolean, point: Point): Direction? {
        var nextDirection = direction
        println("Spiltter $direction $point $rotation")
        when (direction) {
            Direction.EAST -> if (rotation) nextDirection = Direction.EAST else {
                launchBeam(point, Direction.SOUTH)
                launchBeam(point, Direction.NORTH)
                return null
            }

            Direction.NORTH -> if (rotation) {
                launchBeam(point, Direction.EAST); launchBeam(point, Direction.WEST);return null
            } else nextDirection = Direction.NORTH

            Direction.WEST -> if (rotation) nextDirection = Direction.WEST else {
                launchBeam(point, Direction.NORTH); launchBeam(point, Direction.SOUTH);return null
            }

            Direction.SOUTH -> if (rotation) {
                launchBeam(point, Direction.WEST); launchBeam(point, Direction.EAST);return null
            } else nextDirection = Direction.SOUTH
        }
        return nextDirection
    }

    private fun mirror(direction: Direction, rotation: Boolean): Direction {
        println("mirror")
        val nextDirection: Direction = when (direction) {
            Direction.EAST -> if (rotation) Direction.NORTH else Direction.SOUTH
            Direction.NORTH -> if (rotation) Direction.EAST else Direction.WEST
            Direction.WEST -> if (rotation) Direction.SOUTH else Direction.NORTH
            Direction.SOUTH -> if (rotation) Direction.WEST else Direction.EAST
        }
        return nextDirection
    }

    private fun dot(direction: Direction): Direction {
        return direction;
    }

    private fun getNext(current: Point, direction: Direction): Point {
        return when (direction) {
            Direction.SOUTH -> Point(current.x + 1, current.y)
            Direction.EAST -> Point(current.x, current.y + 1)
            Direction.NORTH -> Point(current.x - 1, current.y)
            Direction.WEST -> Point(current.x, current.y - 1)
        }
    }

    override fun partTwo(): Any {
        val horizontalMax = grid.entries.maxOf { it.key.x } + 1
        val verticalMax = grid.entries.maxOf { it.key.y } + 1

        addBeamResults(0 until horizontalMax, 0) { i -> Point(i, 0) to Direction.EAST }
        addBeamResults(0 until horizontalMax, verticalMax - 1) { i -> Point(i, verticalMax - 1) to Direction.WEST }
        addBeamResults(0 until verticalMax, 0) { i -> Point(0, i) to Direction.SOUTH }
        addBeamResults(0 until verticalMax, horizontalMax - 1) { i -> Point(horizontalMax - 1, i) to Direction.NORTH }

        return resultList.max()
    }

    private fun addBeamResults(
        range: IntRange,
        fixedCoordinate: Int,
        startPointProvider: (Int) -> Pair<Point, Direction>
    ) {
        for (i in range) {
            beamSet.clear()
            newGrid.clear()
            val (startPoint, direction) = startPointProvider(i)
            launchBeam(startPoint, direction)
            resultList.add(newGrid.size)
            newGrid.clear()
        }
    }
}