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

import org.assertj.core.api.AbstractThrowableAssert;

import com.googlecode.catchexception.MyException;

public class MyExceptionCustomAssert extends AbstractThrowableAssert<MyExceptionCustomAssert, MyException> {

    protected MyExceptionCustomAssert(MyException actual) {
        super(actual, MyExceptionCustomAssert.class);
    }

    public MyExceptionCustomAssert hasErrorCode(int errorCode) {
        isNotNull();
        if (actual.getErrorCode() != errorCode) {
            failWithMessage("Expected myException's errorCode to be <%s> but was <%s>", errorCode,
                    actual.getErrorCode());
        }
        return this;
    }

}
