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
package com.googlecode.catchexception.throwable.apis;

import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.ThrowableAssert;

import com.googlecode.catchexception.throwable.CatchThrowable;
import com.googlecode.catchexception.throwable.ThrowableNotThrownAssertionError;

/**
 * Supports <a href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">BDD</a>-like approach to catch and verify
 * throwables (<i>given/when/then</i>).
 * <p>
 * EXAMPLE: <code><pre class="prettyprint lang-java">// given an empty list
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
 * <p>
 * The Method {@link #then(Throwable)} uses <a href="https://github.com/alexruiz/fest-assert-2.x">FEST Fluent Assertions
 * 2.x</a>. You can use them directly if you like:
 * <code><pre class="prettyprint lang-java">// import static org.fest.assertions.Assertions.assertThat;

// then we expect an IndexOutOfBoundsThrowable
assertThat(caughtThrowable())
        .isInstanceOf(IndexOutOfBoundsThrowable.class)
        .hasMessage("Index: 1, Size: 0") 
        .hasMessageStartingWith("Index: 1") 
        .hasMessageEndingWith("Size: 0") 
        .hasMessageContaining("Size") 
        .hasNoCause();
</pre></code>
 * 
 * @author rwoo
 * @since 1.2.0
 * 
 */
public class CatchThrowableBdd {

    /**
     * Use it together with {@link #then(Throwable)} or {@link #thenThrown(Class)} in order to catch an throwable and to
     * get access to the thrown throwable (for further verifications).
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * 
     * @param obj
     *            The instance that shall be proxied. Must not be <code>null</code>.
     * @return Returns a proxy for the given object. The proxy catches throwables of the given type when a method on the
     *         proxy is called.
     * @see CatchThrowable#catchThrowable(Object)
     */
    public static <T> T when(T obj) {
        return CatchThrowable.catchThrowable(obj);
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void thenThrown(Class actualThrowableClazz) {
        Throwable e = CatchThrowable.caughtThrowable();
        if (e == null) {
            // no throwable caught -> assertion failed
            throw new ThrowableNotThrownAssertionError(actualThrowableClazz);
        } else if (!actualThrowableClazz.isAssignableFrom(CatchThrowable.caughtThrowable().getClass())) {
            // caught throwable is of wrong type -> assertion failed
            throw new ThrowableNotThrownAssertionError(actualThrowableClazz, e);
        } else {
            // the caught throwable is of the expected type -> nothing to do :-)
        }
    }

    /**
     * Enables <a href="https://github.com/alexruiz/fest-assert-2.x">FEST Fluent Assertions 2.x</a> about the caught
     * throwable.
     * <p>
     * EXAMPLE: <code><pre class="prettyprint lang-java">// given an empty list
List myList = new ArrayList();

// when we try to get first element of the list
when(myList).get(1);

// then we expect an IndexOutOfBoundsThrowable
then(caughtThrowable())
        .isInstanceOf(IndexOutOfBoundsThrowable.class)
        .hasMessage("Index: 1, Size: 0") 
        .hasMessageStartingWith("Index: 1") 
        .hasMessageEndingWith("Size: 0") 
        .hasMessageContaining("Size") 
        .hasNoCause();
</pre></code>
     * 
     * @param actualThrowable
     *            the value to be the target of the assertions methods.
     * @return Returns the created assertion object.
     * @see Assertions#assertThat(Throwable)
     */
    public static ThrowableAssert then(Throwable actualThrowable) {
        // delegate to FEST assertions
        return Assertions.assertThat(actualThrowable);
    }

}
