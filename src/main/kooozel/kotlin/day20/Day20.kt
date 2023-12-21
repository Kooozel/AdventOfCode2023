package main.kooozel.kotlin.day20

import main.kooozel.kotlin.Day
import main.kooozel.kotlin.dropBlanks
import main.kooozel.kotlin.findLCMOfListOfNumbers
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

    val lcmInput = mutableMapOf<String, Long>()

    fun start(listOfModule: List<ElfModule>, pulse: Pulse, parents: List<ElfModule>) {
        val queue = LinkedList<Signal>()

        val module = listOfModule.first { it.type == Type.BROADCAST }
        queue.add(Signal(module, pulse, "button"))

        while (queue.isNotEmpty()) {
            val (currentModule, currentPulse, from) = queue.remove()

            for (toWatch in parents) {
                if (currentModule.name == toWatch.name && currentPulse == Pulse.LOW && from == toWatch.inputs.first().first) {
                    lcmInput[currentModule.name] = i.toLong() + 1
                }
            }

            if (lcmInput.size == parents.size) {
                run = false
                break
            }




            val result = processOne(currentModule, currentPulse, from)
            if (result.pulse != null) {
                for (connected in currentModule.connected){
                    val elfModule = listOfModule.find { it.name == connected }
                    if (elfModule == null) {
//                        if (result.pulse == Pulse.LOW) sumLow++ else sumHigh++
                        println("${currentModule.name}  ${result.pulse} -> output")
                        continue
                    }
//                    println("${currentModule.name}  ${result.pulse} -> ${elfModule.name}")
                    queue.add(Signal(elfModule, result.pulse, result.from))
                }
            }
        }
    }

    private fun processOne(currentModule: ElfModule, currentPulse: Pulse?, from: String): Signal{
        if (currentPulse == Pulse.LOW) sumLow++ else sumHigh++
        val newPulse = currentModule.getPulse(currentPulse!!, from)
        return Signal(currentModule, newPulse, currentModule.name)
    }

    var sumLow = 0L
    var sumHigh= 0L

    override fun partOne(): Any {
//        val listOfModule = parseInput(input)
//        println("button - { low -> broadcaster }")
//        val cycle = 1000
//        for (i in 0 until cycle) {
//            println("Cycle ${i + 1}")
//            start(listOfModule,Pulse.LOW)
//            println("=".repeat(10))
//            println("Sum low  = $sumLow, Sum high = $sumHigh")
//        }
        return sumLow.times(sumHigh)
    }

    var i = 0
    var run = true

    override fun partTwo(): Any {
        val listOfModule = parseInput(input)
        val parents = findParents("rx", listOfModule)
        println(parents)

        println("button - { low -> broadcaster }")
        while (run) {
            println("Cycle ${i + 1}")
            start(listOfModule,Pulse.LOW, parents)
            println("=".repeat(10))
//            println("Sum low  = $sumLow, Sum high = $sumHigh")
            i++
        }

        return findLCMOfListOfNumbers(lcmInput.values.toList())
    }

    private fun findParents(name: String, listOfModule: List<ElfModule>): List<ElfModule> {
        var parents = listOfModule.filter { it.connected.contains(name) }
        if (parents.size == 1) {
           parents = findParents(parents[0].name, listOfModule)
        }
        return parents
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

    fun getPulse(incomingPulse: Pulse, from: String?): Pulse? {
        when (this.type) {
            Type.FLIP_FLOP -> return if (incomingPulse == Pulse.HIGH) null else if (this.on) {
                this.on = false; Pulse.LOW
            } else {
                this.on = true; Pulse.HIGH
            }
            Type.CONJUNCTION -> {
                //if not present add
                val pulsePair = this.inputs.first { it.first == from }
                this.inputs.remove(pulsePair)
                val updated = pulsePair.copy(pulsePair.first, incomingPulse)
                this.inputs.add(updated)

                if (this.inputs.all { it.second == Pulse.HIGH }) return Pulse.LOW else return Pulse.HIGH
            }

            Type.OUTPUT -> return null
            Type.BROADCAST -> return incomingPulse

            else -> throw IllegalArgumentException("Unknown destination")
        }
    }
}
data class Signal(val elfModule: ElfModule, val pulse: Pulse?, val from: String)

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
