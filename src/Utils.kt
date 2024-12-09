data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    fun isInBounds(corner: Point) = x >= 0 && x <= corner.x && y >= 0 && y <= corner.y
}