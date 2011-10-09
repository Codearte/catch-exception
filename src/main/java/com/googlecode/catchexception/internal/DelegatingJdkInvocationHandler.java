package com.googlecode.catchexception.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This interceptor delegates the method call to the target object.
 * 
 * @author rwoo
 */
public class DelegatingJdkInvocationHandler implements InvocationHandler {

    /**
     * See {@link #DelegatingJdkInvocationHandler(Object)}.
     */
    private Object target;

    /**
     * @param target
     *            The object all method calls are delegated to. Must not be
     *            <code>null</code>.
     */
    public DelegatingJdkInvocationHandler(Object target) {
        this.target = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        try {
            // delegate to the target's method
            return method.invoke(target, args);

        } catch (Exception e) {
            // exception translation
            if (e instanceof InvocationTargetException) {
                e = (Exception) ((InvocationTargetException) e)
                        .getTargetException();
            }
            throw e;
        }

    }
}