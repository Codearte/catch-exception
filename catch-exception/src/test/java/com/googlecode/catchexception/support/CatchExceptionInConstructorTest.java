/**
 * Copyright (C) ${year} rwoo@gmx.de
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
package com.googlecode.catchexception.support;

import static com.googlecode.catchexception.CatchException.verifyException;

import java.util.function.Supplier;

import org.junit.Test;


/**
 * Demonstrates how to catch exceptions that are thrown by constructors using
 * Google Guava.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
public class CatchExceptionInConstructorTest {

    private static class Thing {

        public Thing(String data) {
            throw new IllegalArgumentException("bad data: " + data);
        }
    }

    @Test
    public void testExceptionThrownInConstructor() throws Exception {

        Supplier<Thing> builder = () -> new Thing("baddata");
        verifyException(builder::get, IllegalArgumentException.class);
    }
}
