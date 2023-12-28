package main.kooozel.kotlin.day23

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.Direction
import main.kooozel.kotlin.Point

class Day23 : Day("23") {
    val input = inputList

    val grid = input.flatMapIndexed { rowIndex, row ->
        row.mapIndexed { index, c ->
            Point(rowIndex.toLong(), index.toLong()) to Hiking(Hiking.HikingType.entries.first { it.char == c })
        }
    }.toMap()

    val visited = mutableSetOf<Point>()
    val conjuction = mutableSetOf<Point>()


    fun validNeighbors(start: Point): List<Pair<Point, Int>> {
        val result = when (grid[start]!!.type) {
            Hiking.HikingType.SLOPE_UP -> listOf(start.plus(Direction.DOWN) to 1)
            Hiking.HikingType.SLOPE_DOWN -> listOf(start.plus(Direction.UP) to 1)
            Hiking.HikingType.SLOPE_LEFT -> listOf(start.plus(Direction.RIGHT) to 1)
            Hiking.HikingType.SLOPE_RIGHT -> listOf(start.plus(Direction.LEFT) to 1)
            else -> {
                Direction.entries.map { start.plus(it) }
                    .filter { grid[it] != null && grid[it]!!.type != Hiking.HikingType.FOREST }
                    .map { it to 1 }
            }
        }

        return result;

    }

    fun findMax(
        current: Point,
        visited: MutableSet<Point>,
        distance: Int = 0,
        validNeighbors: (Point) -> List<Pair<Point, Int>>
    ): Int {
        if (current == end) {
            return distance
        }
        visited.add(current)

        val max = validNeighbors(current).filter { (point, weight) -> point !in visited }
            .maxOfOrNull { (point, weight) ->
                findMax(point, visited, distance + weight, validNeighbors)
            }

        visited.remove(current)

        return max ?: 0
    }

    private val start = Point(0, 1)
    private val end = Point(grid.keys.maxOf { it.x }, grid.keys.maxOf { it.y } - 1)

    override fun partOne(): Any {

        return findMax(current = start, visited = visited, validNeighbors = { current -> validNeighbors(current) })
    }

    override fun partTwo(): Any {
        val junctions = mutableMapOf(
            start to mutableListOf<Pair<Point, Int>>(),
            end to mutableListOf(),
        )
        grid.entries.forEach { (point, type) ->
            if (type.type != Hiking.HikingType.FOREST) {
                val neighbor = Direction.entries.map { point.plus(it) }
                    .filter { grid[it] != null && grid[it]!!.type != Hiking.HikingType.FOREST }
                    .map { it to 1 }

                if (neighbor.size > 2) {
                    junctions[point] = mutableListOf()
                }
            }
        }

        for (junction in junctions.keys) {
            var current = setOf(junction)
            val visited = mutableSetOf(junction)
            var distance = 0

            while (current.isNotEmpty()) {
                distance++
                val newSet = mutableSetOf<Point>()
                current.forEach { point ->
                    Direction.entries.map { point.plus(it) }
                        .filter { grid[it] != null && grid[it]!!.type != Hiking.HikingType.FOREST && it !in visited }
                        .forEach { neighbor ->
                            if (neighbor in junctions) {
                                junctions.getValue(junction).add(neighbor to distance)
                            } else {
                                newSet.add(neighbor)
                                visited.add(neighbor)
                            }
                        }
                }
                current = newSet
            }
        }
        return findMax(
            current = start,
            visited = visited,
            validNeighbors = { current -> junctions.getValue(current) }
        )
    }
}


data class Hiking(val type: HikingType) {
    enum class HikingType(val char: Char) {
        PATH('.'), FOREST('#'), SLOPE_UP('^'), SLOPE_DOWN('v'), SLOPE_LEFT('>'), SLOPE_RIGHT('<')
    }
}