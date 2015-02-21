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