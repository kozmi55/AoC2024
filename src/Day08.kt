import java.io.File
import java.io.InputStream

fun day08part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input8.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    val antennas = mutableMapOf<Char, Set<Point>>()

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val current = grid[i][j]

            if (current != '.') {
                antennas.compute(current) { _, value ->
                    value?.plus(Point(j, i)) ?: setOf(Point(j, i))
                }
            }
        }
    }

    val corner = Point(grid.first().size - 1, grid.size - 1)
    val antinodes = mutableSetOf<Point>()

    antennas.values.forEach { set ->
        set.forEach { first ->
            set.forEach { second ->
                if (first != second) {
                    val firstDist = first - second
                    val firstAntinode = second - firstDist
                    if (firstAntinode.isInBounds(corner)) antinodes.add(firstAntinode)

                    val secondDist = second - first
                    val secondAntinode = first - secondDist
                    if (secondAntinode.isInBounds(corner)) antinodes.add(secondAntinode)
                }
            }
        }
    }

    println(antinodes.size)
}

fun day08part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input8.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    val antennas = mutableMapOf<Char, Set<Point>>()

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val current = grid[i][j]

            if (current != '.') {
                antennas.compute(current) { _, value ->
                    value?.plus(Point(j, i)) ?: setOf(Point(j, i))
                }
            }
        }
    }

    val corner = Point(grid.first().size - 1, grid.size - 1)
    val antinodes = mutableSetOf<Point>()

    antennas.values.forEach { set ->
        set.forEach { first ->
            set.forEach { second ->
                if (first != second) {
                    val firstDist = first - second
                    var firstAntinode = second - firstDist
                    while (firstAntinode.isInBounds(corner)) {
                        antinodes.add(firstAntinode)
                        firstAntinode -= firstDist
                    }

                    val secondDist = second - first
                    var secondAntinode = first - secondDist
                    while (secondAntinode.isInBounds(corner)) {
                        antinodes.add(secondAntinode)
                        secondAntinode -= secondDist
                    }
                }
            }
        }
    }

    val antennaAntinodes = antennas.values.filter { it.size > 1 }.flatten()
    antinodes.addAll(antennaAntinodes)

    println(antinodes.size)
}

data class Point(val x: Int, val y: Int) {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    fun isInBounds(corner: Point) = x >= 0 && x <= corner.x && y >= 0 && y <= corner.y
}