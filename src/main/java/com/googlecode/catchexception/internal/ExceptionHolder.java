package com.googlecode.catchexception.internal;

/**
 * Holds a caught exception {@link ThreadLocal per Thread}.
 * 
 * @author rwoo
 * 
 */
public class ExceptionHolder {

    /**
     * The container for the most recently caught exception.
     */
    private static final ThreadLocal<Exception> caughtException = new ThreadLocal<Exception>();

    /**
     * Saves the given exception in {@link #caughtException}.
     * 
     * @param <E>
     *            the type of the caught exception
     * @param caughtException
     *            the caught exception
     */
    public static <E extends Exception> void set(E caughtException) {
        ExceptionHolder.caughtException.set(caughtException);
    }

    /**
     * @param <E>
     *            the type of the caught exception
     * @return Returns the caught exception. Returns null if there is no
     *         exception caught.
     */
    @SuppressWarnings("unchecked")
    public static <E extends Exception> E get() {
        return (E) caughtException.get();
    }

}
