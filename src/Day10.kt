import java.io.File
import java.io.InputStream
import java.util.*

fun day10part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input10.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList().map { char ->
            // try-catch to work with test inputs
            try {
                char.digitToInt()
            } catch (e: Exception) {
                -1
            }
        }
    }

    val startPoints = mutableListOf<MapPoint>()

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == 0) {
                startPoints.add(MapPoint(0, Point(i, j)))
            }
        }
    }

    val corner = Point(grid.first().size - 1, grid.size - 1)

    val result = startPoints.sumOf { start ->
        val points = ArrayDeque(listOf(start))
        while (points.first().height != 9) {
            val currentPoint = points.removeFirst()
            directions.forEach {
                val next = currentPoint.point + it
                if (next.isInBounds(corner) && grid[next.x][next.y] == currentPoint.height + 1) {
                    points.addLast(MapPoint(currentPoint.height + 1, next))
                }
            }
        }
        points.distinctBy { it.point }.size
    }

    println(result)
}

fun day10part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input10.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList().map { char ->
            // try-catch to work with test inputs
            try {
                char.digitToInt()
            } catch (e: Exception) {
                -1
            }
        }
    }

    val points = mutableListOf<MapPoint>()

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == 0) {
                points.add(MapPoint(0, Point(i, j)))
            }
        }
    }

    val corner = Point(grid.first().size - 1, grid.size - 1)

    while (points.first().height != 9) {
        val currentPoint = points.removeFirst()
        directions.forEach {
            val next = currentPoint.point + it
            if (next.isInBounds(corner) && grid[next.x][next.y] == currentPoint.height + 1) {
                points.addLast(MapPoint(currentPoint.height + 1, next))
            }
        }
    }

    println(points.size)
}

data class MapPoint(val height: Int, val point: Point)

private val directions = listOf(
    Point(-1, 0),
    Point(0, 1),
    Point(1, 0),
    Point(0, -1)
)