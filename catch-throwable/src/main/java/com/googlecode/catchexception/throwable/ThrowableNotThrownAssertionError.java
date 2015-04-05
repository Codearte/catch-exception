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

import java.io.Serializable;

/**
 * Thrown if a method has not thrown an throwable of the expected type.
 * 
 * @author rwoo
 * @since 16.09.2011
 */
public class ThrowableNotThrownAssertionError extends AssertionError {

    /**
     * See {@link Serializable}.
     */
    private static final long serialVersionUID = 7044423604241785057L;

    /**
     * Use this constructor if neither an throwable of the expected type nor another throwable is thrown.
     * 
     * @param <E>
     *            the type of the throwable that is not thrown.
     * @param clazz
     *            the type of the throwable that is not thrown.
     */
    public <E extends Throwable> ThrowableNotThrownAssertionError(Class<E> clazz) {
        super(clazz == Throwable.class ? "Throwable expected but not thrown" : "Neither a throwable of type "
                + clazz.getName() + " nor another throwable was thrown");
    }

    /**
     * Use this constructor if an throwable of another than the expected type is thrown.
     * 
     * @param <E>
     *            the type of the throwable that is not thrown.
     * @param clazz
     *            the type of the throwable that is not thrown.
     * @param e
     *            the throwable that has been thrown instead of the expected one.
     */
    public <E extends Throwable> ThrowableNotThrownAssertionError(Class<E> clazz, Throwable e) {
        super("Throwable of type " + clazz.getName() + " expected but was not thrown. "
                + "Instead a throwable of type " + e.getClass() + " with message '" + e.getMessage() + "' was thrown.");
    }
}
