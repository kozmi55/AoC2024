import java.io.File
import java.io.InputStream

fun day24part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input24.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val values = mutableMapOf<String, Boolean>()
    val operations = mutableSetOf<Operation>()
    lineList.forEach { line ->
        if (line.contains(":")) {
            val parts = line.split(": ")
            values[parts[0]] = parts[1] == "1"
        } else if (line.isNotBlank()) {
            val parts = line.split(" -> ")
            val firstSplitParts = parts[0].split(" ")
            operations.add(Operation(firstSplitParts[0], firstSplitParts[2], firstSplitParts[1], parts[1]))
        }
    }

    while (operations.isNotEmpty()) {
        val possibleOperations = operations.filter { values.containsKey(it.input1) && values.containsKey(it.input2) }
        possibleOperations.forEach {
            val result = it.operate(values)
            values[it.result] = result
        }

        operations.removeAll(possibleOperations.toSet())
    }

    val zSorted = values.filter { it.key.startsWith("z") }.toSortedMap().reversed().values.map { if (it) '1' else '0' }.joinToString("")
    println(zSorted.toLong(radix = 2))
}

fun day24part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input24.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val values = mutableMapOf<String, Boolean>()
    val operations = mutableSetOf<Operation>()
    lineList.forEach { line ->
        if (line.contains(":")) {
            val parts = line.split(": ")
            values[parts[0]] = parts[1] == "1"
        } else if (line.isNotBlank()) {
            val parts = line.split(" -> ")
            val firstSplitParts = parts[0].split(" ")
            operations.add(Operation(firstSplitParts[0], firstSplitParts[2], firstSplitParts[1], parts[1]))
        }
    }

    val xSorted = values.filter { it.key.startsWith("x") }.toSortedMap().reversed().values.map { if (it) '1' else '0' }.joinToString("")
    val ySorted = values.filter { it.key.startsWith("y") }.toSortedMap().reversed().values.map { if (it) '1' else '0' }.joinToString("")
    val xValue = xSorted.toLong(radix = 2)
    val yValue = ySorted.toLong(radix = 2)
    println(xValue)
    println(yValue)


    val tempOperations = operations.toMutableList()
    while (tempOperations.isNotEmpty()) {
        val possibleOperations = tempOperations.filter { values.containsKey(it.input1) && values.containsKey(it.input2) }
        possibleOperations.forEach {
            val result = it.operate(values)
            values[it.result] = result
        }

        tempOperations.removeAll(possibleOperations.toSet())
    }

    val zSorted = values.filter { it.key.startsWith("z") }.toSortedMap().reversed().values.map { if (it) '1' else '0' }.reversed()
    println(zSorted)

    val correctResult = (xValue + yValue).toString(2).toList().reversed()
    val diffIndexes = mutableSetOf<Int>()
    for (i in correctResult.indices) {
        if (correctResult[i] != zSorted[i]) diffIndexes.add(i)
    }

    println(diffIndexes)
    val wrongOperations = operations.filter {
        if (it.result.first() == 'z') {
            val number = it.result.drop(1).toInt()
            diffIndexes.contains(number)
        } else false
    }.toMutableList()
    println(wrongOperations)

    val wrongOperationsAll = mutableSetOf<Operation>()
    while (wrongOperations.isNotEmpty()) {
        val op = wrongOperations.removeFirst()
        wrongOperationsAll.add(op)
        val newOps = operations.filter { it.result == op.input1 || it.result == op.input2 }
        wrongOperations.addAll(0, newOps)
    }

    wrongOperationsAll.forEach { println(it) }
    println(wrongOperationsAll.size)

    val operations2 = operations.toMutableList()
    val wrongIndexes = wrongOperationsAll.map { operations.indexOf(it) }

    var swapFound = true
    val swappedItems = mutableSetOf<String>()
    val checkedSwaps = mutableSetOf<Set<Set<Int>>>()
    while (!swapFound) {
        val indexes = wrongIndexes.shuffled().take(8)
        val newOperations = operations2.toMutableList()
        newOperations.swapItemsAtIndex(indexes[0], indexes[1])
        newOperations.swapItemsAtIndex(indexes[2], indexes[3])
        newOperations.swapItemsAtIndex(indexes[4], indexes[5])
        newOperations.swapItemsAtIndex(indexes[6], indexes[7])

        val checked = setOf(setOf(indexes[0], indexes[1]), setOf(indexes[2], indexes[3]), setOf(indexes[4], indexes[5]), setOf(indexes[6], indexes[7]))
        if (checkedSwaps.contains(checked)) {
            println("skipped: $checked")
            continue
        }

        checkedSwaps.add(checked)
        println("checking: $checked")

        while (newOperations.isNotEmpty()) {
            val possibleOperations = newOperations.filter { values.containsKey(it.input1) && values.containsKey(it.input2) }
            possibleOperations.forEach {
                val result = it.operate(values)
                values[it.result] = result
            }

            newOperations.removeAll(possibleOperations.toSet())
        }

        val zSorted = values.filter { it.key.startsWith("z") }.toSortedMap().reversed().values.map { if (it) '1' else '0' }.joinToString("")
        val zValue = zSorted.toLong(radix = 2)

        if (xValue + yValue == zValue) {
            println(zValue)
            swapFound = true
            indexes.forEach {
                swappedItems.add(newOperations[it].result)
            }
        }

    }
}

data class Operation(val input1: String, val input2: String, val operation: String, val result: String) {
    fun operate(values: Map<String, Boolean>): Boolean {
        val value1 = values[input1]!!
        val value2 = values[input2]!!
        val result = when (operation) {
            "AND" -> value1 and value2
            "OR" -> value1 or value2
            else -> value1 xor value2
        }

        return result
    }
}

private fun generateUniqueRandomNumbers(size: Int, rangeStart: Int, rangeEnd: Int): List<Int> {
    return (rangeStart until rangeEnd).shuffled().take(size)
}

fun List<Operation>.swapItemsAtIndex(i1: Int, i2: Int): List<Operation> {
    return mapIndexed { index, item ->
        when (index) {
            i1 -> item.copy(result = this[i2].result) // Replace item at index1 with item2
            i2 -> item.copy(result = this[i1].result) // Replace item at index2 with item1
            else -> item // Leave other items unchanged
        }
    }
}