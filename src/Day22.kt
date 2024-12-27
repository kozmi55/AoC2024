import java.io.File
import java.io.InputStream

fun day22part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input22.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val initialSecrets = lineList.map { it.toLong() }
    var secretNumbers = initialSecrets.toList()

    repeat(2000) {
        secretNumbers = secretNumbers.map { number ->
            var temp = number * 64
            temp = number xor temp
            var secret = temp % 16777216

            temp = secret / 32
            temp = secret xor temp
            secret = temp % 16777216

            temp = secret * 2048
            temp = secret xor temp
            secret = temp % 16777216

            secret
        }
    }

    println(secretNumbers.sum())
}

fun day22part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input22.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    val initialSecrets = lineList.map { it.toLong() }

    val listOfSequenceToMaxValueMaps = initialSecrets.map { number ->
        val listOfNumberToSequence = mutableListOf((number % 10).toInt() to emptyList<Int>())
        var last = number

        repeat(2000) { index ->
            var temp = last * 64
            temp = last xor temp
            var secret = temp % 16777216

            temp = secret / 32
            temp = secret xor temp
            secret = temp % 16777216

            temp = secret * 2048
            temp = secret xor temp
            secret = temp % 16777216

            last = secret

            val realIndex = index + 1
            val next = (secret % 10).toInt()
            val sequence = if (realIndex >= 4) {
                val keys = listOfNumberToSequence.map { it.first }.toList()
                listOf(
                    keys[realIndex - 3] - keys[realIndex - 4],
                    keys[realIndex - 2] - keys[realIndex - 3],
                    keys[realIndex - 1] - keys[realIndex - 2],
                    next - keys[realIndex - 1]
                )
            } else {
                emptyList()
            }
            listOfNumberToSequence.add(next to sequence)
        }

        val listOfSequenceToNumbers = listOfNumberToSequence.filter { it.second.isNotEmpty() }.map { it.second to it.first }
        val mapOfSequenceToMaxValue = mutableMapOf<List<Int>, Int>()
        listOfSequenceToNumbers.forEach { (sequence, number) ->
            mapOfSequenceToMaxValue.putIfAbsent(sequence, number)
        }

        mapOfSequenceToMaxValue
    }

    val sequencesCombinedValue = mutableMapOf<List<Int>, Int>()
    listOfSequenceToMaxValueMaps.map {
        it.forEach { (key, value) ->
            sequencesCombinedValue.compute(key) { _, combinedValue ->
                if (combinedValue == null) value else combinedValue + value
            }
        }
    }

    println(sequencesCombinedValue.maxBy { it.value }.value)
}