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
package com.googlecode.catchexception;

@SuppressWarnings("javadoc")
public class PublicSomethingImpl implements Something {

    public PublicSomethingImpl() {
        super();
    }

    @Override
    public void doNothing() {
        //
    }

    @Override
    public void doThrow() {
        throw new UnsupportedOperationException();
    }

    public void doesNotBelongToAnyInterface() {
        //
    }

    @Override
    public void doThrowNoSuchMethodError() {
        throw new NoSuchMethodError("==testCatchException==");
    }

    @Override
    public void doThrowAssertionError() {
        throw new AssertionError(123);
    }

    protected void dooo() {
        throw new MyException();
    }

}