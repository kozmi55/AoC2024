import java.io.File
import java.io.InputStream
import java.util.ArrayDeque

fun day12part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input12.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    val processed = mutableSetOf<Point>()
    val listOfAreas = mutableListOf<Pair<Char, Int>>()
    val listOfPerimeters = mutableListOf<Pair<Char, Int>>()
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (!processed.contains(Point(j, i))) {
                val first = Point(j, i)
                val firstChar = grid[first.y][first.x]
                val pointsQueue = ArrayDeque(listOf(first))
                var perimeter = 0
                var area = 1
                while (pointsQueue.isNotEmpty()) {
                    val currentPoint = pointsQueue.removeFirst()
                    processed.add(currentPoint)
                    directions.forEach {
                        try {
                            val neighbour = currentPoint + it
                            if (grid[neighbour.y][neighbour.x] == firstChar) {
                                if (!processed.contains(neighbour) && !pointsQueue.contains(neighbour)) {
                                    area++
                                    pointsQueue.add(neighbour)
                                }
                            } else {
                                perimeter++
                            }
                        } catch (e: Exception) {
                            perimeter++
                        }
                    }
                }
                listOfAreas.add(Pair(firstChar, area))
                listOfPerimeters.add(Pair(firstChar, perimeter))
            }
        }
    }

    val result = listOfAreas.zip(listOfPerimeters) { first, second ->
        first.second * second.second
    }.sum()

    println(result)
}

fun day12part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input12.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    val processed = mutableSetOf<Point>()
    val listOfAreas = mutableListOf<Pair<Char, Int>>()
    val listOfPerimeters = mutableListOf<Pair<Char, Int>>()
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (!processed.contains(Point(j, i))) {
                val first = Point(j, i)
                val firstChar = grid[first.y][first.x]
                val pointsQueue = ArrayDeque(listOf(first))
                val previousFences = mutableSetOf<Fence>()
                var perimeter = 0
                var area = 1
                while (pointsQueue.isNotEmpty()) {
                    val currentPoint = pointsQueue.removeFirst()
                    processed.add(currentPoint)
                    directions.forEach { direction ->
                        val neighbour = currentPoint + direction
                        try {
                            if (grid[neighbour.y][neighbour.x] == firstChar) {
                                if (!processed.contains(neighbour) && !pointsQueue.contains(neighbour)) {
                                    area++
                                    pointsQueue.add(neighbour)
                                }
                            } else {
                                val currentFence = Fence(currentPoint, direction, direction.horizontal())
                                if (previousFences.none { isNeighbouringFences(currentFence, it) }) {
                                    perimeter++
                                }
                                previousFences.add(currentFence)
                            }
                        } catch (e: Exception) {
                            val currentFence = Fence(currentPoint, direction, direction.horizontal())
                            if (previousFences.none { isNeighbouringFences(currentFence, it) }) {
                                perimeter++
                            }
                            previousFences.add(currentFence)
                        }
                    }
                }
                listOfAreas.add(Pair(firstChar, area))
                listOfPerimeters.add(Pair(firstChar, perimeter))
            }
        }
    }

    val result = listOfAreas.zip(listOfPerimeters) { first, second ->
        first.second * second.second
    }.sum()

    println(result)
}

fun isNeighbouringFences(first: Fence, second: Fence): Boolean {
    if ((second.horizontal != first.horizontal) || (first.direction != second.direction)) return false

    return if (first.horizontal) {
        (first.point.x + 1 == second.point.x || first.point.x - 1 == second.point.x) && first.point.y == second.point.y
    } else {
        (first.point.y + 1 == second.point.y || first.point.y - 1 == second.point.y) && first.point.x == second.point.x
    }
}

data class Fence(val point: Point, val direction: Point, val horizontal: Boolean)

private val directions = listOf(
    Point(-1, 0),
    Point(0, 1),
    Point(1, 0),
    Point(0, -1)
)

private fun Point.horizontal(): Boolean = x == 0