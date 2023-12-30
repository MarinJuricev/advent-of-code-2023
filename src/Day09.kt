fun main() {
    check(part1(readInput("Day09_test")) { acc, new -> acc + new.last() } == 114)
    part1(readInput("Day09")) { acc, new -> acc + new.last() }.println()

    check(part2(readInput("Day09_test")) { acc, new -> new.first() - acc } == 2)
    part2(readInput("Day09")) { acc, new -> new.first() - acc }.println()
}

private fun part1(
    input: List<String>,
    action: (Int, List<Int>) -> Int,
): Int = input
    .map(String::extractInts)
    .sumOf { it.toHistoryDiagram(action) }

private fun part2(
    input: List<String>,
    action: (Int, List<Int>) -> Int,
): Int = input
    .map(String::extractInts)
    .sumOf { it.toHistoryDiagramPart2(action) }

private fun List<Int>.toHistoryDiagram(
    action: (Int, List<Int>) -> Int,
): Int {
    val seq = generateSequence(this) { lastDiagram ->
        if (lastDiagram.all { it == 0 }) return@generateSequence null
        lastDiagram.windowed(2) { it[1] - it[0] }
    }

    return seq.fold(0, action)
}

private fun List<Int>.toHistoryDiagramPart2(
    action: (Int, List<Int>) -> Int,
): Int {
    val seq = generateSequence(this) { lastDiagram ->
        if (lastDiagram.all { it == 0 }) return@generateSequence null
        lastDiagram.windowed(2) { it[1] - it[0] }
    }.toList().reversed()

    return seq.fold(0, action)
}
