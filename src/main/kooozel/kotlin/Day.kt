package main.kooozel.kotlin

import main.kooozel.kotlin.readInput


abstract class Day(dayNumber: String) {

    // lazy delegate ensures the property gets computed only on first access
    protected val inputList: List<String> by lazy { readInput(dayNumber) }
    protected val inputString: String by lazy { readInputAsString(dayNumber) }
    protected val inputListTest: List<String> by lazy { readTestInput(dayNumber) }
    protected val inputStringTest: String by lazy { readTestInputAsString(dayNumber) }

    abstract fun partOne(): Any

    abstract fun partTwo(): Any
}