import java.io.File
import java.io.InputStream

fun day07part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input7.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val lines = lineList.map { line ->
        val parts = line.split(": ")
        val result = parts.first().toLong()
        val values = parts.last().split(" ").map { it.toLong() }
        Pair(result, values)
    }

    var calibrationResult = 0L
    lines.forEach {
        if (isValid(it)) calibrationResult += it.first
    }

    println(calibrationResult)
}

private fun isValid(line: Pair<Long, List<Long>>): Boolean {
    return operate(line.second.drop(1), line.second.first(), line.first)
}

private fun operate(list: List<Long>, result: Long, target: Long): Boolean {
    if (list.isEmpty()) return result == target

    val newResultAdd = list.first() + result
    val newResultMultiply = list.first() * result
    val newList = list.drop(1)

    return operate(newList, newResultMultiply, target) || operate(newList, newResultAdd, target)
}

fun day07part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input7.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val lines = lineList.map { line ->
        val parts = line.split(": ")
        val result = parts.first().toLong()
        val values = parts.last().split(" ").map { it.toLong() }
        Pair(result, values)
    }

    var calibrationResult = 0L
    lines.forEach {
        if (isValidPart2(it)) {

            calibrationResult += it.first
        }
    }

    println(calibrationResult)
}

private fun isValidPart2(line: Pair<Long, List<Long>>): Boolean {
    return operatePart2(line.second.drop(1), line.second.first(), line.first)
}

private fun operatePart2(list: List<Long>, result: Long, target: Long): Boolean {
    if (list.isEmpty()) return result == target

    val newResultAdd = list.first() + result
    val newResultMultiply = list.first() * result
    val newResultConcat = "$result${list.first()}".toLong()
    val newList = list.drop(1)

    return operatePart2(newList, newResultMultiply, target) || operatePart2(
        newList,
        newResultAdd,
        target
    ) || operatePart2(newList, newResultConcat, target)
}