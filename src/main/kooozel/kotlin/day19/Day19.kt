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
        return partList.filter { solvePart(it, workflowMap) }.fold(0L){ acc: Long, part: Part -> acc + (part.x + part.m + part.a + part.s) }
    }

    private fun getPartList(parts: String): List<Part> =
        parts.lines().map { parseNumbers(it) }
            .map { Part(it[0], it[1], it[2], it[3]) }

    private fun solvePart(part: Part, workflows: Map<String, Workflow>): Boolean {
        var nextLocation = getNextLocation(part, workflows["in"]!!)
        println(nextLocation)
        while (nextLocation !in "A,R") {
            nextLocation = getNextLocation(part, workflows[nextLocation]!!)
            println(nextLocation)
        }
        return if(nextLocation == "A") true else if(nextLocation == "R") false else throw IllegalArgumentException("No way")

    }

    private fun getNextLocation(part: Part ,workflow: Workflow): String {
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
        val longRange = 1L..4000L
        val superPart = SuperPart(longRange, longRange, longRange, longRange )
        return input.length
    }
}

private data class Part(val x: Long, val m: Long, val a: Long, val s: Long)
private data class SuperPart(val x: LongRange, val m: LongRange, val a: LongRange, val s: LongRange)

private data class Workflow(val name: String, val rest: List<String>)