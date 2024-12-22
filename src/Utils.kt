import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.measureTimedValue

data class Point(var x: Int, var y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun times(other: Int) = Point(x * other, y * other)
    fun manhattanDistance(other: Point) = abs(other.x - x) + abs(other.y - y)
    fun isInBounds(corner: Point) = x >= 0 && x <= corner.x && y >= 0 && y <= corner.y
}

data class PointLong(val x: Long, val y: Long) {
    operator fun plus(other: PointLong) = PointLong(x + other.x, y + other.y)
}

fun <T> runMeasured(block: () -> T) {
    val (_: T, duration: Duration) = measureTimedValue {
        block()
    }

    println("${duration.inWholeMilliseconds} ms.")
}