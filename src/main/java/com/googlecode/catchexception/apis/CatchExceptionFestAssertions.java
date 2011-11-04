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
