import java.io.File
import java.io.InputStream

fun day25part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input25.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val patterns = lineList.chunked(8).map { it.map { it.toList() }.dropLast(1) }

    val numbers = patterns.map { pattern ->
        val numbers = mutableListOf<Int>()
        for (i in 0..<pattern.first().size) {
            var number = 0
            for (j in 1..pattern.size - 2) {
                if (pattern[j][i] == '#') number++
            }
            numbers.add(number)
        }
        Pair(numbers, pattern[0][0] == '#')
    }

    val locks = numbers.filter { it.second }.map { it.first }
    val keys = numbers.filterNot { it.second }.map { it.first }

    var validCount = 0
    locks.forEach { lock ->
        keys.forEach { key ->
            val valid = lock.mapIndexed { index, i ->
                i + key[index]
            }.all { it <= 5 }
            if (valid) {
                validCount++
            }
        }
    }

    println(validCount)
}