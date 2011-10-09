package com.googlecode.catchexception.internal;

import java.lang.reflect.Method;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

/**
 * This interceptor delegates the method call to the target object.
 * 
 * @author rwoo
 */
public class DelegatingInterceptor implements MethodInterceptor {

    /**
     * See {@link #DelegatingInterceptor(Object)}.
     */
    private Object target;

    /**
     * @param target
     *            The object all method calls are delegated to. Must not be
     *            <code>null</code>.
     */
    public DelegatingInterceptor(Object target) {
        this.target = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mockito.cglib.proxy.MethodInterceptor#intercept(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[],
     * org.mockito.cglib.proxy.MethodProxy)
     */
    public Object intercept(Object obj, Method method, Object[] args,
            MethodProxy proxy) throws Throwable {

        return proxy.invoke(target, args);
    }
}