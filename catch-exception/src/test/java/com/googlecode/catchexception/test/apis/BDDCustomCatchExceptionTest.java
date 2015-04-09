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

import static com.googlecode.catchexception.test.apis.MyExceptionCustomAssertions.caughtException;
import static com.googlecode.catchexception.test.apis.MyExceptionCustomAssertions.then;
import static com.googlecode.catchexception.test.apis.MyExceptionCustomAssertions.when;

import org.junit.Test;

import com.googlecode.catchexception.MyException;

/**
 * Tests custom exception assertions.
 *
 * @author mariuszs
 */
@SuppressWarnings("javadoc")
public class BDDCustomCatchExceptionTest {

    @Test
    public void testCustomException() throws Exception {

        when(this::throwMyException);

        then(caughtException()).hasErrorCode(500);
    }

    private void throwMyException() {
        throw new MyException(500);
    }

}
