import java.io.File
import java.io.InputStream

fun day04part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input4.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    var xmasCount = 0

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val current = grid[i][j]

            if (current == 'X') {
                directions.forEach {
                    try {
                        if (grid[i + it.first][j + it.second] == 'M' &&
                            grid[i + it.first * 2][j + it.second * 2] == 'A' &&
                            grid[i + it.first * 3][j + it.second * 3] == 'S'
                        ) {
                            xmasCount++
                        }
                    } catch (_: Exception) {
                        // I don't want to handle boundary checks
                    }
                }
            }
        }
    }

    println(xmasCount)
}

private val directions = listOf(
    Pair(1, 0),
    Pair(1, 1),
    Pair(0, 1),
    Pair(-1, 1),
    Pair(-1, 0),
    Pair(-1, -1),
    Pair(0, -1),
    Pair(1, -1)
)

fun day04part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input4.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toList()
    }

    var xmasCount = 0

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            val current = grid[i][j]

            if (current == 'A') {
                try {
                    if (part2Directions.all {
                            val chars = mutableSetOf<Char>()
                            chars.add(grid[i + it.first().first][j + it.first().second])
                            chars.add(grid[i + it.last().first][j + it.last().second])
                            chars.contains('M') && chars.contains('S')
                        }) {
                        xmasCount++
                    }
                } catch (_: Exception) {
                    // I don't want to handle boundary checks
                }
            }
        }
    }

    println(xmasCount)
}

private val part2Directions = listOf(
    listOf(Pair(1, -1), Pair(-1, 1)),
    listOf(Pair(1, 1), Pair(-1, -1))
)