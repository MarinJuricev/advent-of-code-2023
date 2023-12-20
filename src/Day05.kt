import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        val input = readInput("Day05a")
        val seeds = getSeeds(input)
        val seedRanges = seeds.chunked(2).map { (start, length) -> start..<start + length }

        val elfMaps = input.drop(1).split { it.isEmpty() }.map { it.toElfMap() }.reversed()
        val part1 = elfMaps.findLocation { it in seeds }
        val part2 = elfMaps.findLocation { seedRanges.any { range -> it in range } }
    }.println()
}

private fun part1(
    input: List<String>
): Long {
    val seeds = getSeeds(input)
    val elfMaps = input.drop(1).split { it.isEmpty() }.map { it.toElfMap() }.reversed()

    return elfMaps.findLocation { it in seeds }
}

private fun getSeeds(input: List<String>) = input
    .first() // Tbh could've used .extractLongs
    .substringAfter("seeds: ")
    .split(" ")
    .map(String::toLong)


private fun List<String>.toElfMap(): ElfMap =
    drop(1)
        .joinToString()
        .extractLongs()
        .chunked(3)
        .fold(ElfMap()) { acc, (destinationStart, sourceStart, length) ->
            ElfMap(
                sourceRanges = acc.sourceRanges + listOf(sourceStart..<sourceStart + length),
                destinationRanges = acc.destinationRanges + listOf(destinationStart..<destinationStart + length)
            )
        }

private fun List<ElfMap>.findLocation(seedPredicate: (Long) -> Boolean): Long =
    generateSequence(0L) { it + 1 }.first { location ->
        val seed = fold(location) { acc, elfMap -> elfMap[acc] }
        seedPredicate(seed)
    }

private data class ElfMap(
    val sourceRanges: List<LongRange> = listOf(),
    val destinationRanges: List<LongRange> = listOf(),
) {
    operator fun get(destinationValue: Long): Long = destinationRanges
        .indexOfFirst { range -> destinationValue in range }
        .takeIf { it != -1 }
        ?.let { index ->
            val sourceValue = destinationValue + sourceRanges[index].first - destinationRanges[index].first
            sourceValue.takeIf { it in sourceRanges[index] }
        } ?: destinationValue
}