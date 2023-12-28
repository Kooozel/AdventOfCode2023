package main.kooozel.kotlin.day24

import com.google.common.collect.Sets
import main.kooozel.kotlin.Day

class Day24: Day("24") {
    val input = inputList
    val hailList = input.map{ row ->
        val (position, velocity) = row.split(" @ ").map { el -> el.split(",").map { it.trim().toLong() } }
        return@map Hail(position, velocity)
    }


    val range = 200000000000000L..400000000000000

    fun intersect(left: Hail, right: Hail): Pair<Long, Long>? {

        val divide = (right.vx * left.vy - right.vy * left.vx).toDouble()
        if (divide.toInt() == 0) {
            return null
        }

        val s = (left.vy*(left.px - right.px) + left.vx*(right.py - left.py)) / divide
        val t = (right.px + s*right.vx - left.px) / left.vx

        if (s < 0 || t < 0) {
            return null
        }

        val x = left.px + left.vx * t
        val y = left.py + left.vy * t
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
        return hailList.size
    }
}

data class Hail(val px: Long, val py: Long, val pz: Long, val vx: Long, val vy: Long, val vz: Long) {
    constructor(position: List<Long>, velocity: List<Long>) : this(position[0], position[1], position[2], velocity[0], velocity[1], velocity[2])
}