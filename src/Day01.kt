fun main() {
    check(part1(readInput("Day01a_test")) == 142)
    part1(readInput("Day01a")).println()

    check(part2(readInput("Day01b_test")) == 281)
    part2(readInput("Day01b")).println()
}

fun part1(input: List<String>): Int = input
    .mapNotNull(::extractFirstAndLastNumberFromLine)
    .reduce { acc, i -> acc + i }

fun part2(input: List<String>) = input
    .map(::convertTextToNumbers)
    .mapNotNull(::extractFirstAndLastNumberFromLine)
    .reduce { acc, i -> acc + i }

private fun extractFirstAndLastNumberFromLine(
    line: String
) = "${line.find { it.isDigit() }}${line.findLast { it.isDigit() }}".toIntOrNull()

private fun convertTextToNumbers(line: String) = buildString {
    line.forEachIndexed { lineIndex, c ->
        when {
            c.isDigit() -> append(c)
            else -> stringToIntConverter.keys
                .filter { numberInString ->
                    numberInString.indices.all { index ->
                        line.getOrNull(lineIndex + index) == numberInString[index]
                    }
                }
                .map { stringToIntConverter[it] }
                .firstOrNull()
                ?.let(::append)
        }
    }
}

val stringToIntConverter = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)
