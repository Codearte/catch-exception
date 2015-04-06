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
import com.googlecode.catchexception.apis.CatchExceptionAssert;
import com.googlecode.catchexception.apis.CaughtException;

public class MyExceptionCustomAssert extends CatchExceptionAssert {

    protected MyExceptionCustomAssert(CaughtException actual) {
        super(actual);
    }

    public MyExceptionCustomAssert hasErrorCode(int errorCode) {
        isNotNull();
        if (getMyException().getErrorCode() != errorCode) {
            failWithMessage("Expected myException's errorCode to be <%s> but was <%s>", errorCode,
                    getMyException().getErrorCode());
        }
        return this;
    }

    private MyException getMyException() {
        return (MyException) actual;
    }
}
