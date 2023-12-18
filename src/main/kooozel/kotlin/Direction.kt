package main.kooozel.kotlin

import java.awt.Point

enum class Direction {
    NORTH, WEST, EAST, SOUTH;

    companion object {
        fun from(direction: String): Direction {
            return when (direction) {
                "U" -> NORTH
                "D" -> SOUTH
                "R" -> EAST
                "L" -> WEST
                else -> throw IllegalArgumentException()
            }
        }
    }

}

operator fun Direction.plus(current: Point): Point {
    return when (this) {
        Direction.SOUTH -> Point(current.x + 1, current.y)
        Direction.EAST -> Point(current.x, current.y + 1)
        Direction.NORTH -> Point(current.x - 1, current.y)
        Direction.WEST -> Point(current.x, current.y - 1)
    }
}


//TODO: Unify this, +- are different because I am dumb
fun Direction.plus(start: Point, length: Int): Point {
    return when (this) {
        Direction.SOUTH -> Point(start.x - length, start.y)
        Direction.EAST -> Point(start.x, start.y + length)
        Direction.NORTH -> Point(start.x + length, start.y)
        Direction.WEST -> Point(start.x, start.y - length)
    }
}

fun Direction.plus(start: Pair<Long, Long>, length: Long): Pair<Long, Long> {
    val x = start.first
    val y = start.second
    return when (this) {
        Direction.SOUTH -> Pair(x - length, y)
        Direction.EAST -> Pair(x, y + length)
        Direction.NORTH -> Pair(x + length, y)
        Direction.WEST -> Pair(x, y - length)
    }
}

