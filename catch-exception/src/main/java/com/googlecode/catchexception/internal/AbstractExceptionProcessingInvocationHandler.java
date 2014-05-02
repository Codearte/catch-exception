/**
 * Copyright (C) 2011 rwoo@gmx.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.catchexception.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.googlecode.catchexception.ExceptionNotThrownAssertionError;

/**
 * This abstract method invocation interceptor
 * <ul>
 * <li>delegates all method calls to the 'underlying object',
 * <li>catches exceptions of a given type, and
 * <li>(optionally) asserts that an exception of a specific type is thrown.
 * </ul>
 * 
 * @author rwoo
 * @param <E>
 *            The type of the exception that is caught and ,optionally,
 *            verified.
 * @since 16.09.2011
 */
class AbstractExceptionProcessingInvocationHandler<E extends Exception> {

    /**
     * See
     * {@link #AbstractExceptionProcessingInvocationHandler(Object, Class, boolean)}
     * .
     */
    protected Class<E> clazz;

    /**
     * See
     * {@link #AbstractExceptionProcessingInvocationHandler(Object, Class, boolean)}
     * .
     */
    protected Object target;

    /**
     * See
     * {@link #AbstractExceptionProcessingInvocationHandler(Object, Class, boolean)}
     * .
     */
    protected boolean assertException;

    /**
     * @param target
     *            The object all method calls are delegated to. Must not be
     *            <code>null</code>.
     * @param clazz
     *            the type of the exception that is to catch. Must not be
     *            <code>null</code>.
     * @param assertException
     *            True if the interceptor shall throw an
     *            {@link ExceptionNotThrownAssertionError} if the method has not
     *            thrown an exception of the expected type.
     */
    public AbstractExceptionProcessingInvocationHandler(Object target,
            Class<E> clazz, boolean assertException) {

        if (clazz == null) {
            throw new IllegalArgumentException(
                    "exceptionClazz must not be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }

        this.clazz = clazz;
        this.target = target;
        this.assertException = assertException;
    }

    /**
     * Must be called by the subclass before the intercepted method invocation.
     */
    protected void beforeInvocation() {

        // clear exception cache
        ExceptionHolder.set(null);

    }

    /**
     * Must be called by the subclass after the method invocation that has not
     * thrown a exception.
     * 
     * @param retval
     *            The value returned by the intercepted method invocation.
     * @return The value that shall be returned by the proxy.
     * @throws Error
     *             (optionally) Thrown if the verification failed.
     */
    protected Object afterInvocation(Object retval) throws Error {

        if (assertException) {

            throw new ExceptionNotThrownAssertionError(clazz);

        } else {

            // return anything. the value does not matter
            return retval;

        }

    }

    /**
     * Must be called by the subclass after the intercepted method invocation
     * that has thrown a exception.
     * 
     * @param e
     *            the exception thrown by the intercepted method invocation.
     * @param method
     *            the method that was proxied
     * @return Always returns null.
     * @throws Error
     *             Thrown if the verification failed.
     * @throws Exception
     *             The given exception. (Re-)thrown if the thrown exception
     *             shall not be caught and verification is not required.
     */
    protected Object afterInvocationThrowsException(Exception e, Method method)
            throws Error, Exception {

        if (e instanceof InvocationTargetException) {
            Throwable targetThrowable = ((InvocationTargetException) e)
                    .getTargetException();
            if (targetThrowable instanceof Exception) {
                e = (Exception) targetThrowable;
            } else {
                throw (Error) targetThrowable;
            }
        }

        // is the thrown exception of the expected type?
        if (!clazz.isAssignableFrom(e.getClass())) {

            if (assertException) {
                throw new ExceptionNotThrownAssertionError(clazz, e);
            } else {
                throw e;
            }

        } else {

            // save the caught exception for further analysis
            ExceptionHolder.set(e);

            // return anything. the value does not matter but null could cause a
            // NullPointerException while un-boxing
            return safeReturnValue(method.getReturnType());
        }
    }

    /**
     * @param type
     *            a type
     * @return Returns a value of the given type
     */
    Object safeReturnValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        } else if (Void.TYPE.equals(type)) {
            return null; // any value
        } else if (Byte.TYPE.equals(type)) {
            return (byte) 0;
        } else if (Short.TYPE.equals(type)) {
            return (short) 0;
        } else if (Integer.TYPE.equals(type)) {
            return (int) 0;
        } else if (Long.TYPE.equals(type)) {
            return (long) 0;
        } else if (Float.TYPE.equals(type)) {
            return (float) 0.0;
        } else if (Double.TYPE.equals(type)) {
            return (double) 0.0;
        } else if (Boolean.TYPE.equals(type)) {
            return (boolean) false;
        } else if (Character.TYPE.equals(type)) {
            return (char) ' ';
        } else {
            throw new IllegalArgumentException("Type " + type
                    + " is not supported at the moment. Please file a bug.");
        }
    }

}