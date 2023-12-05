fun main() {
    check(part1(readInput("Day01_test")) == 142)
    part1(readInput("Day01")).println()

    check(part2(readInput("Day02_test")) == 281)
    part2(readInput("Day02")).println()
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

private fun convertTextToNumbers(
    line: String
) = buildString {
    line.forEachIndexed { lineIndex, c ->
        if (c.isDigit()) append(c)

        stringToIntConverter.keys.forEach { numberInString ->
            var temp = ""
            numberInString.forEachIndexed { index, numberValue ->
                if (line.getOrNull(lineIndex + index) == numberValue) {
                    temp += line.getOrNull(lineIndex + index)
                }
            }
            if(temp == numberInString)
                append(stringToIntConverter[numberInString])
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
