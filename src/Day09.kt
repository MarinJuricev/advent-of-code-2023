fun main() {
    check(part1(readInput("Day09_test")) == 114)
    part1(readInput("Day09")).println()
}

private fun part1(
    input: List<String>
): Int = input
    .map(String::extractInts)
    .sumOf(List<Int>::toHistoryDiagram)

private fun List<Int>.toHistoryDiagram(): Int = generateSequence(this) { lastDiagram ->
    val newDiagram = lastDiagram.windowed(2) { it[1] - it[0] }
    if (newDiagram.all { it == 0 }) null else newDiagram
}.fold(0) { acc, new ->
    acc + new.last()
}
