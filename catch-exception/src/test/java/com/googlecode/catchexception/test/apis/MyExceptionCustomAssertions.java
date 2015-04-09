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
package com.googlecode.catchexception.test.apis;

import com.googlecode.catchexception.MyException;
import com.googlecode.catchexception.apis.BDDCatchException;

public class MyExceptionCustomAssertions extends BDDCatchException {

    public static MyExceptionCustomAssert then(MyException actual) {
        return new MyExceptionCustomAssert(actual);
    }

    public static MyException caughtException() {
        return caughtException(MyException.class);
    }

}
