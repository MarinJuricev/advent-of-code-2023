fun main() {
    check(part1(readInput("Day08a_test")) == 2L)
    check(part1(readInput("Day08b_test")) == 6L)
    part1(readInput("Day08")).println()

    check(part2(readInput("Day08c_test")) == 6L)
    part2(readInput("Day08")).println()

}

private fun part1(
    input: List<String>,
): Long {
    val (instructions, mazes) = input.split { it.isEmpty() }
    val positions = buildPositionsWithDirections(mazes)

    return positions.getEndInSteps(
        instructions = instructions.first(),
        index = positions.indexOfFirst { it.value == "AAA" },
    ) { value -> value == "ZZZ" }
}

private fun buildPositionsWithDirections(mazes: List<String>) = mazes.map { maze ->
    val (value, leftAndRight) = maze.split(" = ")
    val (left, right) = leftAndRight
        .removeSurrounding("(", ")")
        .split(", ")

    PositionWithDirections(
        value = value,
        leftDirection = left,
        rightDirection = right,
    )
}

private tailrec fun List<PositionWithDirections>.getEndInSteps(
    instructions: String,
    index: Int = 0,
    stepsNeeded: Long = 0L,
    endCondition: (String) -> Boolean,
): Long {
    val currentDestination =
        getOrNull(index) ?: error("Extracted the wrong destination at index: $index for instructions: $instructions")

    if (endCondition(currentDestination.value)) return stepsNeeded

    val nextInstructionsIndex = stepsNeeded % instructions.length
    val nextDirection = when (val currentInstruction =
        instructions.getOrNull(nextInstructionsIndex.toInt())) {
        'R' -> currentDestination.rightDirection
        'L' -> currentDestination.leftDirection
        else -> error("got unexpected instruction $currentInstruction")
    }

    return getEndInSteps(
        instructions = instructions,
        endCondition = endCondition,
        index = indexOfFirst { it.value == nextDirection },
        stepsNeeded = stepsNeeded.inc(),
    )
}

private fun part2(
    input: List<String>,
): Long {
    val (instructions, mazes) = input.split { it.isEmpty() }
    val positions = buildPositionsWithDirections(mazes)
    val startingPositions = positions.filter { it.value.endsWith("A") }

    return startingPositions
        .map {positionWithDirection ->
            positions.getEndInSteps(
                instructions = instructions.first(),
                index = positions.indexOfFirst { it.value == positionWithDirection.value },
                endCondition = { it.endsWith("Z") }
            )
        }.reduce { acc, i ->  leastCommonMultiple(acc,i)}
}

//Euclidean algorithm to find the least common divisor of two numbers.
private fun leastCommonMultiple(a: Long, b: Long): Long {
    return a * b / greatestCommonDivisor(a, b)
}

private tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) a else greatestCommonDivisor(b, a % b)
}

private data class PositionWithDirections(
    val value: String,
    val leftDirection: String,
    val rightDirection: String,
)