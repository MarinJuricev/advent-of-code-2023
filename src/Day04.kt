fun main() {
    check(part1(readInput("Day04a_test")) == 13)
    part1(readInput("Day04a")).println()
}

private fun part1(
    input: List<String>,
) = input
    .map { line ->
        var currentPointOffset = 1
        val (winningNumbers, drawnNumbers) = line
            .substringAfter(": ")
            .split("|")
            .map { it.trim().split(" ").filter(String::isNotBlank) }

        ScratchCard(
            id = cardIndexRegex.find(line)?.groupValues?.get(1)?.toInt()
                ?: error("Can't parse cardIndex on line: $line"),
            point = drawnNumbers.maxOfOrNull { drawnNumber ->
                if (drawnNumber in winningNumbers) {
                    currentPointOffset.also { currentPointOffset *= 2 }
                } else 0
            } ?: 0
        )
    }.sumOf {
        it.point
    }


data class ScratchCard(
    val id: Int,
    val point: Int,
)

private val cardIndexRegex = Regex("Card\\s*(\\d+)")
