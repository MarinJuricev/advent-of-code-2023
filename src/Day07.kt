import CardCombo.*

fun main() {
//    check(part1(readInput("Day07a_test")) == 6440)
//    part1(readInput("Day07a")).println()

    check(part2(readInput("Day07a_test")) == 5905)
    part2(readInput("Day07a")).println()
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

private fun part2(
    input: List<String>,
): Int {
    val cards = input
        .map { line ->
            val (cardHand, bid) = line.split(" ")

            CardHand(
                cards = cardHand,
                bid = bid.toIntOrNull() ?: error("Got unexpected bid: $bid"),
                type = cardHand.toCardCombo2()
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
    val cardMap: Map<Char, Int> = map { it }.groupingBy { it }.eachCount()

    return when (cardMap.size) {
        1 -> FIVE_OF_A_KIND
        2 -> if (cardMap.values.any { it == 4 }) FOUR_OF_A_KIND else FULL_HOUSE
        3 -> if (cardMap.values.any { it == 3 }) THREE_OF_A_KIND else TWO_PAIR
        4 -> ONE_PAIR
        else -> HIGH_CARD
    }
}

private fun String.toCardCombo2(): CardCombo {
    val numberOfJokers = count { it == 'J' }
    if (numberOfJokers == 0) return toCardCombo()

    val cardMapWithoutJokers: Map<Char, Int> = map { it }.groupingBy { it }.eachCount().minus('J')

    return when (cardMapWithoutJokers.size) {
        1 -> FIVE_OF_A_KIND
        2 -> when {
            cardMapWithoutJokers.values.any { it == 3 } && numberOfJokers == 1 -> FOUR_OF_A_KIND
            cardMapWithoutJokers.values.all { it == 2 } && numberOfJokers == 1 -> FULL_HOUSE
            cardMapWithoutJokers.values.any { it == 2 } && numberOfJokers == 2 -> FOUR_OF_A_KIND
            cardMapWithoutJokers.values.all { it == 1 } && numberOfJokers == 3 -> FOUR_OF_A_KIND
            else -> FULL_HOUSE
        }

        3 -> when {
            cardMapWithoutJokers.values.any { it == 2 } && numberOfJokers == 1 -> THREE_OF_A_KIND
            cardMapWithoutJokers.values.all { it == 1 } && numberOfJokers == 2 -> THREE_OF_A_KIND
            else -> TWO_PAIR
        }

        else -> ONE_PAIR
    }.also {
        it
    }
}

private data class CardHand(
    val cards: String,
    val bid: Int,
    val type: CardCombo,
)


private enum class CardCombo(val priority: Int) {

    FIVE_OF_A_KIND(1),
    FOUR_OF_A_KIND(2),
    FULL_HOUSE(3),
    THREE_OF_A_KIND(4),
    TWO_PAIR(5),
    ONE_PAIR(6),
    HIGH_CARD(7);
}

private enum class Card(
    val priority: Int,
    val value: Char,
) {
    A(priority = 1, value = 'A'),
    K(priority = 2, value = 'K'),
    Q(priority = 3, value = 'Q'),
    J(
        priority = 14,
        value = 'J'
    ), // To lazy to introduce a smarter way for the J or Joker ( depending on the part ), manually edit it depending on the part
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

