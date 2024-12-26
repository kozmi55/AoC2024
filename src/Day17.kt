import java.io.File
import java.io.InputStream
import java.math.BigInteger

var regA = 0L
var regB = 0L
var regC = 0L
var pointer = 0
val outputs = mutableListOf<Int>()

fun day17part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input17.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    regA = 0L
    regB = 0L
    regC = 0L
    var program = emptyList<Int>()
    pointer = 0
    outputs.clear()

    lineList.forEach {
        when {
            it.contains("Register A") -> regA = it.split(": ").last().toLong()
            it.contains("Register B") -> regB = it.split(": ").last().toLong()
            it.contains("Register C") -> regC = it.split(": ").last().toLong()
            it.isNotBlank() -> program = it.split(": ").last().split(",").map { it.toInt() }
        }
    }

    while (pointer < program.size) {
        val shouldJump = operate(program[pointer], program[pointer + 1])
        if (shouldJump) pointer += 2
    }

    println(outputs.joinToString(","))
}

private fun operate(opcode: Int, operand: Int): Boolean {
    when(opcode) {
        0 -> {
            regA /= pow(2, getComboOp(operand).toInt())
        }
        1 -> {
            regB = regB xor operand.toLong()
        }
        2 -> {
            regB = getComboOp(operand) % 8
        }
        3 -> {
            if (regA != 0L) {
                pointer = operand
                return false
            }
        }
        4 -> {
            regB = regB xor regC
        }
        5 -> {
            outputs.add((getComboOp(operand) % 8).toInt())
        }
        6 -> {
            regB = regA / pow(2, getComboOp(operand).toInt())
        }
        7 -> {
            regC = regA / pow(2, getComboOp(operand).toInt())
        }
    }

    return true
}

private fun getComboOp(operand: Int): Long {
    return when (operand) {
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> regA
        5 -> regB
        6 -> regC
        else -> 0
    }
}

private fun pow(n: Long, exp: Int): Long{
    return BigInteger.valueOf(n).pow(exp).toLong()
}

fun day17part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input17.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    regA = 0L
    regB = 0L
    regC = 0L
    var program = emptyList<Int>()
    pointer = 0
    outputs.clear()

    lineList.forEach {
        when {
            it.contains("Register A") -> regA = it.split(": ").last().toLong()
            it.contains("Register B") -> regB = it.split(": ").last().toLong()
            it.contains("Register C") -> regC = it.split(": ").last().toLong()
            it.isNotBlank() -> program = it.split(": ").last().split(",").map { it.toInt() }
        }
    }

    var tempRegA = 35184372088832L
    var result = 0L
    while (result == 0L) {
        tempRegA += 1L
        regA = tempRegA
        regB = 0L
        regC = 0L
        pointer = 0
        outputs.clear()

        while (pointer < program.size) {
            val shouldJump = operate(program[pointer], program[pointer + 1])
            if (shouldJump) pointer += 2
        }

        if (outputs.joinToString(",") == program.joinToString(",")) result = tempRegA

        //if (outputs.size == program.size) result = tempRegA

        if (outputs.first() == 2) println("${tempRegA} - ${outputs.joinToString(",")}")

        //if (tempRegA % 100000L == 0L) {
        //    println("${(tempRegA.toFloat() / (tempRegA*2).toFloat()) * 100} %")
        //}
    }

    println(result)
}