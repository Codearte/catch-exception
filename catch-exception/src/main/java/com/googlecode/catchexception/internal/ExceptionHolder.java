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
package com.googlecode.catchexception.internal;

import java.lang.ref.WeakReference;

/**
 * Holds a caught exception {@link ThreadLocal per Thread}.
 * 
 * @author rwoo
 * 
 */
public class ExceptionHolder {

    /**
     * The container for the most recently caught exception.
     * <p>
     * There is no need to use {@link WeakReference weak references} here as all
     * the code is for testing so that we don't have to care about memory leaks.
     */
    private static final ThreadLocal<Exception> caughtException = new ThreadLocal<>();

    /**
     * Saves the given exception in {@link #caughtException}.
     * 
     * @param <E>
     *            the type of the caught exception
     * @param caughtException
     *            the caught exception
     */
    public static <E extends Exception> void set(E caughtException) {
        ExceptionHolder.caughtException.set(caughtException);
    }

    /**
     * @param <E>
     *            the type of the caught exception
     * @return Returns the caught exception. Returns null if there is no
     *         exception caught.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception> E get() {
        return (E) caughtException.get();
    }

}
