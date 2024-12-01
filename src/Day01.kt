import java.io.File
import java.io.InputStream
import kotlin.math.abs

fun day01part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input1.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val right = mutableListOf<Int>()
    val left = mutableListOf<Int>()

    lineList.forEach { line ->
        val numbers = line.split("   ").map { it.toInt() }
        right.add(numbers.first())
        left.add(numbers.last())
    }

    right.sort()
    left.sort()
    val pairs = right.zip(left)

    val totalDistance = pairs.sumOf { abs(it.first - it.second) }

    println(totalDistance)
}

fun day01part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input1.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val right = mutableListOf<Int>()
    val left = mutableListOf<Int>()

    lineList.forEach { line ->
        val numbers = line.split("   ").map { it.toInt() }
        right.add(numbers.first())
        left.add(numbers.last())
    }

    val countMap = mutableMapOf<Int, Int>()
    right.forEach {
        countMap.compute(it, { _, value ->
            if (value == null) 1 else value + 1
        })
    }

    var totalSimilarityScore = 0
    left.forEach {
        totalSimilarityScore += (countMap[it] ?: 0) * it
    }

    println(totalSimilarityScore)
}