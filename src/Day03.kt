fun main() {
    check(part1(readInput("Day03a_test")) == 4361)
    part1(readInput("Day03a")).println()

    check(part2(readInput("Day03a_test")) == 467835)
    part2(readInput("Day03a")).println()
}

private fun part1(
    input: List<String>,
): Int = input
    .parse()
    .filter { numberWithCoordinates ->
        calculateIsNextToValidNeighbour(numberWithCoordinates, input).any { it }
    }.sumOf { it.number }


private fun part2(
    input: List<String>,
): Int = input
    .parseGearRatio()
    .filter {
        it.toList().size == 2
    }.sumOf {
        it.reduce { acc, i -> acc * i }
    }

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
    line.parse(y)
}

private fun String.parse(y: Int) = numberRegex.findAll(this).map { match ->
    NumberWithCoordinates(
        number = match.value.toInt(),
        y = y,
        fromX = match.range.first,
        toX = match.range.last,
    )
}

private fun List<String>.parseGearRatio() = flatMapIndexed { y: Int, line: String ->
    gearRegex.findAll(line).map { match ->
        val upperLinesNumbers = getOrNull(y.dec())
            ?.parse(y.dec())
            ?.mapNotNull { numberWithCoordinates ->
                if ((match.range.first.dec()..match.range.first.inc()).overlapsWith(numberWithCoordinates.fromX..numberWithCoordinates.toX))
                    numberWithCoordinates.number
                else null
            }
        val currentLineNumbers = getOrNull(y)
            ?.parse(y)
            ?.mapNotNull { numberWithCoordinates ->
                if ((match.range.first.dec()..match.range.first.inc()).overlapsWith(numberWithCoordinates.fromX..numberWithCoordinates.toX))
                    numberWithCoordinates.number
                else null
            }
        val lowerLinesNumbers = getOrNull(y.inc())
            ?.parse(y.inc())
            ?.mapNotNull { numberWithCoordinates ->
                if ((match.range.first.dec()..match.range.first.inc()).overlapsWith(numberWithCoordinates.fromX..numberWithCoordinates.toX))
                    numberWithCoordinates.number
                else null
            }

        upperLinesNumbers.orEmpty() + currentLineNumbers.orEmpty() + lowerLinesNumbers.orEmpty()
    }
}

private data class NumberWithCoordinates(
    val number: Int,
    val y: Int,
    val fromX: Int,
    val toX: Int,
)

private infix fun IntRange.overlapsWith(other: IntRange): Boolean {
    return this.first <= other.last && this.last >= other.first
}

private val numberRegex = "\\d+".toRegex()
private val gearRegex = "\\*".toRegex()