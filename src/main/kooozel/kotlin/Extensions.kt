package main.kooozel.kotlin

import java.awt.Point
fun List<List<*>>.isSafe(at: Point) =
    at.y in this.indices && at.x in this[at.y].indices

operator fun <T> List<List<T>>.get(at: Point): T =
    this[at.y][at.x]

public  fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

private fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0.toLong() && lcm % b == 0.toLong()) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}