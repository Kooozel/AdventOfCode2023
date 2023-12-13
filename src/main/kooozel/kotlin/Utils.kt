package main.kooozel.kotlin

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: String) = Path("src/main/kooozel/kotlin/day${day}/resources/Day${day}.txt").readLines()
fun readTestInput(day: String) = Path("src/main/kooozel/kotlin/day${day}/resources/Day${day}_test.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun readInputAsString(day: String) = Path("src/main/kooozel/kotlin/day${day}/resources/Day${day}.txt").readText()
fun readTestInputAsString(day: String) = Path("src/main/kooozel/kotlin/day${day}/resources/Day${day}_test.txt").readText()

fun parseNumbers(input: String): List<Long> {
    val pattern = Regex("\\d+")
    return pattern.findAll(input).map { it.value.toLong() }.toList()
}
