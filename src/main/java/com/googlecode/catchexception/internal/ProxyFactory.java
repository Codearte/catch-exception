package com.googlecode.catchexception.internal;

import org.mockito.cglib.proxy.MethodInterceptor;

/**
 * Creates proxies.
 * 
 * @author rwoo
 * 
 */
interface ProxyFactory {

    /**
     * Create a proxy.
     * 
     * @param <T>
     *            The type parameter makes some casts redundant.
     * @param targetClass
     *            the class the factory shall create a proxy for
     * @param interceptor
     *            the method interceptor that shall be applied to all method
     *            calls.
     * @return Returns the created proxy.
     */
    public <T> T createProxy(Class<?> targetClass, MethodInterceptor interceptor);

}