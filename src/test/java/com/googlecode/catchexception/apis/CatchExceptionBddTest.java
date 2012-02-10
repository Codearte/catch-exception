package com.googlecode.catchexception.apis;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.then;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.thenThrown;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests {@link CatchExceptionBdd}.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
public class CatchExceptionBddTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void testThen() {
        // given an empty list
        List myList = new ArrayList();

        // when we try to get first element of the list
        when(myList).get(1);

        // then we expect an IndexOutOfBoundsException
        then(caughtException()) //
                .isInstanceOf(IndexOutOfBoundsException.class) //
                .hasMessage("Index: 1, Size: 0") //
                .hasNoCause();

        // test: caughtException() ==null
        try {
            assertThat((Throwable) null).isInstanceOf(
                    IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("expecting actual value not to be null",
                    e.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testThenThrown() {

        // given a list with nine elements
        List myList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // when we try to get the 500th element
        when(myList).get(500);

        // then we expect an IndexOutOfBoundsException
        thenThrown(IndexOutOfBoundsException.class);

        // test: caughtException() ==null
        when(myList).get(0);
        try {
            thenThrown(IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("Neither an exception of type java.lang."
                    + "IndexOutOfBoundsException nor another exception "
                    + "was thrown", e.getMessage());
        }

        // test: caughtException() is not IllegalArgumentException
        when(myList).get(500);
        try {
            thenThrown(IllegalArgumentException.class);

        } catch (AssertionError e) {
            assertEquals("Exception of type java.lang.IllegalArgumentException"
                    + " expected but was not thrown. Instead an exception of"
                    + " type class java.lang.ArrayIndexOutOfBoundsException"
                    + " with message '500' was thrown.", e.getMessage());
        }

    }

}
