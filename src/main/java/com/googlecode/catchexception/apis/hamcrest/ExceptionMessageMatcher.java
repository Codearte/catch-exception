package com.googlecode.catchexception.apis.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * 
 * Creates a {@link Matcher matcher} that matches an exception with a certain
 * message.
 * 
 * @author rwoo
 * 
 * @param <T>
 *            an exception subclass
 */
public class ExceptionMessageMatcher<T extends Exception> extends
        BaseMatcher<T> {

    /**
     * The string matcher that shall match exception message.
     */
    private Matcher<String> expectedMessageMatcher;

    /**
     * @param expectedMessage
     *            the expected exception message
     */
    public ExceptionMessageMatcher(String expectedMessage) {
        super();
        this.expectedMessageMatcher = CoreMatchers.is(expectedMessage);
    }

    /**
     * @param expectedMessageMatcher
     *            a string matcher that shall match the exception message
     */
    public ExceptionMessageMatcher(Matcher<String> expectedMessageMatcher) {
        super();

        this.expectedMessageMatcher = expectedMessageMatcher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.Matcher#matches(java.lang.Object)
     */
    public boolean matches(Object obj) {
        if (!(obj instanceof Exception))
            return false;

        Exception exception = (Exception) obj;

        String foundMessage = exception.getMessage();

        return expectedMessageMatcher.matches(foundMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
     */
    public void describeTo(Description description) {
        description.appendText("has a message that ").appendDescriptionOf(
                expectedMessageMatcher);
    }

}
