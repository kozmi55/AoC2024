import java.io.File
import java.io.InputStream

fun day15part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input15.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = mutableListOf<MutableList<Char>>()
    val instructions = mutableListOf<Char>()

    lineList.forEach {
        if (it.contains('#')) {
            grid.add(it.toMutableList())
        } else if (it.isNotBlank()) {
            instructions.addAll(it.toList())
        }
    }

    var currentRobotPos = Point(0, 0)
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '@') {
                currentRobotPos = Point(j, i)
            }
        }
    }

    instructions.forEach {
        val direction = directions[it]!!
        var nextPoint = currentRobotPos + direction
        var nextChar = grid[nextPoint.y][nextPoint.x]

        var nextEmptySpace = Point(-1, -1)
        if (nextChar == '.') nextEmptySpace = nextPoint
        while (nextChar != '#' && nextChar != '.') {
            nextPoint += direction
            nextChar = grid[nextPoint.y][nextPoint.x]

            if (nextChar == '.') nextEmptySpace = nextPoint
        }

        // Move when possible
        if (nextEmptySpace != Point(-1, -1)) {
            if (direction.x != 0) {
                iterateRange(nextEmptySpace.x, currentRobotPos.x) { newX ->
                    grid[currentRobotPos.y][newX] = grid[currentRobotPos.y][newX - direction.x]
                }
            } else {
                iterateRange(nextEmptySpace.y, currentRobotPos.y) { newY ->
                    grid[newY][currentRobotPos.x] = grid[newY - direction.y][currentRobotPos.x]
                }
            }

            grid[currentRobotPos.y][currentRobotPos.x] = '.'
            currentRobotPos += direction
        }
    }

    var sum = 0
    grid.forEachIndexed { i, chars ->
        chars.forEachIndexed { j, c ->
            if (c == 'O') sum += i * 100 + j
        }
    }

    println(sum)
}

fun day15part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input15.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val grid = mutableListOf<MutableList<Char>>()
    val instructions = mutableListOf<Char>()

    lineList.forEach {
        if (it.contains('#')) {
            grid.add(it.toList().joinToString(separator = "") { char ->
                when (char) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    '@' -> "@."
                    else -> "$char$char"
                }
            }.toMutableList())
        } else if (it.isNotBlank()) {
            instructions.addAll(it.toList())
        }
    }

    var currentRobotPos = Point(0, 0)
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '@') {
                currentRobotPos = Point(j, i)
            }
        }
    }

    instructions.forEach { inst ->
        val direction = directions[inst]!!
        var nextPoint = currentRobotPos + direction
        var nextChar = grid[nextPoint.y][nextPoint.x]

        // Handle empty space
        if (nextChar == '.') {
            grid[nextPoint.y][nextPoint.x] = '@'
            grid[currentRobotPos.y][currentRobotPos.x] = '.'
            currentRobotPos += direction
            return@forEach
        } else if (nextChar == '#') {
            // Handle wall
            return@forEach
        }

        if (direction.x != 0) {
            // Handle horizontal case, same as before
            var nextEmptySpace = Point(-1, -1)
            while (nextChar != '#' && nextChar != '.') {
                nextPoint += direction
                nextChar = grid[nextPoint.y][nextPoint.x]

                if (nextChar == '.') nextEmptySpace = nextPoint
            }

            // Move when possible
            if (nextEmptySpace != Point(-1, -1)) {
                if (direction.x != 0) {
                    iterateRange(nextEmptySpace.x, currentRobotPos.x) { newX ->
                        grid[currentRobotPos.y][newX] = grid[currentRobotPos.y][newX - direction.x]
                    }
                }

                grid[currentRobotPos.y][currentRobotPos.x] = '.'
                currentRobotPos += direction
            }
        } else {
            // Vertical case, and the next is always a box
            val box = getBox(nextPoint, nextChar)!!

            val boxesToCheck = ArrayDeque(listOf(box))
            val boxesChecked = mutableSetOf<Pair<Point, Point>>()
            var blocked = false
            while (boxesToCheck.isNotEmpty() && !blocked) {
                val boxToCheck = boxesToCheck.removeFirst()
                boxesChecked.add(boxToCheck)
                val neighbor1 = boxToCheck.first + direction
                val neighbor2 = boxToCheck.second + direction
                val box1 = getBox(neighbor1, grid[neighbor1.y][neighbor1.x])
                val box2 = getBox(neighbor2, grid[neighbor2.y][neighbor2.x])
                box1?.let { boxesToCheck.addLast(it) }
                box2?.let { boxesToCheck.addLast(it) }

                if (grid[neighbor1.y][neighbor1.x] == '#' || grid[neighbor2.y][neighbor2.x] == '#') blocked = true
            }

            if (!blocked) {
                boxesChecked.map { it.toList() }.flatten().forEach { point ->
                    grid[point.y][point.x] = '.'
                }
                boxesChecked.forEach { boxChecked ->
                    grid[boxChecked.first.y + direction.y][boxChecked.first.x] = '['
                    grid[boxChecked.second.y + direction.y][boxChecked.second.x] = ']'
                }

                grid[nextPoint.y][nextPoint.x] = '@'
                grid[currentRobotPos.y][currentRobotPos.x] = '.'
                currentRobotPos += direction
            }
        }
    }

    var sum = 0
    grid.forEachIndexed { i, chars ->
        chars.forEachIndexed { j, c ->
            if (c == '[') sum += i * 100 + j
        }
    }

    println(sum)
}

private fun getBox(nextPoint: Point, nextChar: Char): Pair<Point, Point>? {
    return when (nextChar) {
        '[' -> Pair(Point(nextPoint.x, nextPoint.y), Point(nextPoint.x + 1, nextPoint.y))
        ']' -> Pair(Point(nextPoint.x - 1, nextPoint.y), Point(nextPoint.x, nextPoint.y))
        else -> null
    }
}

private val directions = mapOf(
    '<' to Point(-1, 0),
    '^' to Point(0, -1),
    '>' to Point(1, 0),
    'v' to Point(0, 1)
)

fun iterateRange(a: Int, b: Int, action: (Int) -> Unit) {
    if (a <= b) {
        for (i in a..b) {
            action(i)
        }
    } else {
        for (i in a downTo b) {
            action(i)
        }
    }
}