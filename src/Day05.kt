import java.io.File
import java.io.InputStream

fun day05part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input5.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val rules = lineList
        .filter { it.contains("|") }
        .map { it.split("|") }
        .map { values -> values.first().toInt() to values.last().toInt() }

    val pages = lineList
        .filter { it.contains(",") }
        .map { it.split(",").map { values -> values.toInt() } }

    var centerItemsSum = 0
    pages.forEach { page ->
        if (isValidPage(page, rules)) {
            val middleNumber = page[page.size / 2]
            centerItemsSum += middleNumber
        }
    }

    println(centerItemsSum)
}

fun day05part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input5.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val rules = lineList
        .filter { it.contains("|") }
        .map { it.split("|") }
        .map { values -> values.first().toInt() to values.last().toInt() }

    val invalidPages = lineList
        .filter { it.contains(",") }
        .map { it.split(",").map { values -> values.toInt() } }
        .filter { !isValidPage(it, rules) }

    var centerItemsSum = 0
    invalidPages.forEach { page ->
        val correctPage = sortList(page, rules)
        val middleNumber = correctPage[correctPage.size / 2]
        centerItemsSum += middleNumber
    }

    println(centerItemsSum)
}

private fun sortList(list: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
    var result = list
    while (!isValidPage(result, rules)) {
        rules.forEach {
            if (result.contains(it.first) && result.contains(it.second) && result.indexOf(it.first) > result.indexOf(it.second)) {
                result = result.swapItems(it.first, it.second)
            }
        }
    }
    return result
}


fun <T> List<T>.swapItems(item1: T, item2: T): List<T> {
    val index1 = indexOf(item1)
    val index2 = indexOf(item2)

    if (index1 == -1 || index2 == -1) {
        return this // Return the original list if items are not found
    }

    return mapIndexed { index, item ->
        when (index) {
            index1 -> this[index2] // Replace item at index1 with item2
            index2 -> this[index1] // Replace item at index2 with item1
            else -> item // Leave other items unchanged
        }
    }
}

private fun isValidPage(page: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
    page.forEachIndexed { index, i ->
        val before = page.subList(0, index)
        val after = page.subList(index + 1, page.size)
        rules
            .filter { it.first == i || it.second == i }
            .forEach { if (before.contains(it.second) || after.contains(it.first)) return false }
    }
    return true
}