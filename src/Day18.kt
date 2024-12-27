import java.io.File
import java.io.InputStream

fun day18part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input18.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val blockedPoints = lineList.map {
        val parts = it.split(",")
        Point(parts[0].toInt(), parts[1].toInt())
    }.take(1024)

    val endPos = Point(70, 70)
    val startPos = Point(0, 0)

    val pointsToVisit = ArrayDeque(listOf(startPos))
    val visitedPoints = mutableMapOf(Point(0, 0) to 0)

    while (pointsToVisit.isNotEmpty()) {
        val currentPoint = pointsToVisit.removeFirst()
        val currentDistance = visitedPoints[currentPoint]!!
        directions.forEach { direction ->
            val next = currentPoint + direction
            if (!blockedPoints.contains(next) && next.isInBounds(endPos)) {
                val possibleDistance = currentDistance + 1
                visitedPoints.compute(next) { _, value ->
                    if (value == null || possibleDistance < value) {
                        pointsToVisit.addLast(next)
                        possibleDistance
                    } else {
                        value
                    }
                }
            }
        }
    }

    val result = visitedPoints[endPos]
    println(result)
}

fun day18part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input18.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val blockedPoints = lineList.map {
        val parts = it.split(",")
        Point(parts[0].toInt(), parts[1].toInt())
    }

    val endPos = Point(70, 70)
    val startPos = Point(0, 0)

    var escapePossible = true
    var time = 1024
    var currentlyBlockedPoints: List<Point> = emptyList()

    while (escapePossible) {
        currentlyBlockedPoints = blockedPoints.take(time)

        val pointsToVisit = ArrayDeque(listOf(startPos))
        val visitedPoints = mutableMapOf(Point(0, 0) to 0)
        while (pointsToVisit.isNotEmpty()) {
            val currentPoint = pointsToVisit.removeFirst()
            val currentDistance = visitedPoints[currentPoint]!!
            directions.forEach { direction ->
                val next = currentPoint + direction
                if (!currentlyBlockedPoints.contains(next) && next.isInBounds(endPos)) {
                    val possibleDistance = currentDistance + 1
                    visitedPoints.compute(next) { _, value ->
                        if (value == null || possibleDistance < value) {
                            pointsToVisit.addLast(next)
                            possibleDistance
                        } else {
                            value
                        }
                    }
                }
            }
        }

        escapePossible = visitedPoints.containsKey(endPos)
        time++
    }

    val result = currentlyBlockedPoints.last()
    println(result)
}

private val directions = listOf(
    Point(-1, 0),
    Point(0, 1),
    Point(1, 0),
    Point(0, -1)
)