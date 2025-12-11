package day11.part2

import utils.Utils

fun main() {
    val crucialNodesConnectionsCache = mutableMapOf<String, MutableMap<String, Long>>()
    val lines = Utils.loadLines("day11.txt")
    val devices = lines.associate { definition -> parseDeviceDefiniton(definition,crucialNodesConnectionsCache) } + ("out" to Device("out", emptyList(), crucialNodesConnectionsCache));
    devices.values.forEach { it.connect(devices) }
    val startingPoint = devices["svr"]
    val numOfWaysToDac = startingPoint!!.moveTo("dac", emptyList())
    val numOfWayToFft = startingPoint.moveTo("fft", emptyList())
    val numOfWayFromDacToFft = devices["dac"]!!.moveTo("fft", emptyList())
    val numOfWayFromFftToDac = devices["fft"]!!.moveTo("dac", emptyList())
    val fromDacToEnd = devices["dac"]!!.moveTo("out", emptyList())
    val fromFftToEnd = devices["fft"]!!.moveTo("out", emptyList())
    println(numOfWaysToDac * numOfWayFromDacToFft * fromFftToEnd + numOfWayToFft* numOfWayFromFftToDac * fromDacToEnd )
}

private fun parseDeviceDefiniton(definition: String, cache: MutableMap<String, MutableMap<String, Long>>): Pair<String, Device> {
    val splitted = definition.split(":")
    val name = splitted[0];
    val connections = splitted[1].drop(1).split(" ")
    return name to Device(name, connections, cache);
}


class Device(
    val ownName: String,
    val codesOfConnected: List<String>,
    val cache: MutableMap<String, MutableMap<String, Long>>
) {
    var connectedTo: List<Device>? = null

    fun connect(codeToDevice: Map<String, Device>) {
        connectedTo = codesOfConnected.map { codeToDevice[it]!! }
    }

    fun moveTo(name: String, visited:List<String>): Long {
        if (name == ownName)return 1;
        if (ownName in visited) return 0
        val valFromCache = cache[ownName]?.get(name)
        if (valFromCache != null) {
            return valFromCache

        }
        val moveToVal = connectedTo!!.sumOf {
             it.moveTo(name, visited + ownName)
        };
        cache.putIfAbsent(ownName, mutableMapOf())
        cache[ownName]!!.putIfAbsent(name, moveToVal)
        return moveToVal
    }
}
