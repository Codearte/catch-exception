package com.googlecode.catchexception.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This {@link AbstractExceptionProcessingInvocationHandler} implements JDK's
 * {@link InvocationHandler}.
 * 
 * @author rwoo
 * @param <E>
 *            The type of the exception that shall be caught and (optionally)
 *            verified
 */
public class ExceptionProcessingJdkInvocationHandler<E extends Exception>
        extends AbstractExceptionProcessingInvocationHandler<E> implements
        InvocationHandler {

    @SuppressWarnings("javadoc")
    public ExceptionProcessingJdkInvocationHandler(Object target,
            Class<E> clazz, boolean assertException) {
        super(target, clazz, assertException);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        beforeInvocation();

        try {

            Object retval = method.invoke(target, args);
            return afterInvocation(retval);

        } catch (InvocationTargetException e) {

            Throwable cause = e.getTargetException();

            if (!(cause instanceof Exception)) {

                // we do not handle throwables that are not exceptions
                throw cause;

            } else {

                return afterInvocationThrowsException((Exception) cause, method);
            }

        }
    }
}