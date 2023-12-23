import CardCombo.*

fun main() {
    check(part1(readInput("Day07a_test")) == 6440)
    part1(readInput("Day07a")).println()
}

private fun part1(
    input: List<String>,
): Int {
    val cards = input
        .map { line ->
            val (cardHand, bid) = line.split(" ")

            CardHand(
                cards = cardHand,
                bid = bid.toIntOrNull() ?: error("Got unexpected bid: $bid"),
                type = cardHand.toCardCombo()
            )
        }.sortedWith(cardHandComparator)
    val cardsSize = cards.size

    return cards
        .foldIndexed(0) { index, acc, next ->
            acc + next.bid * (cardsSize - index)
        }
}


private val cardHandComparator = Comparator<CardHand> { hand1, hand2 ->
    when {
        hand1.type.priority != hand2.type.priority -> hand1.type.priority.compareTo(hand2.type.priority)
        else -> {
            val hand1Cards = Card.fromString(hand1.cards)
            val hand2Cards = Card.fromString(hand2.cards)

            hand1Cards
                .zip(hand2Cards)
                .map { it.first.priority.compareTo(it.second.priority) }
                .firstOrNull { it != 0 } ?: 0
        }
    }
}

private fun String.toCardCombo(): CardCombo {
    val cardMap = map { it }.groupingBy { it }.eachCount()
    return when (cardMap.size) {
        1 -> FiveOfAKind(value = cardMap.keys.first().toString())
        2 -> if (cardMap.values.any { it == 4 }) {
            FourOfaKind(
                value = cardMap.keys.first().toString(),
                leftover = cardMap.keys.drop(1).joinToString("")
            )
        } else FullHouse(
            threes = cardMap.keys.first().toString(),
            pair = cardMap.keys.elementAtOrNull(1)?.toString()
                ?: error("Got error while accessing 1 index in ${cardMap.keys}")
        )

        3 ->
            if (cardMap.values.any { it == 3 }) ThreeOfAKind( // or two pairs
                value = cardMap.keys.first().toString(),
                leftover = cardMap.keys.drop(1).joinToString("")
            ) else {
                TwoPair(
                    firstPair = cardMap.keys.first().toString(),
                    secondPair = cardMap.keys.elementAtOrNull(1)?.toString()
                        ?: error("Got error while accessing 1 index in ${cardMap.keys}"),
                    leftover = cardMap.keys.drop(2).joinToString("")
                )
            }

        4 -> OnePair(
            pair = cardMap.keys.first().toString(),
            leftover = cardMap.keys.drop(1).joinToString("")
        )

        else -> HighCard(value = this)
    }

}

private data class CardHand(
    val cards: String,
    val bid: Int,
    val type: CardCombo,
)


private sealed interface CardCombo {

    val priority: Int

    data class FiveOfAKind(val value: String, override val priority: Int = 1) : CardCombo
    data class FourOfaKind(val value: String, val leftover: String, override val priority: Int = 2) : CardCombo
    data class FullHouse(val threes: String, val pair: String, override val priority: Int = 3) : CardCombo
    data class ThreeOfAKind(val value: String, val leftover: String, override val priority: Int = 4) : CardCombo
    data class TwoPair(
        val firstPair: String,
        val secondPair: String,
        val leftover: String,
        override val priority: Int = 5
    ) : CardCombo

    data class OnePair(val pair: String, val leftover: String, override val priority: Int = 6) : CardCombo
    data class HighCard(val value: String, override val priority: Int = 7) : CardCombo
}

private enum class Card(
    val priority: Int,
    val value: Char,
) {
    A(priority = 1, value = 'A'),
    K(priority = 2, value = 'K'),
    Q(priority = 3, value = 'Q'),
    J(priority = 14, value = 'J'), // To lazy to introduce a smarter way for the J or Joker ( depending on the part ), manually edit it depending on the part
    T(priority = 5, value = 'T'),
    NINE(priority = 6, value = '9'),
    EIGHT(priority = 7, value = '8'),
    SEVEN(priority = 8, value = '7'),
    SIX(priority = 9, value = '6'),
    FIVE(priority = 10, value = '5'),
    FOUR(priority = 11, value = '4'),
    THREE(priority = 12, value = '3'),
    TWO(priority = 13, value = '2');


    companion object {
        fun fromString(value: String): List<Card> = value.map { char ->
            Card.entries.find { it.value == char } ?: error("Couldn't build Card, got $value")
        }
    }
}

