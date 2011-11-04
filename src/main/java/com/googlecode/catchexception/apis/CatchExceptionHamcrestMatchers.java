package com.googlecode.catchexception.apis;

import org.hamcrest.Matcher;
import org.junit.matchers.JUnitMatchers;

import com.googlecode.catchexception.apis.hamcrest.ExceptionMessageMatcher;
import com.googlecode.catchexception.apis.hamcrest.ExceptionNoCauseMatcher;

/**
 * Provides some {@link Matcher matchers} to match some {@link Exception
 * exception} properties.
 * <p>
 * EXAMPLE:
 * <code><pre class="prettyprint lang-java">assertThat(caughtException(),
  allOf(
    is(IndexOutOfBoundsException.class), 
    hasMessage("Index: 9, Size: 9"),
    hasNoCause()
  ));</pre></code>
 * <p>
 * To combine the standard Hamcrest matchers, your custom matchers, these
 * matchers, and other matcher collections (as {@link JUnitMatchers}) in a
 * single class follow the instructions outlined in <a
 * href="http://code.google.com/p/hamcrest/wiki/Tutorial#Sugar_generation">Sugar
 * generation</a>.
 * 
 * @author rwoo
 */
public class CatchExceptionHamcrestMatchers {

    /**
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">assertThat(caughtException(), hasMessage("Index: 9, Size: 9"));</pre></code>
     * 
     * @param <T>
     *            the exception subclass
     * @param expectedMessage
     *            the expected exception message
     * @return Returns a matcher that matches an exception if it has the given
     *         message.
     */
    public static <T extends Exception> org.hamcrest.Matcher<T> hasMessage(
            String expectedMessage) {
        return new ExceptionMessageMatcher<T>(expectedMessage);
    }

    /**
     * EXAMPLES:
     * <code><pre class="prettyprint lang-java">assertThat(caughtException(), hasMessageThat(is("Index: 9, Size: 9")));
assertThat(caughtException(), hasMessageThat(containsString("Index: 9"))); // using JUnitMatchers
assertThat(caughtException(), hasMessageThat(containsPattern("Index: \\d+"))); // using Mockito's Find</pre></code>
     * 
     * @param <T>
     *            the exception subclass
     * @param stringMatcher
     *            a string matcher
     * @return Returns a matcher that matches an exception if the given string
     *         matcher matches the exception message.
     */
    public static <T extends Exception> org.hamcrest.Matcher<T> hasMessageThat(
            Matcher<String> stringMatcher) {
        return new ExceptionMessageMatcher<T>(stringMatcher);
    }

    /**
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">assertThat(caughtException(), hasNoCause());</pre></code>
     * 
     * @param <T>
     *            the exception subclass
     * @return Returns a matcher that matches the exception if it does not have
     *         a {@link Throwable#getCause() cause}.
     */
    public static <T extends Exception> org.hamcrest.Matcher<T> hasNoCause() {
        return new ExceptionNoCauseMatcher<T>();
    }

}
