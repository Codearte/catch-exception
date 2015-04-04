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

import static com.googlecode.catchexception.throwable.apis.BDDCatchThrowable.caughtThrowable;
import static com.googlecode.catchexception.throwable.apis.BDDCatchThrowable.then;
import static com.googlecode.catchexception.throwable.apis.BDDCatchThrowable.thenThrown;
import static com.googlecode.catchexception.throwable.apis.BDDCatchThrowable.when;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests {@link com.googlecode.catchexception.throwable.apis.BDDCatchThrowable}.
 *
 * @author rwoo
 */
@SuppressWarnings("javadoc")
public class BDDCatchThrowableTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void testThen() {
        // given an empty list
        List myList = new ArrayList();

        // when we try to get first element of the list
        when(myList).get(1);

        // then we expect an IndexOutOfBoundsException
        then(caughtThrowable())
                .isInstanceOf(IndexOutOfBoundsException.class) //
                .hasMessage("Index: 1, Size: 0") //
                .hasNoCause();

    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testThenCaughtThrowable() {
        // given an empty list
        List myList = new ArrayList();

        // when we try to get first element of the list
        when(myList).get(1);

        // then we expect an IndexOutOfBoundsException
        then(caughtThrowable())
                .isInstanceOf(IndexOutOfBoundsException.class) //
                .hasMessage("Index: 1, Size: 0") //
                .hasNoCause();

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

        // test: caughtThrowable() ==null
        when(myList).get(0);
        try {
            thenThrown(IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("Neither a throwable of type java.lang." + "IndexOutOfBoundsException nor another throwable "
                    + "was thrown", e.getMessage());
        }

        // test: caughtThrowable() is not IllegalArgumentException
        when(myList).get(500);
        try {
            thenThrown(IllegalArgumentException.class);

        } catch (AssertionError e) {
            assertEquals("Throwable of type java.lang.IllegalArgumentException"
                            + " expected but was not thrown. Instead a throwable of"
                            + " type class java.lang.ArrayIndexOutOfBoundsException" + " with message '500' was " +
                            "thrown.",
                    e.getMessage());
        }

    }

}
