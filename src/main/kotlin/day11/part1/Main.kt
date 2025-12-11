package day11.part1

import utils.Utils

fun main() {
    val lines = Utils.loadLines("day11.txt")
    val devices = lines.associate(::parseDeviceDefiniton);
    devices.values.forEach { it.connect(devices) }
    val startingPoint = devices["svr"]
    println(startingPoint)
    println(startingPoint!!.move())
}

private fun parseDeviceDefiniton(definition: String): Pair<String, Device> {
    val splitted = definition.split(":")
    val name = splitted[0];
    val connections = splitted[1].drop(1).split(" ")
    return name to Device(connections);
}


sealed interface Node{
    fun move():Long
}

data object Out: Node{
    override fun move(): Long = 1;
}

class Device(val codesOfConnected: List<String>) : Node{
    var connectedTo: List<Node>? = null

    fun connect(codeToDevice: Map<String, Node>) {
        connectedTo = codesOfConnected.map { codeToDevice[it] ?:  Out }
    }

    override fun move(): Long {
        return connectedTo!!.sumOf {
            it.move()
        }
    }
}
