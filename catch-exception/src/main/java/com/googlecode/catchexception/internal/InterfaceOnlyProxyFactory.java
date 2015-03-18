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

import java.util.HashSet;
import java.util.Set;

import io.codearte.catchexception.shade.mockito.cglib.proxy.Enhancer;
import io.codearte.catchexception.shade.mockito.cglib.proxy.MethodInterceptor;

/**
 * This {@link ProxyFactory} create proxies that implements all interfaces of
 * the underlying object including the marker interface
 * {@link InterfaceOnlyProxy}. But in contrast to the proxies created by
 * {@link SubclassProxyFactory} such a proxy does not subclass the class of the
 * underlying object.
 * 
 * @author rwoo
 */
public class InterfaceOnlyProxyFactory implements ProxyFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.googlecode.catchexception.internal.ProxyFactory#createProxy(java.
     * lang.Class, org.mockito.cglib.proxy.MethodInterceptor)
     */
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Class<?> targetClass, MethodInterceptor interceptor) {

        // get all the interfaces (is there an easier way?)
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        Class<?> clazz = targetClass;
        while (true) {
            for (Class<?> interfaze : clazz.getInterfaces()) {
                interfaces.add(interfaze);
            }
            if (clazz.getSuperclass() == null) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        interfaces.add(InterfaceOnlyProxy.class);

        return (T) Enhancer.create(Object.class,
                interfaces.toArray(new Class<?>[interfaces.size()]),
                interceptor);
    }
}