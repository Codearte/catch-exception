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

import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.ThrowableAssert;

import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.ExceptionNotThrownAssertionError;

/**
 * Supports <a
 * href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">BDD</a>-like
 * approach to catch and verify exceptions (<i>given/when/then</i>).
 * <p>
 * EXAMPLE:
 * <code><pre class="prettyprint lang-java">// given an empty list
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
 * <p>
 * The Method {@link #then(Exception)} uses <a
 * href="https://github.com/alexruiz/fest-assert-2.x">FEST Fluent Assertions
 * 2.x</a>. You can use them directly if you like:
 * <code><pre class="prettyprint lang-java">// import static org.fest.assertions.Assertions.assertThat;

// then we expect an IndexOutOfBoundsException
assertThat(caughtException())
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index: 1, Size: 0") 
        .hasMessageStartingWith("Index: 1") 
        .hasMessageEndingWith("Size: 0") 
        .hasMessageContaining("Size") 
        .hasNoCause();
</pre></code>
 * 
 * @author rwoo
 * @see com.googlecode.catchexception.apis.BDDCatchException
 * @deprecated As of release 1.3.0, replaced by {@link com.googlecode.catchexception.apis.BDDCatchException()}
 */
@Deprecated
public class CatchExceptionBdd extends BDDCatchException {

    /**
     * Enables <a href="https://github.com/alexruiz/fest-assert-2.x">FEST Fluent
     * Assertions 2.x</a> about the caught exception.
     * <p>
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">// given an empty list
List myList = new ArrayList();

// when we try to get first element of the list
when(myList).get(1);

// then we expect an IndexOutOfBoundsException
then(caughtException())
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index: 1, Size: 0") 
        .hasMessageStartingWith("Index: 1") 
        .hasMessageEndingWith("Size: 0") 
        .hasMessageContaining("Size") 
        .hasNoCause();
</pre></code>
     * 
     * @param actualException
     *            the value to be the target of the assertions methods.
     * @return Returns the created assertion object.
     * @see Assertions#assertThat(Throwable)
     */
    public static ThrowableAssert then(Exception actualException) {
        // delegate to FEST assertions
        return Assertions.assertThat(actualException);
    }

}
