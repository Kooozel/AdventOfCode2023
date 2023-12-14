package main.kooozel.kotlin.day12

import com.google.common.cache.CacheBuilder
import main.kooozel.kotlin.Day
import java.util.concurrent.TimeUnit

class Day12 : Day("12") {
    val input = inputListTest
    override fun partOne(): Any {
        var output = 0
        // Iterate over each row in the file
//        for (entry in input) {
//
//            // Split into the record of .#? record and the 1,2,3 group
//            val (record, rawGroups) = entry.split(" ")
//
//            // Convert the group from string 1,2,3 into a list
//            val groups = rawGroups.split(',').map { it.toInt() }
//
//            output += calc(record, groups)
//
//            // Create a nice divider for debugging
//            println( "-".repeat(10))
//        }
        return output
    }


    fun calc(record: String, groups: List<Int>): Int {

        fun pound(): Int {
            val thisGroupLength = minOf(record.length, groups[0])
            val thisGroup = record.substring(0, thisGroupLength).replace("?", "#")


            if (thisGroup != "#".repeat(groups[0])) {
                return 0
            }

            if (record.length == thisGroupLength) {
                if (groups.size == 1) {
                    return 1
                } else {
                    return 0
                }
            }

            if (record[groups[0]] in "?.") {
                return cache.get(Pair(record.substring(groups.first() + 1), groups.drop(1))) {
                    calc(record.substring(groups.first() + 1), groups.drop(1))
                }
            }

            return 0
        }

        fun dot(): Int {
            return cache.get(Pair(record.substring(1), groups)) {
                calc(record.substring(1), groups)
            }
        }

        if (groups.isEmpty()) {
            if ('#' !in record) {
                return 1
            } else {
                return 0
            }
        }

        if (record.isEmpty()) {
            return 0
        }

        val nextCharacter = record[0]

        val out = when (nextCharacter) {
            '#' -> pound()
            '.' -> dot()
            '?' -> dot() + pound()
            else -> throw RuntimeException()
        }

        println("$record $groups $out")
        return out
    }

    val cache = CacheBuilder.newBuilder()
        .maximumSize(Long.MAX_VALUE)
        .build<Pair<String, List<Int>>, Int>()

    override fun partTwo(): Any {
        var output = 0L
        // Iterate over each row in the file
        for (entry in input) {

            // Split into the record of .#? record and the 1,2,3 group
            val (record, rawGroups) = entry.split(" ")

            // Convert the group from string 1,2,3 into a list
            val groups = rawGroups.split(',').map { it.toInt() }

            output += calc(record.repeat(5), List(5) { groups }.flatten())

            // Create a nice divider for debugging
            println("-".repeat(10))
        }
        return output
    }


}
