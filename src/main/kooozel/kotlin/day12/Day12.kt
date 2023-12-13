package main.kooozel.kotlin.day12

import com.google.common.cache.CacheBuilder
import main.kooozel.kotlin.Day

class Day12 : Day("12") {
    val input = inputListTest
    override fun partOne(): Any {
        return output
    }


    private var output = 0L

    private fun calc(key: Pair<String, List<Int>>): Int {
        var out = cache.getIfPresent(key)
        if (out != null) {
            return out
        } else {
            // Did we run out of groups? We might still be valid
            val groups = key.second
            val record = key.first
            if (groups.isEmpty()) {
                // Make sure there aren't anymore damaged springs, if so, we're valid
                if ('#' !in record) {
                    // This will return true even if record is empty, which is valid
                    return 1
                } else {
                    // More damaged springs that aren't in the groups
                    return 0
                }
            }

            // There are more groups, but no more record
            if (record.isEmpty()) {
                // We can't fit, exit
                return 0
            }
            // Look at the next element in each record and group
            val nextCharacter = record[0]
            val nextGroup = groups[0]

            //         Logic that treats the first character as pound-sign "#"
            fun pound(): Int {

                //If the first is a pound, then the first n characters must be
                //able to be treated as a pound, where n is the first group number
                val thisGroup = record[nextGroup].toString().replace("?", "#")

                //If the next group can't fit all the damaged springs, then abort
                if (thisGroup != "#".repeat(nextGroup)) {
                    return 0
                }

                //If the rest of the record is just the last group, then we're
                //done and there's only one possibility
                if (record.length == nextGroup) {
                    //Make sure this is the last group
                    if (groups.size == 1) {
                        // We are valid
                        return 1
                    } else {
                        //There's more groups, we can't make it work
                        return 0
                    }
                }

                // Make sure the character that follows this group can be a seperator
                if (record[nextGroup] in "?.") {
                    //It can be seperator, so skip it and reduce to the next group
                    return calc(Pair(record.substring(nextGroup + 1), groups.drop(1)))
                }
                //Can't be handled, there are no possibilites
                return 0
            }

            //     Logic that treats the first character as dot "."
            fun dot(): Int {
                // ADD LOGIC HERE ... need to process this character and call
                // calc() on a substring
                return calc(Pair(record.substring(1), groups))
            }

            if (nextCharacter == '#') {
                //Test pound logic
                out = pound()
            } else if (nextCharacter == '.') {
                //Test dot logic
                out = dot()
            } else if (nextCharacter == '?') {
                //This character could be either character, so we'll explore both
                //possibilities
                out = dot() + pound()
            } else {
                throw RuntimeException()
            }

            cache.put(key, out)

            // Help with debugging
            println("$record $groups -> $out")
            return out
        }
    }

    val cache = CacheBuilder.newBuilder().build<Pair<String, List<Int>>, Int>()

    override fun partTwo(): Any {
        for (entry in input) {
            val (record, rawGroups) = entry.split(" ")
            val groups = rawGroups.split(',').map { it.toInt() }
            output += calc(Pair(record, groups))
            println("-".repeat(10))
        }

        return output
    }


}
