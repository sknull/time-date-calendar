package de.visualdigits.kotlin.tdc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalDate

class LocalDateExtensionsTest {

    @Test
    fun testEasterSunday() {
        assertEquals(
            LocalDate.of(2024, 3, 31),
            2024.easterSunday()
        )
    }

    @Test
    fun testDayOfWeek1() {
        assertEquals(
            LocalDate.of(2024, 3, 29),
            LocalDate.of(2024, 3, 25).nextDayOfWeek(DayOfWeek.FRIDAY)
        )
    }

    @Test
    fun testDayOfWeek2() {
        assertEquals(
            LocalDate.of(2024, 4, 1),
            LocalDate.of(2024, 3, 26).nextDayOfWeek(DayOfWeek.MONDAY)
        )
    }

    @Test
    fun testFullMoon() {
        assertEquals(
            LocalDate.of(2024, 3, 25),
            LocalDate.of(2024, 3, 1).nextFullMoon(SearchMode.GE)
        )
    }

    @Test
    fun testFullMoon2() {
        assertEquals(
            LocalDate.of(2014, 4, 14),
            LocalDate.of(2014, 3, 21).nextFullMoon(SearchMode.GT)
        )
    }

    @Test
    fun testNewMoon() {
        assertEquals(
            LocalDate.of(2014, 5, 30),
            LocalDate.of(2014, 6, 1).nextNewMoon(SearchMode.GE)
        )
    }

    @Test
    fun testFullMoonNoLeapYear() {
        var nextFullMoon = LocalDate.of(2014, 6, 1).nextFullMoon(SearchMode.GE)
        val dateExpected1 = LocalDate.of(2014, 6, 12)

        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GT)
        assertEquals(LocalDate.of(2014, 7, 11), nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LT)
        assertEquals(LocalDate.of(2014, 5, 13), nextFullMoon)
    }


    @Test
    fun testFullMoonLeapYear() {
        var nextFullMoon = LocalDate.of(2016, 6, 1).nextFullMoon(SearchMode.GE)
        val dateExpected1 = LocalDate.of(2016, 6, 20)

        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GT)
        assertEquals(LocalDate.of(2016, 7, 19), nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LT)
        assertEquals(LocalDate.of(2016, 5, 21), nextFullMoon)
    }


    @Test
    fun testFullMoonJanuary() {
        var nextFullMoon = LocalDate.of(2016, 1, 1).nextFullMoon(SearchMode.GE)
        val dateExpected1 = LocalDate.of(2016, 1, 23)

        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GT)
        assertEquals(LocalDate.of(2016, 2, 22), nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LT)
        assertEquals(LocalDate.of(2015, 12, 24), nextFullMoon)
    }


    @Test
    fun testFullMoonDecember() {
        var nextFullMoon = LocalDate.of(2016, 12, 31).nextFullMoon(SearchMode.GE)
        val dateExpected1 = LocalDate.of(2017, 1, 12)

        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.GT)
        assertEquals(LocalDate.of(2017, 2, 11), nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LE)
        assertEquals(dateExpected1, nextFullMoon)

        nextFullMoon = dateExpected1.nextFullMoon(SearchMode.LT)
        assertEquals(LocalDate.of(2016, 12, 14), nextFullMoon)
    }
}
