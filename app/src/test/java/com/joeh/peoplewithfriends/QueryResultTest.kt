package com.joeh.peoplewithfriends

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class QueryResultTest {

    @Test
    fun testQueryResultConstructors()
    {
        val person = Person()
        val r1 = PersonViewModel.QueryResult.fromData( person )
        val r2 = PersonViewModel.QueryResult.fromErrorMessage( "foo" )
        val r3 = PersonViewModel.QueryResult.fromThrowable( Exception( "bar" ) )

        assertNull( "fromData construction should not have error message", r1.errorMessage )
        assertNull( "fromErrorMessage construction should not have data", r2.personData )
        assertNull( "fromThrowable construction should not have data", r3.personData )
    }
}
