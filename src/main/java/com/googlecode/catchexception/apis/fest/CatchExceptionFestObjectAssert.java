package com.googlecode.catchexception.apis.fest;

import org.fest.assertions.ObjectAssert;

import com.googlecode.catchexception.CatchException;

/**
 * In comparison to {@link ObjectAssert} this class holds a proxy that is able
 * to catch and verify that a exception is thrown as soon as any method is
 * called on the proxy.
 * 
 * @author rwoo
 * 
 * @param <T>
 *            the type of object that shall be proxied so that the proxy can
 *            catch and verify the thrown exception.
 */
public class CatchExceptionFestObjectAssert<T> extends ObjectAssert {

    /**
     * The proxy that is able to catch and verify that a exception is thrown as
     * soon as any method is called on the proxy.
     */
    private T proxy;

    /**
     * @param actual
     *            the target to verify.
     */
    public CatchExceptionFestObjectAssert(T actual) {
        super(actual);
    }

    /**
     * Verifies that a method throws an exception of the given type, or to be
     * more precise, creates a proxy that is able to do this. See
     * {@link #when()}.
     * 
     * @param expectedExceptionType
     *            the type of the expected exception
     * @return Returns this assertion object with an initialized proxy, see
     *         {@link #when()}.
     */
    @SuppressWarnings("unchecked")
    public CatchExceptionFestObjectAssert<T> throwz(
            Class<? extends Exception> expectedExceptionType) {
        proxy = (T) CatchException.verifyException(actual,
                expectedExceptionType);
        return this;
    }

    /**
     * @return Returns the proxy that catches and verifies that a exception is
     *         thrown as soon as any method is called on the proxy.
     */
    public T when() {
        return proxy;
    }

}
