fun main() {
    check(part1(readInput("Day04a_test")) == 13)
    part1(readInput("Day04a")).println()

    check(part2(readInput("Day04a_test")) == 30)
    part2(readInput("Day04a")).println()
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

private fun part2(
    input: List<String>,
): Int {
    val scratchCardsWithRules = input.map(::scratchCardWithRules)

    return buildMap<Int, Int> {
        scratchCardsWithRules.forEach { this[it.id] = 1 }
        scratchCardsWithRules.forEach { card ->
            card.idOfWonScratchCards.forEach { wonCardId ->
                this[wonCardId] = getOrDefault(wonCardId, 0) + this[card.id]!!
            }
        }
    }.values.sum()
}


private fun scratchCardWithRules(line: String): ScratchCardWithRules {
    val cardIndex = cardIndexRegex.find(line)?.groupValues?.get(1)?.toInt()
        ?: error("Can't parse cardIndex on line: $line")
    var scratchCardIndex = cardIndex.inc()
    val (winningNumbers, drawnNumbers) = line
        .substringAfter(": ")
        .split("|")
        .map { it.trim().split(" ").filter(String::isNotBlank) }

    return ScratchCardWithRules(
        id = cardIndex,
        idOfWonScratchCards = drawnNumbers.mapNotNull { drawnNumber ->
            if (drawnNumber in winningNumbers) {
                scratchCardIndex.also { scratchCardIndex++ }
            } else null
        }
    )
}

data class ScratchCard(
    val id: Int,
    val point: Int,
)

data class ScratchCardWithRules(
    val id: Int,
    val idOfWonScratchCards: List<Int> = emptyList()
)

private val cardIndexRegex = Regex("Card\\s*(\\d+)")
