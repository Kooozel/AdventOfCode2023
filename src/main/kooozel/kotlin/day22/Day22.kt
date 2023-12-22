package main.kooozel.kotlin.day22

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.dropBlanks
import java.awt.Point

class Day22: Day("22") {
    private val input = inputList
    private val bricks = input.mapIndexed {index,row -> Brick.from(index, row) }.sortedBy { it.z.first }


    private val supports = mutableMapOf<Int, MutableSet<Int>>()
    private val supported = mutableMapOf<Int, MutableSet<Int>>()

    private fun fall() {
        val maxes = mutableMapOf<Point, Pair<Int, Int>>().withDefault { -1 to 0 }

        for (brick in bricks) {
            val points = brick.points2D()
            val maxZ = points.map { maxes.getValue(it) }.maxOf { it.second }
            println(maxZ)
            brick.z = maxZ + 1..<maxZ + 1 + brick.z.count()

            for (point in points) {
                val (id, z) = maxes.getValue(point)
                if (z == maxZ && id != -1) {
                    supports.getOrPut(id) { mutableSetOf() }.add(brick.id)
                    supported.getOrPut(brick.id) { mutableSetOf() }.add(id)
                }
                maxes[point] = brick.id to brick.z.last
            }

        }
    }
    override fun partOne(): Any {
        fall()
        println(bricks)
        val nonDisintegrated = supports.count { (_, others) -> others.any { other -> supported.getValue(other).size == 1 } }
        return bricks.size - nonDisintegrated
    }

    override fun partTwo(): Any {
        return bricks.sumOf { brick ->
            val falling = mutableSetOf(brick.id)
            var nextBricks: Set<Int> = supports.getOrDefault(brick.id, emptySet())

            while (nextBricks.isNotEmpty()) {
                nextBricks = buildSet {
                    for (next in nextBricks) {
                        if ((supported.getValue(next) - falling).isEmpty()) {
                            falling += next
                        }
                        addAll(supports.getOrDefault(next, emptySet()))
                    }
                }
            }

            falling.size - 1
        }
    }
}

data class Brick(val id: Int, val x: IntRange, val y: IntRange, var z: IntRange){
    fun points2D() = x.flatMap { x -> y.map { y -> Point(x, y ) } }
    companion object {
        fun from(index: Int, row: String): Brick {
            val (start, end) = row.split('~').dropBlanks()
            val (x1, y1, z1) = start.split(',').map { it.toInt() }
            val (x2, y2, z2)  = end.split(',').map { it.toInt() }
            return Brick(index, x1..x2,y1..y2,z1..z2)
        }
    }
}