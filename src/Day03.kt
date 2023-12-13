fun main() {
    check(part1(readInput("Day03a_test")) == 4361)
}

private fun part1(
    input: List<String>,
): Int = input
    .parse()
    .filter { numberWithCoordinates ->
        (numberWithCoordinates.fromX..numberWithCoordinates.toX).all { xIndex ->
            true
        }
//        when {
//            input.getOrNull(globalIndex.dec())?.getOrNull(index.dec())?.isValidNumber() == true -> append(c)
//            input.getOrNull(globalIndex.dec())?.getOrNull(index)?.isValidNumber() == true -> append(c)
//            input.getOrNull(globalIndex.dec())?.getOrNull(index.inc())?.isValidNumber() == true -> append(c)
//            line.getOrNull(index.dec())?.isValidNumber() == true -> append(c)
//            line.getOrNull(index.inc())?.isValidNumber() == true -> append(c)
//            input.getOrNull(globalIndex.inc())?.getOrNull(index.dec())?.isValidNumber() == true -> append(c)
//            input.getOrNull(globalIndex.inc())?.getOrNull(index)?.isValidNumber() == true -> append(c)
//            input.getOrNull(globalIndex.inc())?.getOrNull(index.inc())?.isValidNumber() == true -> append(c)
//        }

    }.map {
        it
        1
    }
    .reduce { acc, unit -> 1 }

private fun Char.isValidNumber(): Boolean = !symbols.contains(this)

private fun List<String>.parse(): List<NumberWithCoordinates> {
    val numberRegex = "\\d+".toRegex()
    return flatMapIndexed { y, line ->
        numberRegex.findAll(line).map { match ->
            NumberWithCoordinates(
                number = match.value.toInt(),
                y = y,
                fromX = match.range.first,
                toX = match.range.last,
            )
        }
    }
}

data class NumberWithCoordinates(
    val number: Int,
    val y: Int,
    val fromX: Int,
    val toX: Int,
)

val symbols = listOf('*', '#', '+', '$', '/')
