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

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.DelegatingMockitoMethodProxy;
import org.mockito.internal.creation.cglib.CGLIBHacker;

/**
 * This {@link AbstractThrowableProcessingInvocationHandler} implements
 * {@link MethodInterceptor} for Mockito's cglib variant.
 * 
 * @author rwoo, federico.gaule at gmail.com
 * @param <E>
 *            The type of the throwable that shall be caught and (optionally)
 *            verified
 */
public class ThrowableProcessingInterceptor<E extends Throwable> extends
        AbstractThrowableProcessingInvocationHandler<E> implements
        MethodInterceptor {

    /**
     * We use this object to change the naming policy that is used by
     * {@link MethodProxy#helper}. The new naming policy avoids duplicate class
     * definitions.
     */
    private CGLIBHacker cglibHacker = new CGLIBHacker();

    @SuppressWarnings("javadoc")
    public ThrowableProcessingInterceptor(Object target, Class<E> clazz,
            boolean assertThrowable) {
        super(target, clazz, assertThrowable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mockito.cglib.proxy.MethodInterceptor#intercept(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[],
     * org.mockito.cglib.proxy.MethodProxy)
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args,
            MethodProxy proxy) throws Throwable {

        beforeInvocation();

        cglibHacker.setMockitoNamingPolicy(new DelegatingMockitoMethodProxy(
                proxy));

        try {

            Object retval = proxy.invoke(target, args);

            return afterInvocation(retval);

        } catch (Throwable e) {

            return afterInvocationThrowsThrowable(e, method);
        }
    }
}