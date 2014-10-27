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
package com.googlecode.catchexception.throwable.apis;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Find;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.catchexception.throwable.CatchThrowable.catchThrowable;
import static com.googlecode.catchexception.throwable.CatchThrowable.caughtThrowable;
import static com.googlecode.catchexception.throwable.apis.CatchThrowableHamcrestMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Tests {@link CatchThrowableHamcrestMatchers}.
 *
 * @author rwoo
 */
@SuppressWarnings("javadoc")
public class CatchThrowableHamcrestMatchersTest {

    @Before
    public void setup() {
        List<String> fellowshipOfTheRing = new ArrayList<String>();
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
        catchThrowable(fellowshipOfTheRing).get(9);
        // caughtThrowable().printStackTrace();
    }

    private void assertMessage(String foundMessage, String expectedExpectedPart, String expectedGotPart) {

        String[] foundParts = foundMessage.split("(?=got:)");
        assertEquals("split of foundMessage did not work: " + foundMessage, 2, foundParts.length);
        String foundExpectedPart = foundParts[0].trim();
        String foundGotPart = foundParts[1].trim();
        assertEquals(expectedExpectedPart, foundExpectedPart);
        assertEquals(expectedGotPart, foundGotPart);
    }

    @Test
    public void testMatcher_instanceOf() {

        assertThat(caughtThrowable(), instanceOf(IndexOutOfBoundsException.class));

        assertThat(caughtThrowable(), is(IndexOutOfBoundsException.class));

        try {
            assertThat(caughtThrowable(), instanceOf(IllegalArgumentException.class));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), "Expected: an instance of java.lang.IllegalArgumentException",
                    "got: <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    private static org.hamcrest.Matcher<String> containsPattern(String regex) {
        return new Find(regex);
    }

    @Test
    public void learningtestMatcher_hasMessage_findRegex() {

        assertThat(caughtThrowable(), hasMessageThat(containsPattern("Index: \\d+")));

        try {
            assertThat(caughtThrowable(), hasMessageThat(containsPattern("Index : \\d+")));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            // OK
        }
    }

    @Test
    public void testMatcher_hasMessage_equalByString() {

        assertThat(caughtThrowable(), hasMessage("Index: 9, Size: 9"));

        try {
            assertThat(caughtThrowable(), hasMessage("something went wrong"));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), "Expected: has a message that is \"something went wrong\"",
                    "got: <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_hasMessage_equalByStringMatcher() {

        assertThat(caughtThrowable(), hasMessageThat(is("Index: 9, Size: 9")));

        try {
            assertThat(caughtThrowable(), hasMessageThat(is("something went wrong")));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), "Expected: has a message that is \"something went wrong\"",
                    "got: <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_hasMessage_containsByStringMatcher() {

        assertThat(caughtThrowable(), hasMessageThat(is(containsString("Index: 9"))));

        try {
            assertThat(caughtThrowable(), hasMessageThat(is(containsString("Index: 8"))));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), "Expected: has a message that is a string containing \"Index: 8\"",
                    "got: <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }
    }

    @Test
    public void testMatcher_hasNoCause() {

        assertThat(caughtThrowable(), hasNoCause());

        try {
            assertThat(new RuntimeException(caughtThrowable()), hasNoCause());
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), //
                    "Expected: has no cause", "got: <java.lang.RuntimeException: "
                            + "java.lang.IndexOutOfBoundsException:" + " Index: 9, Size: 9>");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMatcher_allOf() {

        assertThat(caughtThrowable(), allOf( //
                instanceOf(IndexOutOfBoundsException.class), //
                hasMessage("Index: 9, Size: 9"),//
                hasNoCause() //
        ));

        try {
            assertThat(caughtThrowable(), allOf( //
                    instanceOf(IndexOutOfBoundsException.class), //
                    hasMessage("something went wrong"),//
                    hasNoCause() //
            ));
            throw new RuntimeException("AssertionError expected");
        } catch (AssertionError e) {
            assertMessage(e.getMessage(), "Expected: " //
                    + "(an instance of java.lang.IndexOutOfBoundsException" //
                    + " and has a message that is \"something went wrong\"" //
                    + " and has no cause)", "got: <java.lang.IndexOutOfBoundsException: Index: 9, Size: 9>");
        }

    }
}
