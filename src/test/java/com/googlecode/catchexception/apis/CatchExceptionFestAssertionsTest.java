/**
 * Copyright (C) 2011 rwoo@gmx.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        catchException(fellowshipOfTheRing).get(500);

        // well, the following code tests FEST ... thus it'a rather a learning
        // test, or an integration test
        assertThat(caughtException())
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("Index: 500, Size: 9") //
                .hasNoCause();

        // test: caughtException() ==null
        try {
            assertThat((Throwable) null).isInstanceOf(
                    IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("expecting actual value not to be null",
                    e.getMessage());
        }

        // test: caughtException() == new RuntimeException()
        try {
            assertThat(new RuntimeException()).isInstanceOf(
                    IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("expected instance of:<java.lang."
                    + "IndexOutOfBoundsException> but was instance "
                    + "of:<java.lang.RuntimeException>", e.getMessage());
        }

        // test: caughtException() has other unexpected message
        try {
            assertThat(caughtException()) //
                    .isInstanceOf(IndexOutOfBoundsException.class) //
                    .hasMessage("Hi!");

        } catch (AssertionError e) {
            assertEquals("expected:<'[Hi!]'> but was:"
                    + "<'[Index: 500, Size: 9]'>", e.getMessage());
        }
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
