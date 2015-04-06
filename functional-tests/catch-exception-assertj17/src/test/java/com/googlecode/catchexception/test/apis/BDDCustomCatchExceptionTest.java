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

import static com.googlecode.catchexception.test.apis.MyExceptionCustomAssertions.then;
import static com.googlecode.catchexception.test.apis.MyExceptionCustomAssertions.when;
import static com.googlecode.catchexception.test.apis.MyExceptionCustomAssertions.caughtException;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

import com.googlecode.catchexception.PublicSomethingImpl;

/**
 * Tests custom exception assertions.
 *
 * @author rwoo
 */
@SuppressWarnings("javadoc")
public class BDDCustomCatchExceptionTest {

    @Test
    public void testCustomException() throws Exception {

        when(new PublicSomethingImpl()).throwMyException();

        then(caughtException()).hasErrorCode(500);

        // and verify that BDDAssertions works too
        then(new RuntimeException("dd")).hasMessage("dd");
        then(new Integer("100")).isEqualTo(100);
    }
}
