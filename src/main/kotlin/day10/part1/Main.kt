package day10.part1

import utils.Utils

fun main() {
    val machines = Utils.loadLines("day10.txt").map { createMachine(it) }
    println(machines.sumOf { it.shortestButtonCombination() })
}


private fun createMachine(line: String): Machine {
    val splitted = line.split(" ");
    val desiredState = createDesiredState(splitted.first())
    val wiring = splitted.drop(1).dropLast(1).let { readWiring(it) }
    return Machine(desiredState, wiring)
}

private fun createDesiredState(diagram: String): Set<Int> {
    return diagram
        .drop(1)
        .dropLast(1)
        .mapIndexed { i, char -> if (char == '#') i else -1 }.filter { it != -1 }
        .toSet()
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

internal data class Machine(val desiredState: Set<Int>, val wiring: List<List<Int>>) {
    fun shortestButtonCombination(): Int {
        var combinations = wiring.map { Combination(desiredState, wiring, listOf(it)) }
        while (combinations.none{it.isFinished()}) {
            combinations = combinations.flatMap { it.grow() }
        }
        return combinations.last().moves.size;
    }
}

internal class Combination(val desiredState: Set<Int>, val wiring: List<List<Int>>,val moves: List<List<Int>>) {
    fun grow(): List<Combination> {
       return wiring.map{  Combination(desiredState,wiring, moves + listOf(it))  }
    }

    fun isFinished(): Boolean {
        val stateFromMoves = moves.fold(emptySet<Int>()) { acc, next ->
            next.fold(acc) { acc, next -> if (acc.contains(next)) acc - next else acc + next }
        }
        return stateFromMoves == desiredState;
    }
}
