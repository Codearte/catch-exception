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
package com.googlecode.catchexception.throwable;

import java.lang.ref.WeakReference;

/**
 * Holds a caught throwable {@link ThreadLocal per Thread}.
 * 
 * @author rwoo
 * 
 */
class ThrowableHolder {

    /**
     * The container for the most recently caught throwable.
     * <p>
     * There is no need to use {@link WeakReference weak references} here as all
     * the code is for testing so that we don't have to care about memory leaks.
     */
    private static final ThreadLocal<Throwable> caughtThrowable = new ThreadLocal<>();

    /**
     * Saves the given throwable in {@link #caughtThrowable}.
     * 
     * @param <E>
     *            the type of the caught throwable
     * @param caughtThrowable
     *            the caught throwable
     */
    public static <E extends Throwable> void set(E caughtThrowable) {
        ThrowableHolder.caughtThrowable.set(caughtThrowable);
    }

    /**
     * @param <E>
     *            the type of the caught throwable
     * @return Returns the caught throwable. Returns null if there is no
     *         throwable caught.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> E get() {
        return (E) caughtThrowable.get();
    }

}
