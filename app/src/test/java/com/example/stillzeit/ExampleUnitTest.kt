package com.example.stillzeit

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun createTimeStringFunctionReturnsCorrectlyFormatedStrings() {
        val timeString = createTimeString()
        val lastChar = timeString.last() // Generierte String sollen auf Z enden. Keine Zeitzone angegeben
        val eleventhChar = timeString[10] // Datum und Uhrzeit sind durch 'T' getrennt
        val length = timeString.length // 20 Zeichen länge. Der Zeitstempel ist auf Sekunden genau, nicht Nanosekunden oder Millisekunden

        assertEquals(lastChar, 'Z')
        assertEquals(eleventhChar, 'T')
        assertEquals(length, 20)
    }
}