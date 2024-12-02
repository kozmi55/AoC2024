import java.io.File
import java.io.InputStream
import kotlin.math.abs

fun day02part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input2.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val reports = lineList.map { line ->
        line.split(" ").map { it.toInt() }
    }

    val safeReports = reports.count { it.isSafe() }

    println(safeReports)
}

fun day02part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input2.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val reports = lineList.map { line ->
        line.split(" ").map { it.toInt() }
    }

    val safeReports = reports.count { it.isSafeDampened() }

    println(safeReports)
}

private fun List<Int>.isSafe(): Boolean {
    val increasing = (get(0) - get(1)) < 0
    for (i in 0..size - 2) {
        val diff = get(i) - get(i + 1)
        if (diff == 0) return false
        if (increasing && diff > 0) return false
        if (!increasing && diff < 0) return false
        if (abs(diff) > 3) return false
    }

    return true
}

private fun List<Int>.isSafeDampened(): Boolean {
    if (isSafe()) return true
    for (i in indices) {
        val dampenedList = filterIndexed { index, _ -> index != i }
        if (dampenedList.isSafe()) return true
    }

    return false
}