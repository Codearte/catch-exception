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
package com.googlecode.catchexception.throwable.internal;

import java.lang.reflect.Method;

import com.googlecode.catchexception.throwable.ThrowableNotThrownAssertionError;

/**
 * This abstract method invocation interceptor
 * <ul>
 * <li>delegates all method calls to the 'underlying object',
 * <li>catches throwables of a given type, and
 * <li>(optionally) asserts that an throwable of a specific type is thrown.
 * </ul>
 * 
 * @author rwoo
 * @param <E>
 *            The type of the throwable that is caught and ,optionally, verified.
 * @since 16.09.2011
 */
class AbstractThrowableProcessingInvocationHandler<E extends Throwable> {

    /**
     * See {@link #AbstractThrowableProcessingInvocationHandler(Object, Class, boolean)} .
     */
    protected Class<E> clazz;

    /**
     * See {@link #AbstractThrowableProcessingInvocationHandler(Object, Class, boolean)} .
     */
    protected Object target;

    /**
     * See {@link #AbstractThrowableProcessingInvocationHandler(Object, Class, boolean)} .
     */
    protected boolean assertThrowable;

    /**
     * @param target
     *            The object all method calls are delegated to. Must not be <code>null</code>.
     * @param clazz
     *            the type of the throwable that is to catch. Must not be <code>null</code>.
     * @param assertThrowable
     *            True if the interceptor shall throw an {@link ThrowableNotThrownAssertionError} if the method has not
     *            thrown an throwable of the expected type.
     */
    public AbstractThrowableProcessingInvocationHandler(Object target, Class<E> clazz, boolean assertThrowable) {

        if (clazz == null) {
            throw new IllegalArgumentException("throwableClazz must not be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }

        this.clazz = clazz;
        this.target = target;
        this.assertThrowable = assertThrowable;
    }

    /**
     * Must be called by the subclass before the intercepted method invocation.
     */
    protected void beforeInvocation() {

        // clear throwable cache
        ThrowableHolder.set(null);

    }

    /**
     * Must be called by the subclass after the method invocation that has not thrown a throwable.
     * 
     * @param retval
     *            The value returned by the intercepted method invocation.
     * @return The value that shall be returned by the proxy.
     * @throws Error
     *             (optionally) Thrown if the verification failed.
     */
    protected Object afterInvocation(Object retval) throws Error {

        if (assertThrowable) {

            throw new ThrowableNotThrownAssertionError(clazz);

        } else {

            // return anything. the value does not matter
            return retval;

        }

    }

    /**
     * Must be called by the subclass after the intercepted method invocation that has thrown a throwable.
     * 
     * @param e
     *            the throwable thrown by the intercepted method invocation.
     * @param method
     *            the method that was proxied
     * @return Always returns null.
     * @throws Error
     *             Thrown if the verification failed.
     * @throws Throwable
     *             The given throwable. (Re-)thrown if the thrown throwable shall not be caught and verification is not
     *             required.
     */
    protected Object afterInvocationThrowsThrowable(Throwable e, Method method) throws Error, Throwable {

        // BEGIN specific to catch-throwable
        if (e instanceof ThrowableNotThrownAssertionError) {
            throw e;
        }
        // END specific to catch-throwable

        // is the thrown throwable of the expected type?
        if (!clazz.isAssignableFrom(e.getClass())) {

            if (assertThrowable) {
                throw new ThrowableNotThrownAssertionError(clazz, e);
            } else {
                throw e;
            }

        } else {

            // save the caught throwable for further analysis
            ThrowableHolder.set(e);

            // return anything. the value does not matter but null could cause a
            // NullPointerThrowable while un-boxing
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
            throw new IllegalArgumentException("Type " + type + " is not supported at the moment. Please file a bug.");
        }
    }

}