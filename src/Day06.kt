fun main() {
    check(part1(readInput("Day06a_test")) == 288)
    part1(readInput("Day06a")).println()

    check(part2(readInput("Day06a_test")) == 71503)
    part2(readInput("Day06a")).println()

}

private fun part1(
    input: List<String>
): Int {
    val (times, distances) = input.map { it.extractInts() }

    return times.mapIndexed { index, time ->
        val fullDistance = distances[index]
        (0..time).count { pressedButtonTime ->
            val milliMeterPerSecond = time - pressedButtonTime
            val distanceTraveled = milliMeterPerSecond * pressedButtonTime

            distanceTraveled > fullDistance
        }
    }.reduce { acc, i -> acc * i }
}

private fun part2(
    input: List<String>
): Int {
    val (time, distance) = input.map { it.extractInts().joinToString("").toLong() }

    return (0L..time).count { pressedButtonTime ->
        val milliMeterPerSecond = time - pressedButtonTime
        val distanceTraveled = milliMeterPerSecond * pressedButtonTime

        distanceTraveled > distance
    }
}

