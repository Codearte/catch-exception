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
package com.googlecode.catchexception.throwable.test.apis;

import static com.googlecode.catchexception.throwable.test.apis.MyThrowableCustomAssertions.caughtThrowable;
import static com.googlecode.catchexception.throwable.test.apis.MyThrowableCustomAssertions.then;
import static com.googlecode.catchexception.throwable.test.apis.MyThrowableCustomAssertions.when;

import org.junit.Test;

import com.googlecode.catchexception.throwable.MyThrowable;

/**
 * Tests custom throwable assertions.
 *
 * @author rwoo
 */
@SuppressWarnings("javadoc")
public class BDDCustomCatchThrowableTest {

    @Test
    public void testCustomException() throws Exception {

        when(this::throwMyThrowable);

        then(caughtThrowable()).hasErrorCode(500);
    }

    private void throwMyThrowable() {
        throw new MyThrowable(500);
    }
}
