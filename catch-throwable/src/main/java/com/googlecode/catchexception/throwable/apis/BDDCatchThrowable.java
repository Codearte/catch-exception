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
package com.googlecode.catchexception.throwable.apis;

import com.googlecode.catchexception.throwable.CatchThrowable;
import com.googlecode.catchexception.throwable.ThrowingCallable;

/**
 * Supports <a href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">BDD</a>-like approach to catch and verify
 * throwables (<i>given/when/then</i>).
 * <p>
 * <code><pre class="prettyprint lang-java">import static com.googlecode.catchexception.throwable.apis
 * .BDDCatchThrowable.*;

 // given an empty list
 List myList = new ArrayList();

 // when we try to get the first element of the list
 when(myList).get(1);

 // then we expect an IndexOutOfBoundsThrowable
 then(caughtThrowable())
 .isInstanceOf(IndexOutOfBoundsThrowable.class)
 .hasMessage("Index: 1, Size: 0")
 .hasNoCause();

 // then we expect an IndexOutOfBoundsThrowable (alternatively)
 thenThrown(IndexOutOfBoundsThrowable.class);
 </pre></code>
 *
 * @author rwoo
 * @author mariuszs
 * @since 1.3.0
 *
 */
public class BDDCatchThrowable {

    /**
     *
     * @param actor
     *            The instance that shall be proxied. Must not be <code>null</code>.
     * @see com.googlecode.catchexception.throwable.CatchThrowable#catchThrowable(ThrowingCallable)
     */
    public static void when(ThrowingCallable actor) {
        CatchThrowable.catchThrowable(actor);
    }

    /**
     * Returns the throwable caught during the last call on the proxied object in the current thread.
     *
     * @return Returns the throwable caught during the last call on the proxied object in the current thread - if the
     * call was made through a proxy that has been created via {@link #when(ThrowingCallable)}. Returns null the proxy
     * has not caught an throwable. Returns null if the caught throwable belongs to a class that is no longer
     * {@link ClassLoader loaded}.
     */
    public static Throwable caughtThrowable() {
        return CatchThrowable.caughtThrowable();
    }

    public static <T extends Throwable> T caughtThrowable(Class<T> caughtThrowableType) {
        return CatchThrowable.caughtThrowable();
    }
    /**
     * Throws an assertion if no throwable is thrown or if an throwable of an unexpected type is thrown.
     * <p>
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">// given a list with nine members
     List myList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

     // when we try to get the 500th member of the fellowship
     when(myList).get(500);

     // then we expect an IndexOutOfBoundsThrowable
     thenThrown(IndexOutOfBoundsThrowable.class);
     </pre></code>
     *
     * @param actualThrowableClazz
     *            the expected type of the caught throwable.
     */
    @SuppressWarnings("rawtypes")
    public static void thenThrown(Class actualThrowableClazz) {
        CatchThrowableUtils.thenThrown(actualThrowableClazz);
    }

}
