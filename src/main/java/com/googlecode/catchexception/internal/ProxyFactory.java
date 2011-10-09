package com.googlecode.catchexception.internal;

import org.mockito.cglib.proxy.MethodInterceptor;

/**
 * Creates proxies that catch and verify exceptions.
 * 
 * @author rwoo
 * 
 */
interface ProxyFactory {

    /**
     * Creates proxies that catch and verify exceptions.
     * @param targetClass TODO
     * @param interceptor TODO
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * @param <E>
     *            The type of the given <code>exception</code>.
     * @return Returns the created proxy.
     */
    public <T, E extends Exception> T createProxy(Class<?> targetClass, MethodInterceptor interceptor);

}