package com.googlecode.catchexception.apis.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Creates a {@link Matcher matcher} that matches an exception that has no
 * {@link Throwable#getCause() cause}.
 * 
 * @author rwoo
 * 
 * @param <T>
 *            an exception subclass
 */
public class ExceptionNoCauseMatcher<T extends Exception> extends
        BaseMatcher<T> {

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.Matcher#matches(java.lang.Object)
     */
    public boolean matches(Object obj) {
        if (!(obj instanceof Exception))
            return false;

        Exception exception = (Exception) obj;

        return exception.getCause() == null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
     */
    public void describeTo(Description description) {
        description.appendText("has no cause");
    }

}
