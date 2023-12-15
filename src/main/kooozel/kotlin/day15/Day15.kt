package main.kooozel.kotlin.day15

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.dropBlanks

class Day15 : Day("15") {
    val input = inputList

    private fun parseInput(string: String): List<String> {
        return string.split(",").dropBlanks()
    }


    override fun partOne(): Any {
        val result = solveOne(input.get(0))
        return result.sum()
    }

    private fun solveOne(string: String): MutableList<Long> {
        val split = parseInput(string)
        val list = mutableListOf<Long>()
//        split.forEach {
//            var sum = 0L
//            sum += hashAlgo(it, sum)
//            list.add(sum)
//            println("$it sum = $sum")
//        }
        return list
    }

    private fun hashAlgo(s: String, value: Long): Long {
        var sum = value
        for (char in s) {
            sum += char.code
            sum *= 17
            sum %= 256
        }
        return sum
    }

    private val boxes = mutableMapOf<Long, MutableList<Pair<String, Int>>>()


    override fun partTwo(): Any {
        val steps = parseInput(input[0])
        for (step in steps) {
            val operationalChar = step.first { c: Char -> c == '=' || c == '-' }
            val (label, focalLength) = step.split("=", "-")
            val boxNumber = hashAlgo(label, 0)
            println(step)
            when (operationalChar) {
                '-' -> {
                    dash(boxNumber, label)
                }

                '=' -> {
                    equalSign(boxNumber, label, focalLength.toInt())
                }

                else -> {
                    throw Exception()
                }
            }
            println(boxes)
        }

        val resultList = mutableListOf<Long>()
        boxes.forEach{
            for (i in 0 until it.value.size) {
                val element = (it.key + 1) * it.value[i].second * (i + 1).toLong()
                resultList.add(element)
                println(element)
            }

        }

        return resultList.sum()
    }

    private fun equalSign(boxNumber: Long, label: String, focalLength: Int) {
        var box = boxes[boxNumber]
        if (box != null) {
            if (box.find { it.first == label } != null) {
                box = box.map { if (it.first == label) it.copy(label, focalLength) else it }.toMutableList()
                boxes[boxNumber] = box
            } else {
                box.add(box.size, Pair(label, focalLength))
            }
        } else {
            boxes[boxNumber] = mutableListOf(Pair(label, focalLength))
        }
    }

    private fun dash(boxNumber: Long, label: String) {
        var box = boxes[boxNumber]
        if (box != null) {
            box = box.filter { it.first != label }.toMutableList()
            boxes[boxNumber] = box
        }
    }
}