fun main() {
    check(part1(readInput("Day03a_test")) == 4361)
    part1(readInput("Day03a")).println()
}

private fun part1(
    input: List<String>,
): Int = input
    .parse()
    .filter { numberWithCoordinates ->
        calculateIsNextToValidNeighbour(numberWithCoordinates, input).any { it }
    }.sumOf { it.number }

private fun calculateIsNextToValidNeighbour(
    numberWithCoordinates: NumberWithCoordinates,
    input: List<String>
) = (numberWithCoordinates.fromX.dec()..numberWithCoordinates.toX.inc()).flatMap { xIndex ->
    (numberWithCoordinates.y.dec()..numberWithCoordinates.y.inc() step setStepDependingIfOnStartOrEndPosition(
        xIndex,
        numberWithCoordinates
    )).map { yIndex ->
        input.getOrNull(yIndex)?.getOrNull(xIndex).isValidNeighbour()
    }
}

private fun setStepDependingIfOnStartOrEndPosition(
    xIndex: Int,
    numberWithCoordinates: NumberWithCoordinates,
) = if (xIndex < numberWithCoordinates.fromX || xIndex > numberWithCoordinates.toX) 1 else 2

private fun Char?.isValidNeighbour(): Boolean = if (this == null) false else this != '.'

private fun List<String>.parse(): List<NumberWithCoordinates> = flatMapIndexed { y, line ->
    numberRegex.findAll(line).map { match ->
        NumberWithCoordinates(
            number = match.value.toInt(),
            y = y,
            fromX = match.range.first,
            toX = match.range.last,
        )
    }
}

private data class NumberWithCoordinates(
    val number: Int,
    val y: Int,
    val fromX: Int,
    val toX: Int,
)

private val numberRegex = "\\d+".toRegex()
