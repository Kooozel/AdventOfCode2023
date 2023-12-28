package main.kooozel.kotlin.day25

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.dropBlanks
import java.util.Random

class Day25 : Day("25") {
    val input = inputList
    val lines = input.map { row ->
        row.split(":", " ").dropBlanks()
    }

    val nodes = lines.flatten().toSet()
    val edges = lines.flatMap { line -> line.drop(1).map { line.first() to it } }.toSet()
    val component = Component(nodes, edges)
    override fun partOne(): Any {
        return component.solve(Random())
    }

    override fun partTwo(): Any {
        return input.size
    }
}

data class Component(val nodes: Set<String>, val edges: Set<Pair<String, String>>) {
    fun solve(random: Random): Int {
        while (true) {
            val subgroup = nodes.map { mutableSetOf(it) }.toMutableSet()
            val subgroupContaining = {node: String -> subgroup.first { node in it }}

            val randomEdge = edges.shuffled(random).map { it.toList() }.iterator()

            while (subgroup.size > 2) {
                val (s1, s2) = randomEdge.next().map(subgroupContaining)
                if (s1 === s2) continue
                s1.addAll(s2)
                subgroup.removeIf { it === s2 }
            }

            if (edges.count { (a, b) -> subgroupContaining(a) != subgroupContaining(b) } > 3) continue

            return subgroup.map { it.size }.reduce(Int::times)
        }
    }
}