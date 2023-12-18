package main.kooozel.kotlin

import java.awt.Point

enum class Direction {
    UP, LEFT, RIGHT, DOWN;

    companion object {
        fun from(direction: String): Direction {
            return when (direction) {
                "U" -> UP
                "D" -> DOWN
                "R" -> RIGHT
                "L" -> LEFT
                else -> throw IllegalArgumentException()
            }
        }
    }

}

operator fun Direction.plus(current: Point): Point {
    return when (this) {
        Direction.DOWN -> Point(current.x + 1, current.y)
        Direction.RIGHT -> Point(current.x, current.y + 1)
        Direction.UP -> Point(current.x - 1, current.y)
        Direction.LEFT -> Point(current.x, current.y - 1)
    }
}


//TODO: Unify this, +- are different because I am dumb
fun Direction.plus(start: Point, length: Int): Point {
    return when (this) {
        Direction.DOWN -> Point(start.x - length, start.y)
        Direction.RIGHT -> Point(start.x, start.y + length)
        Direction.UP -> Point(start.x + length, start.y)
        Direction.LEFT -> Point(start.x, start.y - length)
    }
}



