fun main() {
    check(part1(readInput("Day09_test")) == 114)
//    part1(readInput("Day09")).println()
}

private fun part1(
    input: List<String>
): Int = input
    .map(String::extractInts)
    .sumOf(List<Int>::toHistoryDiagram)

private fun List<Int>.toHistoryDiagram(): Int {
    val historyDiagrams: MutableList<List<Int>> = mutableListOf(this)

    while (true) {
        val temp = mutableListOf<Int>()
        val diagramToLookup = historyDiagrams.last()

        diagramToLookup.forEachIndexed { index, i ->
            if (index < diagramToLookup.lastIndex)
                temp.add(diagramToLookup[index + 1] - i)
        }

        historyDiagrams.add(temp)
        if (temp.all { it == 0 }) break
    }


    return historyDiagrams.fold(0) { acc, new ->
        acc + new.last()
    }
}
