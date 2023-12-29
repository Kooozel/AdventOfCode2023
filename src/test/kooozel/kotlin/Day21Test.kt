
import main.kooozel.kotlin.Point
import main.kooozel.kotlin.day21.Day21
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day21Test {
    val test = Day21()
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
        assertEquals(Point(3, 6), recalculatedPoint)
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
        assertEquals(Point(3, 5), recalculatedPoint)
    }
}