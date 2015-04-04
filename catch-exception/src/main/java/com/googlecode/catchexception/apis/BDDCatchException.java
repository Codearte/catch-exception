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
package com.googlecode.catchexception.apis;

import org.assertj.core.api.ThrowableAssert;

import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.internal.ExceptionHolder;

/**
 * Supports <a
 * href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">BDD</a>-like
 * approach to catch and verify exceptions (<i>given/when/then</i>).
 * <p>
 * EXAMPLE:
 * <code><pre class="prettyprint lang-java">import static org.assertj.core.api.BDDAssertions.then;

// given an empty list
List myList = new ArrayList();

// when we try to get the first element of the list
when(myList).get(1);

// then we expect an IndexOutOfBoundsException
then(caughtException())
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index: 1, Size: 0")
        .hasNoCause();

// then we expect an IndexOutOfBoundsException (alternatively)
thenThrown(IndexOutOfBoundsException.class);
</pre></code>
 *
 * @author rwoo
 * @author mariuszs
 * @since 1.3.0
 */
public class BDDCatchException {

    /**
     * @param <T>
     *            The type of the given <code>obj</code>.
     *
     * @param obj
     *            The instance that shall be proxied. Must not be
     *            <code>null</code>.
     * @return Returns a proxy for the given object. The proxy catches
     *         exceptions of the given type when a method on the proxy is
     *         called.
     * @see com.googlecode.catchexception.CatchException#catchException(Object)
     */
    public static void when(ThrowableAssert.ThrowingCallable actor) {
        CatchException.catchException(actor);
    }

    /**
     * Throws an assertion if no exception is thrown or if an exception of an
     * unexpected type is thrown.
     * <p>
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">// given a list with nine members
List myList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

// when we try to get the 500th member of the fellowship
when(myList).get(500);

// then we expect an IndexOutOfBoundsException
thenThrown(IndexOutOfBoundsException.class);
</pre></code>
     *
     * @param actualExceptionClazz
     *            the expected type of the caught exception.
     */
    @SuppressWarnings("rawtypes")
    public static void thenThrown(Class actualExceptionClazz) {
      CatchExceptionUtils.thenThrown(actualExceptionClazz);
    }

    public static ExceptionBox caughtException() {
        return new ExceptionBox(ExceptionHolder.get());
    }

    public static CatchExceptionAssert then(ExceptionBox actual) {
        return new CatchExceptionAssert(actual.getException());
    }

}
