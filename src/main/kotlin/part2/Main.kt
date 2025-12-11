package part2

import utils.Utils

fun main() {
    val machines = Utils.loadLines("day10.txt").map { createMachine(it) }
    println(machines.sumOf { it.shortestButtonCombination() })
}


private fun createMachine(line: String): Machine {
    val splitted = line.split(" ");
    val wiring = splitted.drop(1).dropLast(1).let { readWiring(it) }
    val joltage =
        splitted
            .last()
            .drop(1)
            .dropLast(1)
            .split(",")
            .map { it.toInt() }
            .foldIndexed(mapOf<Int, Int>()) { index, acc, next -> acc + (index to next) }
    return Machine(wiring, joltage)
}

private fun readWiring(wiring: List<String>): List<List<Int>> {
    return wiring.map(::readSingleWiring)
}

private fun readSingleWiring(singleWiring: String): List<Int> {
    return singleWiring
        .drop(1)
        .dropLast(1)
        .split(",")
        .map { it.toInt() }
}

internal data class Machine(val wiring: List<List<Int>>, val joltage: Map<Int, Int>) {
    fun shortestButtonCombination(): Int {
        var combinations =  setOf(Combination(wiring, mapOf(), joltage))
        while (combinations.none { it.isFinished() }) {
            combinations = combinations.flatMap { it.grow() }.filter { !it.hasOvershoot() }.toSet()
            println(combinations.size )
        }
        println("chociaż coś")
        return combinations.last().moves.values.sum();
    }
}

internal data class Combination(
    val wiring: List<List<Int>>,
    val moves: Map<List<Int>, Int>,
    val joltage: Map<Int, Int>
) {
    fun grow(): List<Combination> {
        val stateFromMoves = stateFromMoves()
        val highestJoltage = joltage.entries.filter { (index, joltage) ->
            (stateFromMoves[index] ?: 0) < joltage
        }.maxBy { (_, joltage) -> joltage }
        return wiring.filter { it.contains(highestJoltage.key) }.map { Combination(wiring,
            moves + (it to (moves[it] ?: 0) + 1), joltage) }
    }

    fun hasOvershoot(): Boolean {
        val stateFromMoves = stateFromMoves()
        return stateFromMoves.any { (key, value) ->
            val expected = joltage[key]
            expected == null || value > expected
        }
    }

    fun isFinished(): Boolean {
        val stateFromMoves = stateFromMoves()
        return stateFromMoves == joltage;
    }

    private fun stateFromMoves(): Map<Int, Int> = moves.entries.fold(emptyMap<Int, Int>()) { acc, (key,value) ->
        key.fold(acc) { acc, next ->
            val currentValue = acc[next]
            if (currentValue != null) acc + (next to currentValue + value) else acc + (next to value)
        }
    }
}
