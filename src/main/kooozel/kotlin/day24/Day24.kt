package main.kooozel.kotlin.day24

import com.google.common.collect.Sets
import main.kooozel.kotlin.Day
import java.math.BigDecimal

class Day24 : Day("24") {
    val input = inputListTest
    val hailList = input.map { row ->
        val (position, velocity) = row.split(" @ ").map { el -> el.split(",").map { it.trim().toLong() } }
        return@map Hail(position, velocity)
    }


    val range = 200000000000000L..400000000000000

    fun intersect(left: Hail, right: Hail): Pair<Long, Long>? {

        val divide = (right.dx * left.dy - right.dy * left.dx).toDouble()
        if (divide.toInt() == 0) {
            return null
        }

        val s = (left.dy * (left.x - right.x) + left.dx * (right.y - left.y)) / divide
        val t = (right.x + s * right.dx - left.x) / left.dx

        if (s < 0 || t < 0) {
            return null
        }

        val x = left.x + left.dx * t
        val y = left.y + left.dy * t
        return Pair(x.toLong(), y.toLong())

    }

    fun validateIntersect(coords: Pair<Long, Long>): Boolean {
        return coords.first in range && coords.second in range

    }

    override fun partOne(): Any {
        val combi = Sets.combinations(hailList.toSet(), 2)
        return combi.map { intersect(it.first(), it.last()) }.filterNotNull()
            .map { validateIntersect(it) }.filter { it }.size
    }

    override fun partTwo(): Any {
        val first = getLine(hailList[0], hailList[1])
        val second = getLine(hailList[0], hailList[2])
        val third = getLine(hailList[0], hailList[3])
        val fourth = getLine(hailList[0], hailList[4])

        val matrix = arrayOf(first, second, third, fourth)

        solveSystem(matrix)
        val list = mutableListOf<Long>()
        println("Solution:")
        matrix.forEachIndexed { index, row ->
            list.add(Math.round(row[row.size - 1]))
            println("x${index + 1} = ${Math.round(row[row.size - 1])}")
        }

        val (x, y, dx, dy) = list
        val s1 = (dx * (hailList[0].y - y) + dy * (x - hailList[0].x)) / (dy * hailList[0].dx - dx * hailList[0].dy)
        val s2 = (dx * (hailList[1].y - y) + dy * (x - hailList[1].x)) / (dy * hailList[1].dx - dx * hailList[1].dy)
        val t1 =( hailList[0].x + s1 * hailList[0].dx - x) / dx
        val t2 =( hailList[0].x + s2 * hailList[0].dx - x) / dx

        val dz = (hailList[1].z - hailList[0].z + s2 * hailList[1].dz - s1* hailList[0].dz)/ (t2 - t1)
        val z = hailList[0].z + s1 * hailList[0].dz - t1 * dz
        return x + y + z
    }

    private fun getLine(first: Hail, second: Hail): DoubleArray {
        val a = (second.dy - first.dy).toDouble()
        val b = (first.dx - second.dx).toDouble()
        val c = (first.y - second.y).toDouble()
        val d = (second.x - first.x).toDouble()
        val rs = (second.x * second.dy - second.y * second.dx - first.x * first.dy + first.y * first.dx).toDouble()
        return doubleArrayOf(a, b, c, d, rs)
    }

    private fun solveSystem(matrix: Array<DoubleArray>) {
        val rowCount = matrix.size
        val colCount = matrix[0].size - 1
        for (pivot in 0 until rowCount - 1) {
            for (row in pivot + 1 until rowCount) {
                val factor = matrix[row][pivot] / matrix[pivot][pivot]
                for (col in pivot until colCount + 1) {
                    matrix[row][col] -= factor * matrix[pivot][col]
                }
            }
        }

        // Back substitution
        for (row in rowCount - 1 downTo 0) {
            var sum = 0.0
            for (col in row + 1 until colCount) {
                sum += matrix[row][col] * matrix[col][colCount]
            }
            matrix[row][colCount] = (matrix[row][colCount] - sum) / matrix[row][row]
        }
    }
}

data class Hail(val x: Long, val y: Long, val z: Long, val dx: Long, val dy: Long, val dz: Long) {
    constructor(position: List<Long>, velocity: List<Long>) : this(
        position[0],
        position[1],
        position[2],
        velocity[0],
        velocity[1],
        velocity[2]
    )
}