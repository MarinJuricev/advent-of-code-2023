import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.extractInts() = Regex("-?\\d+").findAll(this).map { it.value.toInt() }.toList()

fun String.extractLongs() = Regex("-?\\d+").findAll(this).map { it.value.toLong() }.toList()

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
    fold(listOf(emptyList())) { acc, element ->
        if (predicate(element)) {
            acc + listOf(emptyList())
        } else {
            acc.dropLast(1) + listOf(acc.last() + element)
        }
    }