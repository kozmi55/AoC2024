import java.io.File
import java.io.InputStream

fun day20part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input20.txt").inputStream()

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

    val visitedPoints = mutableMapOf<Point, Int>()
    val points = ArrayDeque(listOf(startPos))
    var distance = 0
    while (points.isNotEmpty()) {
        val current = points.removeFirst()
        visitedPoints[current] = distance
        distance++
        directions.forEach { direction ->
            val next = current + direction
            if (!visitedPoints.containsKey(next) && grid[next.y][next.x] == '.') {
                points.addLast(next)
            }
        }
    }
    val trackDistance = distance + 1
    visitedPoints[endPos] = distance

    val savedList = mutableListOf<Int>()
    visitedPoints.forEach { (point, distanceFromStart) ->
        directions.forEach { direction ->
            val cheatPoint = point + (direction * 2)
            val cheatChar = try {
                grid[cheatPoint.y][cheatPoint.x]
            } catch (e: Exception) {
                // Handle out of bounds
                'x'
            }
            if (cheatChar == '.' || cheatChar == 'E') {
                val cheatPointDistanceToEnd = trackDistance - visitedPoints[cheatPoint]!!
                if (trackDistance - distanceFromStart - 2 > cheatPointDistanceToEnd) {
                    val saved = trackDistance - distanceFromStart - cheatPointDistanceToEnd - 2
                    savedList.add(saved)
                }
            }
        }
    }

    val result = savedList.count { it >= 100 }

    println(result)
}

fun day20part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input20.txt").inputStream()

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

    val visitedPoints = mutableMapOf<Point, Int>()
    val points = ArrayDeque(listOf(startPos))
    var distance = 0
    while (points.isNotEmpty()) {
        val current = points.removeFirst()
        visitedPoints[current] = distance
        distance++
        directions.forEach { direction ->
            val next = current + direction
            if (!visitedPoints.containsKey(next) && grid[next.y][next.x] == '.') {
                points.addLast(next)
            }
        }
    }
    val trackDistance = distance + 1
    visitedPoints[endPos] = distance

    val savedList = mutableListOf<Int>()
    visitedPoints.forEach { (point, distanceFromStart) ->
        val possibleCheatPoints = visitedPoints.filter { point.manhattanDistance(it.key) in 2..20 }
        possibleCheatPoints.forEach { (possibleCheat, cheatPointDistanceFromStart) ->
            val cheatChar = grid[possibleCheat.y][possibleCheat.x]
            if (cheatChar == '.' || cheatChar == 'E') {
                val cheatPointDistanceToEnd = trackDistance - cheatPointDistanceFromStart
                val cheatLength = point.manhattanDistance(possibleCheat)
                if (trackDistance - distanceFromStart - cheatLength > cheatPointDistanceToEnd) {
                    val saved = trackDistance - distanceFromStart - cheatPointDistanceToEnd - cheatLength
                    savedList.add(saved)
                }
            }
        }
    }

    val result = savedList.count { it >= 100 }
    println(result)
}

private val directions = listOf(
    Point(-1, 0),
    Point(0, 1),
    Point(1, 0),
    Point(0, -1)
)