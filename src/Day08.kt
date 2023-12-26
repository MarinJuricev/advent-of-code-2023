fun main() {
    check(part1(readInput("Day08a_test")) == 2)
    check(part1(readInput("Day08b_test")) == 6)

    part1(readInput("Day08")).println()
}

private fun part1(
    input: List<String>,
): Int {
    val (instructions, mazes) = input.split { it.isEmpty() }

    return mazes.map { maze ->
        val (value, leftAndRight) = maze.split(" = ")
        val (left, right) = leftAndRight
            .removeSurrounding("(", ")")
            .split(", ")

        PositionWithDirections(
            value = value,
            leftDirection = left,
            rightDirection = right,
        )
    }.getEndInSteps(instructions = instructions.first())
}

private tailrec fun List<PositionWithDirections>.getEndInSteps(
    instructions: String,
    index: Int = 0,
    stepsNeeded: Int = 0,
): Int {
    val currentDestination = getOrNull(index) ?: error("Extracted the wrong destination at index: $index for instructions: $instructions")

    if (currentDestination.value == "ZZZ")
        return stepsNeeded

    val nextInstructionsIndex =
        if (stepsNeeded <= instructions.lastIndex) stepsNeeded
        else {
            var newIndex = stepsNeeded
            while (newIndex >= instructions.length) {
                newIndex -= instructions.length
            }
            newIndex
        }
    val nextDirection = when (val currentInstruction =
        instructions.getOrNull(nextInstructionsIndex)) {
        'R' -> currentDestination.rightDirection
        'L' -> currentDestination.leftDirection
        else -> error("got unexpected instruction $currentInstruction")
    }

    return getEndInSteps(instructions, indexOfFirst { it.value == nextDirection }, stepsNeeded.inc())
}

private data class PositionWithDirections(
    val value: String,
    val leftDirection: String,
    val rightDirection: String,
)