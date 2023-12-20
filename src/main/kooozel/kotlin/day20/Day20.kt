package main.kooozel.kotlin.day20

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.dropBlanks
import java.util.*


class Day20 : Day("20") {
    val input = inputList
    private fun parseInput(input: List<String>): List<ElfModule> {
        val result = input.map {
            ElfModule(Type.from(it), createName(it), false, null,getConnected(it), mutableSetOf())
        }
        result.forEach { module ->
            module.connected.forEach { name ->
                if (name != "output") {
                    val connected = result.find { it.name == name }
                    if (connected != null && connected.type == Type.CONJUNCTION) {
                        connected.inputs.add(Pair(module.name, Pulse.LOW))
                    }
                }
            }
        }


        return result
    }

    fun start(listOfModule: List<ElfModule>, pulse: Pulse) {
        val queue = LinkedList<Pair<ElfModule, Pulse>>()

        val module = listOfModule.first { it.type == Type.BROADCAST }
        queue.add(Pair(module, pulse))

        while (queue.isNotEmpty()) {
            val (currentModule, currentPulse) = queue.remove()
            val result = process(currentModule, listOfModule, currentPulse)

            for ((newModule, newPulse) in result) {
                if (newPulse != null) {
                    queue.add(Pair(newModule, newPulse))
                }
            }
        }
    }

    private fun process(
        module: ElfModule,
        listOfModule: List<ElfModule>,
        pulse: Pulse
    ): MutableList<Pair<ElfModule, Pulse?>> {
        val result = mutableListOf<Pair<ElfModule, Pulse?>>()
        for (i in module.connected.indices) {
            val sendTo = listOfModule.find { it.name == module.connected[i] }
            if (sendTo == null) {
                if (pulse == Pulse.LOW) sumLow++ else sumHigh++
//                println("${module.name}  $pulse -> output")
                continue
            }
            if (pulse == Pulse.LOW) sumLow++ else sumHigh++
//            println("${module.name}  $pulse -> ${sendTo.name}")
            result.add(Pair(sendTo, module.sentPulse(pulse, sendTo)))
        }
        return result
    }
    var sumLow = 0L
    var sumHigh= 0L
    val listOfModule = parseInput(input)

    override fun partOne(): Any {
        val listOfModule = parseInput(input)
        println("button - { low -> broadcaster }")
        val cycle = 1000
        for (i in 0 until cycle) {
            println("Cycle ${i + 1}")
            start(listOfModule, Pulse.LOW)
            sumLow ++
            val module = listOfModule.first { it.name.contains("mf") }.inputs
            println(module)
            println("=".repeat(10))
            println(listOfModule.first { it.name.contains("xj") })
//            println("Sum low  = $sumLow, Sum high = $sumHigh")
        }
        return sumLow.times(sumHigh)
    }

    override fun partTwo(): Any {
        val listOfModule = parseInput(input)
        println("button - { low -> broadcaster }")
        var i = 1
        while(!listOfModule.first { it.name.contains("mf") }.inputs.all { it.second == Pulse.HIGH}) {
            println("Cycle ${i}")
            start(listOfModule, Pulse.LOW)
            sumLow++
            println("=".repeat(10))
            println(listOfModule.first { it.name.contains("mf") })
            i++
//            println("Sum low  = $sumLow, Sum high = $sumHigh")
        }


        return i
    }
}

fun createName(input: String): String {
    val name = input.split("->").dropBlanks()[0].trim()
    return when (name.first()) {
        '%', '&' -> name.drop(1)
        else -> name
    }
}

fun getConnected(input: String): List<String> {
    return input.split("->").dropBlanks()[1].split(",").dropBlanks().map { it.trim() }
}

data class ElfModule(
    var type: Type,
    val name: String,
    var on: Boolean,
    var lastPulse: Pulse?,
    var connected: List<String>,
    var inputs: MutableSet<Pair<String, Pulse>>
) {

    fun sentPulse(pulse: Pulse, to: ElfModule): Pulse? {
        to.lastPulse = pulse
        when (to.type) {
            Type.FLIP_FLOP -> return if (pulse == Pulse.HIGH) null else if (to.on) {
                to.on = false; Pulse.LOW
            } else {
                to.on = true; Pulse.HIGH
            }

            Type.CONJUNCTION -> {
                //if not present add
                var pulsePair = to.inputs.first { it.first == this.name }
//                if (pulsePair == null) {
//                    pulsePair = Pair(this.name, Pulse.LOW)
//                    to.inputs.add(pulsePair)
//                }
                to.inputs.remove(pulsePair)
                val updated = pulsePair.copy(pulsePair.first, pulse)
                to.inputs.add(updated)


                if (to.inputs.all { it.second == Pulse.HIGH }) return Pulse.LOW else return Pulse.HIGH
            }

            Type.OUTPUT -> return null

            else -> throw IllegalArgumentException("Unknown destination")
        }
    }
}

enum class Type(val prefix: String) {
    FLIP_FLOP("%"), CONJUNCTION("&"), BROADCAST("b"), OUTPUT("o");

    companion object {
        fun from(input: String): Type {
            return when (input.first().toString()) {
                "%" -> FLIP_FLOP
                "&" -> CONJUNCTION
                "b" -> BROADCAST
                "o" -> OUTPUT
                else -> throw IllegalArgumentException("Type not found")
            }
        }
    }

}

enum class Pulse {
    HIGH, LOW
}
