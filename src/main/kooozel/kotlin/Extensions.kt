package main.kooozel.kotlin

import java.awt.Point
fun List<List<*>>.isSafe(at: Point) =
    at.y in this.indices && at.x in this[at.y].indices

operator fun <T> List<List<T>>.get(at: Point): T =
    this[at.y][at.x]