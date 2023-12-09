import Color.*

fun main() {
    val test1Request = mapOf(RED to 12, GREEN to 13, BLUE to 14)
    check(part1(readInput("Day02a_test"), test1Request) == 8)
    part1(readInput("Day02a"), test1Request).println()

    check(part2(readInput("Day02a_test")) == 2286)
    part2(readInput("Day02a")).println()
}

private fun part1(
    input: List<String>,
    request: Map<Color, Int>,
) = input
    .map(::buildDiceGame)
    .filter { diceGame ->
        diceGame.gameThrows.all { gameThrows ->
            gameThrows.all { map -> map.value <= request[map.key]!! }
        }
    }
    .map { it.id }
    .reduce { a, b -> a + b }

private fun part2(
    input: List<String>,
): Int = input
    .map(::buildDiceGame)
    .map(::toFewestNumberOfCubes)
    .map { it.values.reduce { acc, i -> acc * i } }
    .reduce { acc, i -> acc + i }

private fun toFewestNumberOfCubes(diceGame: DiceGame) = entries.associateWith { color ->
    diceGame.gameThrows.maxOf { it[color] ?: 0 }
}


private fun buildDiceGame(line: String) = DiceGame(
    id = line.extractGameId() ?: error("Welp the input for GameId is malformed, the line: $line"),
    gameThrows = line
        .substringAfter(":")
        .split(";")
        .map {
            buildMap {
                it.split(", ").forEach {
                    val (quantity, color) = it.trim().split(" ")
                    put(color.toColor(), quantity.toInt())
                }
            }
        }
)

private fun String.extractGameId() = gameIndexRegex.find(this)?.groupValues?.get(1)?.toInt()
private val gameIndexRegex = Regex("Game\\s*(\\d+)")

private data class DiceGame(
    val id: Int,
    val gameThrows: List<Map<Color, Int>>
)

private enum class Color {
    RED, GREEN, BLUE
}

private fun String.toColor() = when (this) {
    "red" -> RED
    "green" -> GREEN
    "blue" -> BLUE
    else -> error("Got unexpected Color: $this")
}