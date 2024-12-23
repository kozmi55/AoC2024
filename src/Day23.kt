import java.io.File
import java.io.InputStream

fun day23part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input23.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val edges = lineList.map { line ->
        val values = line.split("-")
        Edge(values.first(), values.last())
    }

    val adjacencyList = mutableMapOf<String, MutableSet<String>>()
    for (edge in edges) {
        adjacencyList.computeIfAbsent(edge.start) { mutableSetOf() }.add(edge.end)
        adjacencyList.computeIfAbsent(edge.end) { mutableSetOf() }.add(edge.start)
    }

    val lanParties = mutableSetOf<Set<String>>()
    for (edge in edges) {
        val a = edge.start
        val b = edge.end

        val neighborsA = adjacencyList[a] ?: continue
        val neighborsB = adjacencyList[b] ?: continue

        val commonNeighbors = neighborsA.intersect(neighborsB)
        for (c in commonNeighbors) {
            lanParties.add(setOf(a, b, c))
        }
    }

    val result = lanParties.filter { lanP -> lanP.any { it.first() == 't' } }.size

    println(result)
}

fun day23part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input23.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val edges = lineList.map { line ->
        val values = line.split("-")
        Edge(values.first(), values.last())
    }

    val adjacencyList = mutableMapOf<String, MutableSet<String>>()
    for (edge in edges) {
        adjacencyList.computeIfAbsent(edge.start) { mutableSetOf() }.add(edge.end)
        adjacencyList.computeIfAbsent(edge.end) { mutableSetOf() }.add(edge.start)
    }

    val nodes = adjacencyList.keys
    val maximalCliques = mutableListOf<Set<String>>()

    fun bronKerbosch(r: MutableSet<String>, p: MutableSet<String>, x: MutableSet<String>) {
        if (p.isEmpty() && x.isEmpty()) {
            maximalCliques.add(r.toSet())
            return
        }

        val pivot = (p + x).first()
        val nonNeighbors = p - (adjacencyList[pivot] ?: emptySet()).toSet()

        for (node in nonNeighbors.toSet()) { // Iterate over a copy since we modify `p`
            val newR = r.toMutableSet()
            newR.add(node)

            val newP = p.intersect((adjacencyList[node] ?: emptySet()).toSet()).toMutableSet()
            val newX = x.intersect((adjacencyList[node] ?: emptySet()).toSet()).toMutableSet()

            bronKerbosch(newR, newP, newX)

            p.remove(node)
            x.add(node)
        }
    }

    bronKerbosch(mutableSetOf(), nodes.toMutableSet(), mutableSetOf())

    val result = maximalCliques.maxBy { it.size }.sorted().joinToString(",")

    println(result)
}

data class Edge(val start: String, val end: String)