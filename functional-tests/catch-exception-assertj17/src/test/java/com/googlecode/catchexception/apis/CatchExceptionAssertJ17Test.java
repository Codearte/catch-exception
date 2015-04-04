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

import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionAssertJ.then;
import static com.googlecode.catchexception.apis.CatchExceptionAssertJ.when;
import static org.junit.Assert.assertEquals;

/**
 * Tests {@link CatchExceptionAssertJ}.
 *
 * @author rwoo
 */
public class CatchExceptionAssertJ17Test {

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
                .hasMessageStartingWith("Index: 1") //
                .hasMessageEndingWith("Size: 0") //
                .hasMessageContaining("Size") //
                .hasNoCause();

        // test: caughtException() == new RuntimeException()
        try {
            then(new RuntimeException()).isInstanceOf(
                    IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("\nExpecting:" //
                    + "\n <java.lang.RuntimeException>" //
                    + "\nto be an instance of:" //
                    + "\n <java.lang.IndexOutOfBoundsException>" //
                    + "\nbut was instance of:" //
                    + "\n <java.lang.RuntimeException>", e.getMessage());
        }

        // test: caughtException() has other unexpected message
        try {
            then(caughtException()) //
                    .isInstanceOf(IndexOutOfBoundsException.class) //
                    .hasMessage("Hi!");

        } catch (AssertionError e) {
            assertEquals("\nExpecting message:" //
                    + "\n <\"Hi!\">" //
                    + "\nbut was:" //
                    + "\n <\"Index: 1, Size: 0\">", e.getMessage());
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testThenWithBDDAssertions() {
        // given an empty list
        List myList = new ArrayList();

        // when we try to get first element of the list
        when(myList).get(1);

        // then we expect an IndexOutOfBoundsException
        BDDAssertions.then(caughtException()) //
                .isInstanceOf(IndexOutOfBoundsException.class) //
                .hasMessage("Index: 1, Size: 0") //
                .hasMessageStartingWith("Index: 1") //
                .hasMessageEndingWith("Size: 0") //
                .hasMessageContaining("Size") //
                .hasNoCause();

        // test: caughtException() == new RuntimeException()
        try {
            BDDAssertions.then(new RuntimeException()).isInstanceOf(
                    IndexOutOfBoundsException.class);

        } catch (AssertionError e) {
            assertEquals("\nExpecting:" //
                    + "\n <java.lang.RuntimeException>" //
                    + "\nto be an instance of:" //
                    + "\n <java.lang.IndexOutOfBoundsException>" //
                    + "\nbut was instance of:" //
                    + "\n <java.lang.RuntimeException>", e.getMessage());
        }

        // test: caughtException() has other unexpected message
        try {
            BDDAssertions.then(caughtException()) //
                    .isInstanceOf(IndexOutOfBoundsException.class) //
                    .hasMessage("Hi!");

        } catch (AssertionError e) {
            assertEquals("\nExpecting message:" //
                    + "\n <\"Hi!\">" //
                    + "\nbut was:" //
                    + "\n <\"Index: 1, Size: 0\">", e.getMessage());
        }
    }

}