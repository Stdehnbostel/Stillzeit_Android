package com.example.stillzeit

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimeStringUnitTests {
    @Test
    fun createTimeStringForDatabaseFunctionReturnsCorrectlyFormatedStrings() {
        val now = getSystemTimeStamp()
        val timeString = createTimeStringForDatabase(now)
        val lastChar = timeString.last() // Generierte String sollen auf Z enden. Keine Zeitzone angegeben
        val eleventhChar = timeString[10] // Datum und Uhrzeit sind durch 'T' getrennt
        val length = timeString.length // 20 Zeichen länge. Der Zeitstempel ist auf Sekunden genau, nicht Nanosekunden oder Millisekunden

        assertEquals(lastChar, 'Z')
        assertEquals(eleventhChar, 'T')
        assertEquals(length, 20)
    }


    @Test
    fun createTimeStringCorrectlyFormatedStrings() {
        val now = getSystemTimeStamp()
        val timeString = createTimeString(now)

        val format = Regex("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")
        assertTrue(format.matches(timeString))
    }
}