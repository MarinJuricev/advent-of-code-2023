fun main() {
    check(part1(readInput("Day05a_test")) == 35L)
    part1(readInput("Day05a")).println()
}

private fun part1(
    input: List<String>
): Long {
    val seeds = input
        .first()
        .substringAfter("seeds: ")
        .split(" ")
        .map(String::toLong)

    val categories = input
        .drop(2)
        .fold(mutableListOf<CategoryRange>()) { accRange, element ->
            when {
                element.isEmpty() -> accRange
                element.contains("map") -> buildCategoryRange(element, accRange)
                else -> {
                    updateMap(element, accRange)
                    accRange
                }
            }
        }

    return seeds.minOfOrNull { seed ->
        categories.fold(seed) { acc, next ->
            next.indexMap.getOrDefault(acc, acc)
        }
    } ?: 0L
}

private fun updateMap(element: String, accRange: MutableList<CategoryRange>) {
    val (sourceCategory, destinationCategory, step) = element.split(" ")
    val sourceCategoryConverted = sourceCategory.toLong()
    val destinationCategoryConverted = destinationCategory.toLong()
    val stepConverted = step.toInt()

    // Modify the existing map in place
    val indexMap = accRange.last().indexMap
    repeat(stepConverted) {
        indexMap[destinationCategoryConverted + it] = sourceCategoryConverted + it
    }
}

private fun buildCategoryRange(
    element: String,
    accRange: MutableList<CategoryRange>,
): MutableList<CategoryRange> {
    val (fromCategory, toCategory) = element
        .substringBefore(" map:")
        .split("-to-")
    return accRange.also {
        it.add(
            CategoryRange(
                fromType = Category.fromValue(fromCategory),
                toType = Category.fromValue(toCategory),
            )
        )
    }
}

private data class CategoryRange(
    val fromType: Category,
    val toType: Category,
    val indexMap: MutableMap<Long, Long> = mutableMapOf()
)

private enum class Category(val value: String) {
    SEEDS("seed"),
    SOIL("soil"),
    FERTILIZER("fertilizer"),
    WATER("water"),
    LIGHT("light"),
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    LOCATION("location");

    companion object {
        fun fromValue(value: String): Category =
            entries.first { it.value == value }

    }
}
