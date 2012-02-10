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

import com.googlecode.catchexception.apis.fest.CatchExceptionFestObjectAssert;

/**
 * This 'proof of concept' shows how use catch-exception with a syntax similar
 * to <a href="http://docs.codehaus.org/display/FEST/More+Examples">FEST</a>,
 * see {@link #assertThat2(Object)}.
 * 
 * @author rwoo
 * 
 */
public class CatchExceptionFestAssertions {

    /**
     * Creates a new instance of
     * <code>{@link CatchExceptionFestObjectAssert}</code>.
     * <p>
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">assertThat2(service).throwz(IllegalArgumentException.class).when().prepareBilling(Prize.Zero);
assertThat(caughtException())
        .hasMessage("Prize must be greater than zero but was 0.0")
        .hasNoCause();
                </pre></code>
     * 
     * @param <T>
     * @param actual
     *            the value to be the target of the assertions methods.
     * @return Returns the created assertion object.
     */
    public static <T> CatchExceptionFestObjectAssert<T> assertThat2(T actual) {
        return new CatchExceptionFestObjectAssert<T>(actual);
    }
}
