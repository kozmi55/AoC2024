import java.io.File
import java.io.InputStream

fun day06part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input6.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toMutableList()
    }

    val center = findCenter(grid)
    var guard = center
    var directionIndex = 0

    try {
        while (guard.first < grid.size && guard.first >= 0 && guard.second < grid.first().size && guard.second >= 0) {
            val direction = directions[directionIndex % 4]
            grid[guard.first][guard.second] = 'X'
            val next = grid[guard.first + direction.first][guard.second + direction.second]
            if (next == '#') {
                directionIndex++
            } else {
                guard = Pair(guard.first + direction.first, guard.second + direction.second)
            }
        }
    } catch (e: Exception) {
        // I don't want to handle boundary checks
    }

    println(grid.flatten().count { it == 'X' })
}

fun day06part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input6.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = lineList.map {
        it.toCharArray().toMutableList()
    }

    var infiniteLoops = 0
    grid.forEachIndexed { x, chars ->
        chars.forEachIndexed { y, c ->
            val newGrid = grid.map { it.toMutableList() }
            newGrid[x][y] = 'O'

            val center = findCenter(grid)
            var guard = center
            var directionIndex = 0
            var steps = 0

            try {
                while ((guard.first < newGrid.size && guard.first >= 0 && guard.second < newGrid.first().size && guard.second >= 0) && steps < 10000) {
                    val direction = directions[directionIndex % 4]
                    newGrid[guard.first][guard.second] = 'X'
                    val next = newGrid[guard.first + direction.first][guard.second + direction.second]
                    if (next == '#' || next == 'O') {
                        directionIndex++
                    } else {
                        guard = Pair(guard.first + direction.first, guard.second + direction.second)
                        steps++
                    }
                }
            } catch (e: Exception) {
                // I don't want to handle boundary checks
            }

            if (steps == 10000) infiniteLoops++
        }
    }

    println(infiniteLoops)
}

private fun findCenter(grid: List<List<Char>>): Pair<Int, Int> {
    grid.forEachIndexed { x, chars ->
        chars.forEachIndexed { y, c ->
            if (c == '^') {
                return Pair(x, y)
            }
        }
    }

    return Pair(-1, -1)
}

private val directions = listOf(
    Pair(-1, 0),
    Pair(0, 1),
    Pair(1, 0),
    Pair(0, -1)
)