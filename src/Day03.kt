import java.io.File
import java.io.InputStream

fun day03part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input3.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()
    val memory = lineList.joinToString()

    val regex = Regex("mul\\((\\d+),(\\d+)\\)")

    val matched = regex.findAll(memory)
    val sum = matched.sumOf { match ->
        match.groupValues.drop(1).map { it.toLong() }.reduce { result, value -> result * value }
    }

    println(sum)
}

fun day03part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input3.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()
    val memory = lineList.joinToString().extractSubstringsBetween("do()", "don't()").joinToString()

    val regex = Regex("mul\\((\\d+),(\\d+)\\)")

    val matched = regex.findAll(memory)
    val sum = matched.sumOf { match ->
        match.groupValues.drop(1).map { it.toLong() }.reduce { result, value -> result * value }
    }

    println(sum)
}

private fun String.extractSubstringsBetween(startString: String, endString: String): List<String> {
    val result = mutableListOf<String>()
    var startIndex = indexOf(startString)

    while (startIndex != -1) {
        val endIndex = indexOf(endString, startIndex + 1)
        if (endIndex == -1) break

        val substring = substring(startIndex + 1, endIndex)
        result.add(substring)

        startIndex = indexOf(startString, endIndex + endString.length)
    }

    return result
}