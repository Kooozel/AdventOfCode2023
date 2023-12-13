package main.kooozel.kotlin.day05

import main.kooozel.kotlin.readInput
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min
import kotlin.streams.asSequence
import kotlin.streams.asStream

fun main() {
    fun parseSeedNumbers(input: String): List<Long> {
        val pattern = Regex("\\d+")
        return pattern.findAll(input).map { it.value.toLong() }.toList();
    }

    val seedToSoilList = mutableListOf<Pair<LongRange, LongRange>>()
    val soilToFertilizerList = mutableListOf<Pair<LongRange, LongRange>>()
    val fertilizerToWater = mutableListOf<Pair<LongRange, LongRange>>()
    val waterToLight = mutableListOf<Pair<LongRange, LongRange>>()
    val lightToTemperature = mutableListOf<Pair<LongRange, LongRange>>()
    val temperatureToHumidity = mutableListOf<Pair<LongRange, LongRange>>()
    val humidityToLocation = mutableListOf<Pair<LongRange, LongRange>>()

    fun parse(input: List<String>) {
        var currentList: MutableList<Pair<LongRange, LongRange>>? = null

        for (line in input) {
            when {
                line.startsWith("seed-to-soil map:") -> {
                    currentList = seedToSoilList
                }

                line.startsWith("soil-to-fertilizer map:") -> {
                    currentList = soilToFertilizerList
                }

                line.startsWith("fertilizer-to-water map:") -> {
                    currentList = fertilizerToWater
                }

                line.startsWith("water-to-light map:") -> {
                    currentList = waterToLight
                }

                line.startsWith("light-to-temperature map:") -> {
                    currentList = lightToTemperature
                }

                line.startsWith("temperature-to-humidity map:") -> {
                    currentList = temperatureToHumidity
                }

                line.startsWith("humidity-to-location map:") -> {
                    currentList = humidityToLocation
                }

                line.any { it.isDigit() } -> {
                    val list = parseSeedNumbers(line)
                    currentList?.add(Pair(LongRange(list[0], list[0] + list[2]-1), LongRange(list[1], list[1] + list[2]-1)))
                }
            }
        }
    }

    fun getValue(input: Long, list: List<Pair<LongRange, LongRange>>): Long {
        for ((dest, src) in list) {
            if (input in src) {
                return input + dest.first - src.first
            }
        }
        return input
    }


    fun part1(input: List<String>): Long {
        val time = System.currentTimeMillis()
        val seeds = parseSeedNumbers(input[0])
        parse(input)
        val result = AtomicLong(1000000000000000000L)

        seeds.parallelStream().forEach{

            val soil = getValue(it, seedToSoilList)
            val fertilizer = getValue(soil, soilToFertilizerList)
            val water = getValue(fertilizer, fertilizerToWater)
            val light = getValue(water, waterToLight)
            val temperature = getValue(light, lightToTemperature)
            val humidity = getValue(temperature, temperatureToHumidity)
            val location = getValue(humidity, humidityToLocation)
            result.getAndAccumulate(location, ::min)
        }
        println(System.currentTimeMillis() - time)
        return result.toLong()
    }


    fun part2(input: List<String>): Long {
        val time = System.currentTimeMillis()
        val seeds = parseSeedNumbers(input[0]).chunked(2).map { (start, range) -> start..<start+range }
        parse(input)
        val result = AtomicLong(1000000000000000000L)

        for (seed in seeds) {
            seed.asSequence().asStream().parallel().forEach { it1 ->
                val soil = getValue(it1, seedToSoilList)
                val fertilizer = getValue(soil, soilToFertilizerList)
                val water = getValue(fertilizer, fertilizerToWater)
                val light = getValue(water, waterToLight)
                val temperature = getValue(light, lightToTemperature)
                val humidity = getValue(temperature, temperatureToHumidity)
                val location = getValue(humidity, humidityToLocation)
                result.getAndAccumulate(location, ::min)
            }
        }
        println("It took" + (System.currentTimeMillis() - time).toString())
        return result.toLong();
    }


    val testInput = readInput("05")
    val input = readInput("05")
    println(part2(input))
//    println(part1(input))
}