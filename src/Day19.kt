import java.io.File
import java.io.InputStream

val cache = mutableMapOf<LongRange, Boolean>()
val pt2Cache = mutableMapOf<LongRange, Long>()

fun day19part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input19.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val patterns = lineList.first().split(", ")
    val designs = lineList.drop(2)

    var possible = 0
    designs.forEach { design ->
        cache.clear()
        val ranges = mutableListOf<LongRange>()
        patterns.forEach { pattern ->
            ranges.addAll(findSubstringIndexes(design, pattern))
        }
        val startingNodes = ranges.filter { it.first == 0L }
        val isValidDesign = startingNodes.any { isValid(it, ranges, (design.length - 1).toLong()) }
        if (isValidDesign) possible++
    }

    println(possible)
}

private fun isValid(node: LongRange, ranges: List<LongRange>, lastIndex: Long): Boolean {
    if (node.last == lastIndex) return true

    if (cache.containsKey(node)) return cache[node]!!

    val children = ranges.filter { it.first == node.last + 1 }
    var isValid: Boolean
    for (i in children.indices) {
        isValid = isValid(children[i], ranges, lastIndex)
        cache[children[i]] = isValid
        if (isValid) return true
    }

    return false
}

fun day19part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input19.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val patterns = lineList.first().split(", ")
    val designs = lineList.drop(2)

    var possible = 0L
    designs.forEach { design ->
        pt2Cache.clear()
        val ranges = mutableListOf<LongRange>()
        patterns.forEach { pattern ->
            ranges.addAll(findSubstringIndexes(design, pattern))
        }
        val startingNodes = ranges.filter { it.first == 0L }
        val validCount = startingNodes.sumOf { countValid(it, ranges, (design.length - 1).toLong()) }
        possible += validCount
    }

    println(possible)
}

private fun countValid(node: LongRange, ranges: List<LongRange>, lastIndex: Long): Long {
    if (node.last == lastIndex) return 1

    if (pt2Cache.containsKey(node)) return pt2Cache[node]!!

    val children = ranges.filter { it.first == node.last + 1 }
    var validChildren = 0L
    for (i in children.indices) {
        validChildren += countValid(children[i], ranges, lastIndex)
        pt2Cache[node] = validChildren
    }

    return validChildren
}

private fun findSubstringIndexes(input: String, substring: String): List<LongRange> {
    if (substring.isEmpty()) return emptyList()

    val ranges = mutableListOf<LongRange>()
    var startIndex = input.indexOf(substring)

    while (startIndex != -1) {
        val endIndex = startIndex + substring.length - 1
        ranges.add(startIndex.toLong()..endIndex)
        startIndex = input.indexOf(substring, startIndex + 1)
    }

    return ranges
}