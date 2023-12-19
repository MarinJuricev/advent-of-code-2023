fun main() {
    check(part1(readInput("Day05a_test")) == 35.0)
    part1(readInput("Day05a")).println()
}

private fun part1(
    input: List<String>
): Double {
    val seeds = input
        .first()
        .substringAfter("seeds: ")
        .split(" ")
        .map(String::toDouble)

    val categories = input
        .drop(2)
        .fold(mutableListOf()) { accRange: MutableList<CategoryRange>, element: String ->
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
    } ?: 0.0
}

private fun updateMap(element: String, accRange: MutableList<CategoryRange>) {
    val (sourceCategory, destinationCategory, step) = element.split(" ")
    val sourceCategoryConverted = sourceCategory.toDouble()
    val destinationCategoryConverted = destinationCategory.toDouble()
    val stepConverted = step.toInt()

    val updatedCategoryRange = accRange.last().copy(
        indexMap = accRange.last().indexMap.plus(
            buildMap {
                repeat(stepConverted) {
                    put(destinationCategoryConverted + it, sourceCategoryConverted + it)
                }
            }
        )
    )
    accRange[accRange.lastIndex] = updatedCategoryRange
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
    val indexMap: Map<Double, Double> = emptyMap()
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
