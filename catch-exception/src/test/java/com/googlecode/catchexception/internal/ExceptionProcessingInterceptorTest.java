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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.internal.creation.cglib.MockitoNamingPolicy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * Tests {@link ExceptionProcessingInterceptorTest}
 * 
 * @author federico.gaule at gmail.com
 */
@SuppressWarnings("javadoc")
public class ExceptionProcessingInterceptorTest {

    /**
     * Checks fix for: when using catch-exception and spring proxy on same
     * class, both framework tries to generate same class name and the latter
     * one fails.
     * 
     * @throws Throwable
     */
    @Test
    @PrepareForTest(Method.class)
    public void intercept_useMockitoNamingPolicy() throws Throwable {

        TestClazz testObject = new TestClazz();

        Method method = PowerMockito.mock(Method.class);
        Object[] args = new Object[0];
        MethodProxy methodProxy = MethodProxy.create(TestClazz.class,
                Void.class, "()V", "methodToBeProxy", "methodToBeProxy");

        ExceptionProcessingInterceptor<Exception> instance = new ExceptionProcessingInterceptor<Exception>(
                testObject, Exception.class, false);
        instance.intercept(testObject, method, args, methodProxy);

        Object currentNamingPolicy = getCurrentNamingPolicy(methodProxy);
        Assert.assertSame("namingPolicy", currentNamingPolicy,
                MockitoNamingPolicy.INSTANCE);

    }

    private Object getCurrentNamingPolicy(MethodProxy methodProxy)
            throws NoSuchFieldException, IllegalAccessException {
        Class<?> cglibMethodProxyClass = methodProxy.getClass();
        while (cglibMethodProxyClass != MethodProxy.class) {
            cglibMethodProxyClass = methodProxy.getClass().getSuperclass();
        }
        Field createInfoField = cglibMethodProxyClass
                .getDeclaredField("createInfo");

        createInfoField.setAccessible(true);
        Object createInfo = createInfoField.get(methodProxy);
        Field namingPolicyField = createInfo.getClass().getDeclaredField(
                "namingPolicy");
        namingPolicyField.setAccessible(true);

        return namingPolicyField.get(createInfo);
    }

    private static class TestClazz {
        @SuppressWarnings("unused")
        public void methodToBeProxy() {
        }
    }
}
