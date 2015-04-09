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
package com.googlecode.catchexception.test.apis;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasNoCause;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers;
import com.googlecode.catchexception.matcher.Find;

/**
 * Tests {@link CatchExceptionHamcrestMatchers}.
 *
 * @author rwoo
 */
@SuppressWarnings("javadoc")
public class CatchExceptionHamcrestMatchersTest {

    @Before
    public void setup() {
        List<String> fellowshipOfTheRing = new ArrayList<>();
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

        // assertThat(fellowshipOfTheRing, hasSize(9));
        catchException(() -> fellowshipOfTheRing.get(9));
        // caughtException().printStackTrace();
    }

    private void assertMessage(String foundMessage,
                               String expectedExpectedPart, String expectedGotPart) {

        String[] foundParts = foundMessage.split("(?=but:)");
        assertEquals("split of foundMessage did not work: " + foundMessage, 2,
                foundParts.length);
        String foundExpectedPart = foundParts[0].trim();
        String foundGotPart = foundParts[1].trim();
        assertEquals(expectedExpectedPart, foundExpectedPart);
        assertEquals(expectedGotPart, foundGotPart);
    }

    @Test
    public void testMatcher_instanceOf() {

        assertThat(caughtException(),
                instanceOf(IndexOutOfBoundsException.class));

        assertThat(caughtException(), isA(Exception.class));

        try {
            assertThat(caughtException(),
                    instanceOf(IllegalArgumentException.class));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(
                    e.getMessage(),
                    "Expected: an instance of java.lang.IllegalArgumentException",
                    "but: <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9> is a java.lang.IndexOutOfBoundsException");
        }
    }

    private static org.hamcrest.Matcher<String> containsPattern(String regex) {
        return new Find(regex);
    }

    @Test
    public void learningtestMatcher_hasMessage_findRegex() {

        assertThat(caughtException(),
                hasMessageThat(containsPattern("Index: \\d+")));

        try {
            assertThat(caughtException(),
                    hasMessageThat(containsPattern("Index : \\d+")));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            // OK
        }
    }

    @Test
    public void testMatcher_hasMessage_equalByString() {

        assertThat(caughtException(), hasMessage("Index: 9, Size: 9"));

        try {
            assertThat(caughtException(), hasMessage("something went wrong"));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(),
                    "Expected: has a message that is \"something went wrong\"",
                    "but: was <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_hasMessage_equalByStringMatcher() {

        assertThat(caughtException(), hasMessageThat(is("Index: 9, Size: 9")));

        try {
            assertThat(caughtException(),
                    hasMessageThat(is("something went wrong")));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(),
                    "Expected: has a message that is \"something went wrong\"",
                    "but: was <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_hasMessage_containsByStringMatcher() {

        assertThat(caughtException(),
                hasMessageThat(is(containsString("Index: 9"))));

        try {
            assertThat(caughtException(),
                    hasMessageThat(is(containsString("Index: 8"))));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(
                    e.getMessage(),
                    "Expected: has a message that is a string containing \"Index: 8\"",
                    "but: was <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_hasNoCause() {

        assertThat(caughtException(), hasNoCause());

        try {
            assertThat(new RuntimeException(caughtException()), hasNoCause());
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(
                    e.getMessage(), //
                    "Expected: has no cause",
                    "but: was <java.lang.RuntimeException: "
                            + "java.lang.IndexOutOfBoundsException:"
                            + " Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_allOf() {

        assertThat(caughtException(), allOf( //
                instanceOf(IndexOutOfBoundsException.class), //
                hasMessage("Index: 9, Size: 9"),//
                hasNoCause() //
        ));

        try {
            assertThat(caughtException(), allOf( //
                    instanceOf(IndexOutOfBoundsException.class), //
                    hasMessage("something went wrong"),//
                    hasNoCause() //
            ));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), "Expected: " //
                            + "(an instance of java.lang.IndexOutOfBoundsException" //
                            + " and has a message that is \"something went wrong\"" //
                            + " and has no cause)",
                    "but: has a message that is \"something went wrong\" was <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }

    }
}
