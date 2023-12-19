package main.kooozel.kotlin.day19

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.dropBlanks
import main.kooozel.kotlin.parseNumbers

class Day19 : Day("19") {
    val input = inputStringTest
    override fun partOne(): Any {
        val (workflows, parts) = input.split("\n\n")
        val partList = getPartList(parts)
        val workflowMap = getWorkflowMap(workflows)
        return partList.filter { solvePart(it, workflowMap) }
            .fold(0L) { acc: Long, part: Part -> acc + (part.x + part.m + part.a + part.s) }
    }

    private fun getPartList(parts: String): List<Part> =
        parts.lines().map { parseNumbers(it) }
            .map { Part(it[0], it[1], it[2], it[3]) }

    private fun solvePart(part: Part, workflows: Map<String, Workflow>): Boolean {
        var nextLocation = getNextLocation(part, workflows["in"]!!)
        while (nextLocation !in "A,R") {
            nextLocation = getNextLocation(part, workflows[nextLocation]!!)
        }
        return if (nextLocation == "A") true else if (nextLocation == "R") false else throw IllegalArgumentException("No way")

    }

    private fun getNextLocation(part: Part, workflow: Workflow): String {
        for (s in workflow.rest.dropLast(1)) {
            val (condition, location) = s.split(":")
            val property = getProperty(condition.take(1), part)
            if (condition.contains(">")) {
                if (property > condition.split(">")[1].toLong()) {
                    return location
                }
            } else if (condition.contains("<")) {
                if (property < condition.split("<")[1].toLong()) {
                    return location
                }
            }
        }
        return workflow.rest.last()
    }

    private fun getProperty(take: String, part: Part): Long {
        return when (take) {
            "x" -> part.x
            "m" -> part.m
            "a" -> part.a
            "s" -> part.s
            else -> throw IllegalArgumentException("none existing property")
        }
    }


    private fun getWorkflowMap(workflows: String): Map<String, Workflow> =
        workflows.lines().map { it.dropLast(1).split("{") }.map { Workflow(it[0], it[1].split(",").dropBlanks()) }
            .associateBy { it.name }

    override fun partTwo(): Any {
        val (workflows, parts) = input.split("\n\n")
        val workflowMap = getWorkflowMap(workflows)
        val longRange = 1..4000
        val superPart = SuperPart(xmas.associateWith { longRange }.toMutableMap())
        return solveSuperPart(superPart, workflowMap)
    }
    val xmas = setOf("x", "m", "a", "s")
    private fun IntRange.size() = last - start + 1
    val distinctRanges = mutableSetOf<SuperPart>()

    fun calculateResult(superPart: SuperPart): Long {
        println("Intermediate result: $superPart")
        val result = superPart.ranges.values.map { it.size().toLong() }.reduce(Long::times)
        return result
    }
    private fun solveSuperPart(part: SuperPart, workflows: Map<String, Workflow>): Long {

        val currentWorkflow = workflows["in"]


        fun splitRanges(part: SuperPart, s: List<String>): Long {
            val nextCondition = s.first()
            if (nextCondition == "A") {
                distinctRanges.add(part)
                return calculateResult(part)
            } else if (nextCondition == "R") {

                return 0
            }


            if (s.size == 1) {
                return splitRanges(part, workflows[s.last()]!!.rest)
            }

            val (condition, newLocation) = nextCondition.split(":")
            val property = nextCondition.first().toString()
            var less = false
            val number = when {
                condition.contains(">") -> condition.split(">")[1].toLong()
                condition.contains("<") -> {
                    less = true
                    condition.split("<")[1].toLong()
                }

                else -> throw IllegalArgumentException("Invalid condition format")
            }
            val oldRange = part.ranges[property]
            if (!oldRange!!.contains(number)) {
                println("eror")
            }

            val lesserRange = oldRange!!.first..< number.toInt()
            val moreRange = number.toInt()..oldRange.last
            val morePart =
                part.copy(ranges = part.ranges.toMutableMap().apply { this[property] = moreRange })
            val lessPart =
                part.copy(ranges = part.ranges.toMutableMap().apply { this[property] = lesserRange })
            println("S : $s")
            println("Part: $part")
            println("Condtion: $condition")
            println("New Location: $newLocation")
            println("Lesser Part: $lessPart")
            println("More Part: $morePart")


            if (newLocation == "A") {
                distinctRanges.add(if (less) lessPart else morePart)
                return splitRanges(if (less) morePart else lessPart, s.toMutableList().drop(1)) + calculateResult(if (less) lessPart else morePart)
            }

            if (newLocation == "R") {
                return splitRanges(if (less) morePart else lessPart, s.toMutableList().drop(1))
            }

            return splitRanges(if (less) lessPart else morePart, workflows[newLocation]!!.rest) + splitRanges(if (less) morePart else lessPart, s.toMutableList().drop(1))
        }

        val result = splitRanges(part, currentWorkflow!!.rest)
        println(distinctRanges)
        return result
    }

}

private data class Part(val x: Long, val m: Long, val a: Long, val s: Long)
data class SuperPart(var ranges: MutableMap<String, IntRange>)

private data class Workflow(val name: String, val rest: List<String>)