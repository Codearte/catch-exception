package com.googlecode.catchexception.internal;

/**
 * Creates proxies that catch and verify exceptions.
 * 
 * @author rwoo
 * 
 */
interface ProxyFactory {

    /**
     * Creates proxies that catch and verify exceptions.
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * @param <E>
     *            The type of the given <code>exception</code>.
     * @param obj
     *            The instance that shall be proxied. Must not be
     *            <code>null</code>.
     * @param exceptionCclazz
     *            The type of the exception that shall be caught and
     *            (optionally) verified. Must not be <code>null</code>.
     * @param assertException
     *            True if the caught exception shall be verified.
     * @return Returns the created proxy.
     */
    public <T, E extends Exception> T createProxy(T obj, Class<E> exceptionCclazz,
            boolean assertException);

}