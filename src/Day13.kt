import java.io.File
import java.io.InputStream

fun day13part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input13.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val machines = lineList.chunked(4)
        .map {
            val a = extractCoordinatesForButtons(it[0])
            val b = extractCoordinatesForButtons(it[1])
            val prize = extractCoordinatesForPrize(it[2])
            ClawMachine(prize, a, b)
        }

    val result = machines.sumOf {
        findCheapestWay(it)
    }

    println(result)
}

private fun findCheapestWay(machine: ClawMachine): Long {
    val grid = (0..100).map { i ->
        (0..100).map { j ->
            val tokens = i * 3L + j
            val pointLong = PointLong(i * machine.buttonA.x, i * machine.buttonA.y) + PointLong(j * machine.buttonB.x, j * machine.buttonB.y)
            Progress(pointLong, tokens)
        }
    }

    return grid.flatten().filter { machine.prize == it.currentPos }.minByOrNull { it.tokens }?.tokens ?: 0
}

data class ClawMachine(val prize: PointLong, val buttonA: PointLong, val buttonB: PointLong)

data class Progress(val currentPos: PointLong, val tokens: Long)

private fun extractCoordinatesForButtons(input: String): PointLong {
    val regex = """X\+(\d+),\s*Y\+(\d+)""".toRegex()
    val matchResult = regex.find(input)

    val x = matchResult!!.groupValues[1].toLong() // First capture group
    val y = matchResult!!.groupValues[2].toLong() // Second capture group
    return PointLong(x, y)
}

private fun extractCoordinatesForPrize(input: String): PointLong {
    val regex = """X=(\d+),\s*Y=(\d+)""".toRegex()
    val matchResult = regex.find(input)

    val x = matchResult!!.groupValues[1].toLong() // First capture group
    val y = matchResult!!.groupValues[2].toLong() // Second capture group
    return PointLong(x, y)
}

fun day13part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input13.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val machines = lineList.chunked(4)
        .map {
            val a = extractCoordinatesForButtons(it[0])
            val b = extractCoordinatesForButtons(it[1])
            val prize = extractCoordinatesForPrize(it[2]) + PointLong(10000000000000L, 10000000000000L)
            ClawMachine(prize, a, b)
        }

    val result = machines.map {
        val possibleResults = solveLinearSystem(it.prize, it.buttonA, it.buttonB)
        val result = possibleResults.minByOrNull { it.first * 3 + it.second }
        result?.let { it.first * 3 + it.second } ?: 0
    }.sum()

    println(result)
}

private fun solveLinearSystem(prize: PointLong, buttonA: PointLong, buttonB: PointLong): List<Pair<Long, Long>> {
    val solutions = mutableListOf<Pair<Long, Long>>()

    val c1 = prize.x
    val c2 = prize.y

    val aCoeff1 = buttonA.x
    val bCoeff1 = buttonB.x
    val aCoeff2 = buttonA.y
    val bCoeff2 = buttonB.y

    // Eliminate one variable (use elimination to isolate b):
    // b = ((aCoeff1 * c2) - (aCoeff2 * c1)) / ((bCoeff2 * aCoeff1) - (bCoeff1 * aCoeff2))
    val determinant = bCoeff2 * aCoeff1 - bCoeff1 * aCoeff2

    if (determinant == 0L) {
        return solutions
    }

    // Solve for a and b directly
    val deltaA = (bCoeff2 * c1 - bCoeff1 * c2) // Numerator for a
    val deltaB = (aCoeff1 * c2 - aCoeff2 * c1) // Numerator for b

    if (deltaA % determinant != 0L || deltaB % determinant != 0L) {
        return solutions
    }

    val aParticular = deltaA / determinant
    val bParticular = deltaB / determinant

    // General solutions: Add integer multiples of determinant ratios
    // a = aParticular + k*(bCoeff1/det)
    // b = bParticular + k*(aCoeff1/det)
    val stepA = bCoeff1 / gcd(bCoeff1, aCoeff1)
    val stepB = aCoeff1 / gcd(bCoeff1, aCoeff1)

    for (k in 0..100) { // Generate some general solutions (adjust k as needed)
        val a = aParticular + k * stepA
        val b = bParticular + k * stepB
        solutions.add(a to b)
    }

    return solutions
}

private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)