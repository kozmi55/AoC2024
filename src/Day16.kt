import java.io.File
import java.io.InputStream
import kotlin.math.abs

fun day16part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input16.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    var endPos = Point(0, 0)
    var startPos = Point(0, 0)
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == 'S') {
                startPos = Point(j, i)
            }
            if (grid[i][j] == 'E') {
                endPos = Point(j, i)
            }
        }
    }

    val pointsToVisit = ArrayDeque(listOf(Pair(startPos, directions[2])))
    val visitedPoints = mutableMapOf<Pair<Point, Point>, Pair<Int, Point>>()
    visitedPoints[Pair(startPos, directions[2])] = Pair(0, directions[2])

    while (pointsToVisit.isNotEmpty()) {
        val currentPoint = pointsToVisit.removeFirst()
        val currentDirection = currentPoint.second
        val currentDistance = visitedPoints[currentPoint]!!.first
        directions.forEach { direction ->
            val rotations = when {
                direction == currentDirection -> 0
                abs(direction.x) == abs(currentDirection.x) -> 2
                else -> 1
            }
            val next = Pair(currentPoint.first + direction, direction)
            if (grid[next.first.y][next.first.x] == '.' || grid[next.first.y][next.first.x] == 'E') {
                val possibleDistance = currentDistance + rotations * 1000 + 1
                visitedPoints.compute(next) { _, value ->
                    if (value == null || possibleDistance < value.first) {
                        pointsToVisit.addLast(next)
                        Pair(possibleDistance, direction)
                    } else {
                        value
                    }
                }
            }
        }
    }

    val result = directions.minOf {
        visitedPoints[Pair(endPos, it)]?.first ?: Int.MAX_VALUE
    }
    println(result)
}

fun day16part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input16test.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    var endPos = Point(0, 0)
    var startPos = Point(0, 0)
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == 'S') {
                startPos = Point(j, i)
            }
            if (grid[i][j] == 'E') {
                endPos = Point(j, i)
            }
        }
    }

    val pointsToVisit = ArrayDeque(listOf(Pair(startPos, directions[2])))
    val visitedPoints = mutableMapOf<Pair<Point, Point>, Int>()
    val parents = mutableMapOf<Point, List<Point>>()
    visitedPoints[Pair(startPos, directions[2])] = 0

    while (pointsToVisit.isNotEmpty()) {
        val currentPoint = pointsToVisit.removeFirst()
        val currentDirection = currentPoint.second
        val currentDistance = visitedPoints[currentPoint]!!
        directions.forEach { direction ->
            val rotations = when {
                direction == currentDirection -> 0
                abs(direction.x) == abs(currentDirection.x) -> 2
                else -> 1
            }
            val next = Pair(currentPoint.first + direction, direction)
            if (grid[next.first.y][next.first.x] == '.' || grid[next.first.y][next.first.x] == 'E') {
                val possibleDistance = currentDistance + rotations * 1000 + 1
                visitedPoints.compute(next) { _, value ->
                    if (value == null || possibleDistance < value) {
                        pointsToVisit.addLast(next)
                        parents.compute(next.first) { _, parentsValue ->
                            parentsValue?.plus(currentPoint.first) ?: listOf(currentPoint.first)
                        }
                        possibleDistance
                    } else {
                        if (possibleDistance == value) {
                            parents.compute(next.first) { _, parentsValue ->
                                parentsValue?.plus(currentPoint.first)
                            }
                        }
                        value
                    }
                }
            }
        }
    }

    parents.forEach {
        println(it)
    }

    val parentsToVisit = ArrayDeque(listOf(endPos))
    val pointsInPath = mutableSetOf<Point>()
    val visitedPointsWithoutDirection = mutableMapOf<Point, Int>()
    visitedPoints.forEach { (key, value) ->
        visitedPointsWithoutDirection.compute(key.first) { k, v ->
            if (v == null || v > value) value else v
        }
    }
    while (parentsToVisit.isNotEmpty()) {
        val current = parentsToVisit.removeFirst()
        pointsInPath.add(current)
        val shortestDistance = parents[current]?.minOf { visitedPointsWithoutDirection[it] ?: Int.MAX_VALUE }
        val allParents = parents[current]?.filter { visitedPointsWithoutDirection[it] == shortestDistance }
        allParents?.forEach {
            if (!pointsInPath.contains(it)) parentsToVisit.addLast(it)
        }
    }

    val result = pointsInPath.size
    println(result)
}

data class MazePoint(val parents: List<Point> = emptyList(), val distanceFromStart: Int)

private val directions = listOf(
    Point(-1, 0),
    Point(0, 1),
    Point(1, 0),
    Point(0, -1)
)