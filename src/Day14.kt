import java.io.File
import java.io.InputStream

fun day14part1() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input14.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    var guards = lineList.map { line ->
        val parts = line.split(" ")
        val (x, y) = parts[0].drop(2).split(",").map { it.toInt() }
        val (velX, velY) = parts[1].drop(2).split(",").map { it.toInt() }
        Guard(Point(x, y), Point(velX, velY))
    }

    val maxX = 101
    val maxY = 103

    repeat(100) {
        guards = guards.map {
            val newPos = it.position + it.velocity
            when {
                (newPos.x >= maxX) -> newPos.x -= maxX
                (newPos.x < 0) -> newPos.x += maxX
            }
            when {
                (newPos.y >= maxY) -> newPos.y -= maxY
                (newPos.y < 0) -> newPos.y += maxY
            }
            it.copy(position = newPos)
        }
    }

    val topLeft = guards.filter { it.position.x < maxX / 2 && it.position.y < maxY / 2 }
    val topRight = guards.filter { it.position.x > maxX / 2 && it.position.y < maxY / 2 }
    val bottomLeft = guards.filter { it.position.x < maxX / 2 && it.position.y > maxY / 2 }
    val bottomRight = guards.filter { it.position.x > maxX / 2 && it.position.y > maxY / 2 }

    println(topLeft.size * topRight.size * bottomLeft.size * bottomRight.size)
}

fun day14part2() = runMeasured {
    val inputStream: InputStream = File("${INPUT_PATH}input14.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    var guards = lineList.map { line ->
        val parts = line.split(" ")
        val (x, y) = parts[0].drop(2).split(",").map { it.toInt() }
        val (velX, velY) = parts[1].drop(2).split(",").map { it.toInt() }
        Guard(Point(x, y), Point(velX, velY))
    }

    val maxX = 101
    val maxY = 103

    val treeFile = File("${INPUT_PATH}tree.txt")
    treeFile.writeText("Start")

    repeat(100000) { time ->
        guards = guards.map {
            val newPos = it.position + it.velocity
            when {
                (newPos.x >= maxX) -> newPos.x -= maxX
                (newPos.x < 0) -> newPos.x += maxX
            }
            when {
                (newPos.y >= maxY) -> newPos.y -= maxY
                (newPos.y < 0) -> newPos.y += maxY
            }
            it.copy(position = newPos)
        }

        if ((time + 1) % 103 == 64) {
            println(time + 1)
            treeFile.appendText("\n\n\n")
            treeFile.appendText("${time + 1}")
            treeFile.appendText("\n")
            (0..100).forEach { x ->
                (0..102).forEach { y ->
                    if (guards.any { it.position.x == x && it.position.y == y }) treeFile.appendText("x") else treeFile.appendText(
                        "."
                    )
                }
                treeFile.appendText("\n")
            }
        }
    }
}

data class Guard(val position: Point, val velocity: Point)