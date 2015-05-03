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

import io.codearte.catchexception.shade.mockito.cglib.proxy.MethodInterceptor;
import io.codearte.catchexception.shade.mockito.exceptions.base.MockitoException;
import io.codearte.catchexception.shade.mockito.internal.creation.jmock.ClassImposterizer;

import java.lang.reflect.Modifier;

/**
 * This {@link ProxyFactory} uses Mockito's jmock package to create proxies that
 * subclass from the target's class.
 *
 * @author rwoo
 */
public class SubclassProxyFactory implements ProxyFactory {

    /**
     * That proxy factory is used if this factory cannot be used.
     */
    private ProxyFactory fallbackProxyFactory = new InterfaceOnlyProxyFactory();

    /*
     * (non-Javadoc)
     *
     * @see
     * com.googlecode.catchexception.internal.ProxyFactory#createProxy(java.
     * lang.Object, java.lang.Class, boolean)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<?> targetClass, MethodInterceptor interceptor) {

        // can we subclass the class of the target?
        if (!isTypeMockable(targetClass)) {

            // delegate
            return fallbackProxyFactory.<T>createProxy(targetClass,
                    interceptor);
        }

        // create proxy
        T proxy;
        try {

            proxy = (T) ClassImposterizer.INSTANCE.imposterise(interceptor,
                    targetClass);

        } catch (MockitoException e) {

            // delegate
            return fallbackProxyFactory.<T>createProxy(targetClass,
                    interceptor);
        }

        return proxy;
    }

    public boolean isTypeMockable(Class<?> type) {
        return !type.isPrimitive() && !Modifier.isFinal(type.getModifiers());
    }
}