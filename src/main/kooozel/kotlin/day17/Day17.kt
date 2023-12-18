package main.kooozel.kotlin.day17

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.isSafe
import main.kooozel.kotlin.plus
import java.awt.Point
import java.util.*


class Day17 : Day("17") {
    val input = inputList

    private val grid = input.map { row -> row.map { it.digitToInt() } }
    private val directions = mapOf(
        Direction.NORTH to setOf(Direction.NORTH, Direction.EAST, Direction.WEST),
        Direction.WEST to setOf(Direction.WEST, Direction.NORTH, Direction.SOUTH),
        Direction.SOUTH to setOf(Direction.SOUTH, Direction.EAST, Direction.WEST),
        Direction.EAST to setOf(Direction.EAST, Direction.NORTH, Direction.SOUTH)
    )

    private fun calculateHeatLoss(minSteps: Int = 1, isValidNextMove: (State, Direction) -> Boolean): Int {
        val goal = Point(grid.first().lastIndex, grid.lastIndex)
        val seen = mutableSetOf<State>()
        val queue = PriorityQueue<Work>()

        State(Point(0, 0), Direction.SOUTH, 0).apply {
            queue += Work(this, 0)
            seen += this
        }

        while (queue.isNotEmpty()) {
            val (current, heatLoss) = queue.poll()
            if (current.location == goal && current.steps >= minSteps) return heatLoss

            directions
                .getValue(current.direction)
                .filter { direction ->
                    val plus = direction.plus(current.location)
                    grid.isSafe(plus)
                }
                .filter { direction -> isValidNextMove(current, direction) }
                .map { direction -> current.next(direction) }
                .filterNot { state -> state in seen }
                .forEach { state ->
                    queue += Work(state, heatLoss + grid[state.location])
                    seen += state
                }

        }
        throw IllegalArgumentException("No route")
    }

    override fun partOne(): Any {
        return calculateHeatLoss { state, nextDirection ->
            state.steps < 3 || state.direction != nextDirection
        }
    }





    private data class State(val location: Point, val direction: Direction, val steps: Int) {
        fun next(nextDirection: Direction): State =
            State(nextDirection.plus(location), nextDirection, if (direction == nextDirection) steps + 1 else 1)
    }

    private data class Work(val state: State, val heatLoss: Int) : Comparable<Work> {
        override fun compareTo(other: Work): Int =
            heatLoss - other.heatLoss
    }


    override fun partTwo(): Any {
        return calculateHeatLoss(4) { state, direction -> if (state.steps > 9) state.direction != direction else if (state.steps < 4) state.direction == direction else true }
    }
}

operator fun <T> List<List<T>>.get(at: Point): T =
    this[at.y][at.x]


