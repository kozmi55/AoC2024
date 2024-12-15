import kotlin.time.Duration
import kotlin.time.measureTimedValue

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
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