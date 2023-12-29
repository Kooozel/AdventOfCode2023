package main.kooozel.kotlin.day21

import main.kooozel.kotlin.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
class Day21Test {
   private val test = Day21()
    @Test
    fun testRecalculateWithinRange() {
        val point = Point(5, 7)
        val recalculatedPoint = test.recalculate(point)
        assertEquals(Point(5, 7), recalculatedPoint)
    }

    @Test
    fun testRecalculateNegativeX() {
        val point = Point(-2, 5)
        val recalculatedPoint = test.recalculate(point)
        assertEquals(Point(9, 5), recalculatedPoint)
    }

    @Test
    fun testRecalculateNegativeY() {
        val point = Point(3, -4)
        val recalculatedPoint = test.recalculate(point)
        assertEquals(Point(3, 7), recalculatedPoint)
    }

    @Test
    fun testRecalculateOutOfRangeX() {
        val point = Point(12, 5)
        val recalculatedPoint = test.recalculate(point)
        assertEquals(Point(1, 5), recalculatedPoint)
    }

    @Test
    fun testRecalculateOutOfRangeY() {
        val point = Point(3, 15)
        val recalculatedPoint = test.recalculate(point)
        assertEquals(Point(3, 4), recalculatedPoint)
    }

    @Test
    fun testRecalculateOutOfRangeY2() {
        val point = Point(21, -12)
        val recalculatedPoint = test.recalculate(point)
        assertEquals(Point(10, 10), recalculatedPoint)
    }
}