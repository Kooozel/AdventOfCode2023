package main.kooozel.kotlin.day12

import com.google.common.collect.Sets.combinations
import main.kooozel.kotlin.Day

class Day12 : Day("12") {
    val input = inputListTest
    override fun partOne(): Any {
        var sum = 0L
        for (string in input) {
            val split = string.split(" ")
            val numberOfHashes = split[0].count { it == '#' }
            val questionIndicies = mutableListOf<Int>()
            split[0].forEachIndexed { index, char ->
                if (char == '?') {
                    questionIndicies.add(index)
                }
            }
            val list = split[1].split(",").toList().map { it.toInt() }

            sum += generateStrings(list.sum() - numberOfHashes, split[0], questionIndicies, list)
        }
        return sumList.sum()
    }

    val cache = mutableMapOf<Set<Int>, String>()

    private fun isValid(newString: String, list: List<Int>): Boolean {
        return (getGroups(newString) == list)
    }
    private var done = 0
    val sumList = mutableListOf<Long>()

    private fun generateStrings(hashes: Int, string: String, questionIndices: MutableList<Int>, list: List<Int>): Long {

        var sum = 0L
        combinations(questionIndices.toSet(), hashes).forEach {
            val newString = replaceString(it, string, mutableSetOf())
            if (isValid(newString, list)) sum++
        }
        cache.clear()
        done++
        println("Done $done of ${input.size}")
        sumList.add(sum)
        return sum
    }


    private fun replaceString(set: Set<Int>, string: String, dropped: MutableSet<Int>): String {
        val match = findHighMatchingKey(set)
        if (set.isEmpty()) return string


        if (match.isEmpty()) {
            val first = set.first()
            val newString = string.toCharArray().toMutableList()
            newString.removeAt(first)
            newString.add(first, '#')

            val set1 = set.toMutableList().drop(1)
            dropped.add(first)

            // Use dropped set as the key for caching
            cache[dropped.toSet()] = newString.joinToString("")

            return if (set1.isNotEmpty()) {
                replaceString(set1.toSet(), newString.joinToString(""), dropped)
            } else {
                newString.joinToString("")
            }
        } else {
            val set1 = set.toMutableList().drop(match.size)
            // Use the matched set as the key to retrieve the cached value
            val newString = cache[match] ?: ""

            return if (set1.isNotEmpty()) {
                replaceString(set1.toSet(), newString, match.toMutableSet())
            } else {
                newString
            }
        }
    }

    private fun findHighMatchingKey(set: Set<Int>): Set<Int> {
        var mutable = set.toMutableList()

        while (!cache.containsKey(mutable.toSet())) {
            mutable = mutable.dropLast(1).toMutableList()
            if (mutable.isEmpty()) return mutable.toSet()
        }

        return mutable.toSet()
    }


    private fun getGroups(string: String): MutableList<Int> {
        val hashGroups = mutableListOf<Int>()
        val hashPattern = Regex("#+")
        val hashMatches = hashPattern.findAll(string)
        var count = 0

        for (match in hashMatches) {
            val hashGroup = match.value
            count += 1
            hashGroups.add(hashGroup.length)
        }
        return hashGroups
    }


    override fun partTwo(): Any {
        var sum = 0L
        sumList.clear()
        for (string in input) {
            val split = string.split(" ")
            val s = split[0].repeat(5)
            val numberOfHashes = s.count { it == '#' }
            val questionIndicies = mutableListOf<Int>()
            s.forEachIndexed { index, char ->
                if (char == '?') {
                    questionIndicies.add(index)
                }
            }
            var list = split[1].split(",").toList().map { it.toInt() }
            list = List(5) { list }.flatten()

            sum += generateStrings(list.sum() - numberOfHashes, s, questionIndicies, list)
        }
        return sumList.sum()
    }


}
