package de.visualdigits.kotlin.tdc

import java.time.DayOfWeek
import java.time.LocalDate

private const val DAYS_IN_LEAP_YEAR_FEBRUARY: Int = 29 /* Number of days in a February of a leap year */

private const val DAYS_IN_YEAR: Int = 365 /* Days in a non leap year */

private const val FEBRUARY_SUM: Int = 59 /* Sum of days up to 28.2. of a year */

private const val NEWMOON_OFFSET: Long = 13 /* Distance from new moon to full moon in days */

/**
 * Table for calculating the full moons
 * The index is the 'golden number' minus one => year % 19
 *
 * a : First full moon in a year with the corresponding golden number
 * b : 0 The previous full moon was 29 days ago
 *     1 The previous full moon was 30 days ago
 * c : 0 The next full moon is in 29 days
 *     1 The next full moon is in 30 days
 * a, b, c
 */
private val TOGGLE_TABLE: Array<Triple<Int, Int, Int>> =
    arrayOf(
        //       first,     prev,    next
        Triple(15, 0, 1),
        Triple( 4, 0, 1),
        Triple(23, 1, 1),
        Triple(12, 0, 1),
        Triple( 1, 0, 1),
        Triple(20, 1, 1),
        Triple( 9, 0, 1),
        Triple(28, 1, 0),
        Triple(17, 1, 1),
        Triple( 6, 0, 1),
        Triple(25, 1, 1),
        Triple(14, 0, 1),
        Triple( 3, 0, 1),
        Triple(22, 1, 1),
        Triple(11, 0, 1),
        Triple(30, 1, 0),
        Triple(19, 1, 1),
        Triple( 8, 0, 1),
        Triple(27, 1, 0)
    )

private val GOLDEN_PERIODS = TOGGLE_TABLE.size /* Number of entries in the moon toggle table */

/**
 * Return 1 if the given year is a leap year 0 otherwise.
 */
fun Int.isLeapYear() = if (LocalDate.of(this, 1, 1).isLeapYear) 1 else 0

fun Int.easterSunday(): LocalDate {
    return LocalDate.of(this, 3, 21)
        .nextFullMoon(SearchMode.GT)
        .nextDayOfWeek(DayOfWeek.SUNDAY)
}

/**
 * Calculates the next given day of the week after this date.
 */
fun LocalDate.nextDayOfWeek(weekDay: DayOfWeek): LocalDate {
    var diff = (weekDay.value - dayOfWeek.value).toLong()
    if (diff < 0) {
        diff += 7
    }
    return this.plusDays(diff)
}

/**
 * Calculates the next full moon according to the given search mode.
 */
fun LocalDate.nextFullMoon(mode: SearchMode): LocalDate {
    var y = year
    var hdoy = 0
    val goldenNumber = y % GOLDEN_PERIODS
    var pdoy = TOGGLE_TABLE[goldenNumber].first
    var toggle = TOGGLE_TABLE[goldenNumber].third
    val leapYear = y.isLeapYear()

    var doy = dayOfYear

    if (leapYear > 0 && (doy > FEBRUARY_SUM)) {
        doy--
    }

    when (mode) {
        SearchMode.GT, SearchMode.LE -> {
            while (pdoy <= doy) {
                hdoy = pdoy
                pdoy += (DAYS_IN_LEAP_YEAR_FEBRUARY + toggle)
                toggle = toggle xor 1
            }
            if (mode == SearchMode.LE) {
                pdoy = hdoy
            }
        }

        SearchMode.GE, SearchMode.LT -> {
            while (pdoy < doy) {
                hdoy = pdoy
                pdoy += (DAYS_IN_LEAP_YEAR_FEBRUARY + toggle)
                toggle = toggle xor 1
            }
            if (mode == SearchMode.LT) {
                pdoy = hdoy
            }
        }
    }

    if (leapYear > 0 && (pdoy > FEBRUARY_SUM)) {
        pdoy++
    }

    /*
     * The two special cases are dealt with in the following instruction block.
     *   case 1: An overflow occurs over the current year,
     *           the first full moon of the following year is selected.
     *   case 2: There is an underflow below January 1st of the current year,
     *           the last full moon of the previous year is selected.
     */
    if (pdoy > DAYS_IN_YEAR + leapYear) {
        pdoy = TOGGLE_TABLE[(goldenNumber + 1) % GOLDEN_PERIODS].first
        y++
    } else if (pdoy < 1) {
        val ply = LocalDate.of(y - 1, 1, 1).isLeapYear
        pdoy =
            TOGGLE_TABLE[goldenNumber].first - DAYS_IN_LEAP_YEAR_FEBRUARY - TOGGLE_TABLE[goldenNumber].second + DAYS_IN_YEAR + (if (ply) 1 else 0)
        y--
    }

    return LocalDate.ofYearDay(y, pdoy)
}

/**
 * Calculates the next new moon according to the given search mode.
 */
fun LocalDate.nextNewMoon(mode: SearchMode): LocalDate {
    var fullMoon = nextFullMoon(SearchMode.GT)
    val newMoon = fullMoon.minusDays(NEWMOON_OFFSET)

    return if ((mode == SearchMode.GE || mode == SearchMode.LE) && fullMoon == newMoon) {
        fullMoon
    } else if (mode == SearchMode.GT && fullMoon.isBefore(newMoon)) {
        fullMoon = fullMoon.nextFullMoon(SearchMode.GT)
        fullMoon = fullMoon.nextFullMoon(SearchMode.GT)
        fullMoon.minusDays(NEWMOON_OFFSET)
    } else if ((mode == SearchMode.LT) && (fullMoon.compareTo(newMoon) >= 0)) {
        fullMoon = fullMoon.nextFullMoon(SearchMode.LT)
        fullMoon.minusDays(NEWMOON_OFFSET)
    } else {
        newMoon
    }
}
