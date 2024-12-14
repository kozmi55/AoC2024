import java.io.File
import java.io.InputStream

fun day11part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input11.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    var numbers = lineList.first().split(" ").map { it.toLong() }
    repeat(25) {
        val newStones = mutableListOf<Long>()
        numbers = numbers.map {
            when {
                it == 0L -> 1L
                (it.toString().length % 2) == 0 -> {
                    val splitted = it.toString().splitIntoHalves()
                    newStones.add(splitted.second.toLong())
                    splitted.first.toLong()
                }

                else -> it * 2024
            }
        } + newStones
    }

    println(numbers.size)
}

fun day11part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input11.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    var stones = lineList.first().split(" ").map { it.toLong() }.associateWith { 1L }
    val nextStonesCache = mutableMapOf<Long, List<Long>>()

    repeat(75) {
        val newStones = mutableMapOf<Long, Long>()
        stones.forEach { stone ->
            val next = (nextStonesCache[stone.key] ?: emptyList()).ifEmpty {
                when {
                    stone.key == 0L -> listOf(1L)
                    (stone.key.toString().length % 2) == 0 -> {
                        val splitted = stone.key.toString().splitIntoHalves()
                        listOf(splitted.first.toLong(), splitted.second.toLong())
                    }

                    else -> listOf(stone.key * 2024)
                }
            }
            nextStonesCache.putIfAbsent(stone.key, next)
            next.forEach { nextStones ->
                newStones.compute(nextStones) { _, value ->
                    if (value == null) stone.value else value + stone.value
                }
            }
        }

        stones = newStones
    }

    println(stones.toList().sumOf { it.second })
}

private fun String.splitIntoHalves(): Pair<String, String> {
    val middleIndex = length / 2
    val firstHalf = substring(0, middleIndex)
    val secondHalf = substring(middleIndex)
    return Pair(firstHalf, secondHalf)
}