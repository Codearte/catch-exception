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

import java.lang.reflect.Method;

import io.codearte.catchexception.shade.mockito.cglib.proxy.MethodInterceptor;
import io.codearte.catchexception.shade.mockito.cglib.proxy.MethodProxy;

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