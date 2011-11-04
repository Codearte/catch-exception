package com.googlecode.catchexception.apis;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionFestAssertions.assertThat2;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.catchexception.ExceptionNotThrownAssertionError;
import com.googlecode.catchexception.PublicSomethingImpl;

/**
 * Tests {@link CatchExceptionFestAssertions}.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
public class CatchExceptionFestAssertionsTest {

    private List<String> fellowshipOfTheRing = new ArrayList<String>();

    @Before
    public void setup() {
        // let's do some team building :)
        fellowshipOfTheRing.add("frodo");
        fellowshipOfTheRing.add("sam");
        fellowshipOfTheRing.add("merry");
        fellowshipOfTheRing.add("pippin");
        fellowshipOfTheRing.add("gandalf");
        fellowshipOfTheRing.add("legolas");
        fellowshipOfTheRing.add("gimli");
        fellowshipOfTheRing.add("aragorn");
        fellowshipOfTheRing.add("boromir");
    }

    @Test
    public void testAssertThat_standardFest() {

        // assertThat(fellowshipOfTheRing, hasSize(9));
        catchException(fellowshipOfTheRing).get(9); // argggl !

        assertThat(caughtException())
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("Index: 9, Size: 9") //
                .hasNoCause();
    }

    @Test
    public void testAssertThat_customFest() {

        PublicSomethingImpl service = new PublicSomethingImpl();

        assertThat2(service).throwz(UnsupportedOperationException.class).when()
                .doThrow();
        assertTrue(caughtException() instanceof UnsupportedOperationException);

        try {
            assertThat2(service).throwz(IllegalArgumentException.class).when()
                    .doThrow();
            fail("ExceptionNotThrownAssertionError is expected");
        } catch (ExceptionNotThrownAssertionError e) {
            assertNull(caughtException());
            assertEquals(
                    "Exception of type "
                            + IllegalArgumentException.class.getName()
                            + " expected but was not thrown."
                            + " Instead an exception of type "
                            + UnsupportedOperationException.class
                            + " with message 'null' was thrown.",
                    e.getMessage());
        }
    }
}
