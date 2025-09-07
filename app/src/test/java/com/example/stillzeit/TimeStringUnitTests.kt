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
        val format = Regex("^\\d\\d\\d\\d-(0[1-9]|1[0-2])-([0-3][01]|[0-2]\\d)T([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\dZ\$")
        
        assertTrue(format.matches(timeString))
    }


    @Test
    fun createTimeStringCorrectlyFormatedStrings() {
        val now = getSystemTimeStamp()
        val timeString = createTimeString(now)

        val format = Regex("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")
        assertTrue(format.matches(timeString))
    }
}