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
package com.googlecode.catchexception;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests how PowerMock prepared classes reduce the proxy related limitations of
 * {@link CatchException}.
 * 
 * @author rwoo
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FinalSomethingImpl.class, FinalMethodSomethingImpl.class,
        StringBuilder.class, FinalMethodSomethingImpl.class })
@SuppressWarnings("javadoc")
public class PowermockPreparedTest {

    // /**
    // * Class that encapsulate a mock and its corresponding invocation control.
    // */
    // private static class MockData<T> {
    // private final MockitoMethodInvocationControl methodInvocationControl;
    //
    // private final T mock;
    //
    // MockData(MockitoMethodInvocationControl methodInvocationControl, T mock)
    // {
    // this.methodInvocationControl = methodInvocationControl;
    // this.mock = mock;
    // }
    //
    // public MockitoMethodInvocationControl getMethodInvocationControl() {
    // return methodInvocationControl;
    // }
    //
    // public T getMock() {
    // return mock;
    // }
    // }
    //
    // /**
    // * Clear state in Mockito that retains memory between tests
    // */
    // private static class MockitoStateCleaner implements Runnable {
    // public void run() {
    // clearMockProgress();
    // clearConfiguration();
    // }
    //
    // private void clearMockProgress() {
    // clearThreadLocalIn(ThreadSafeMockingProgress.class);
    // }
    //
    // private void clearConfiguration() {
    // clearThreadLocalIn(GlobalConfiguration.class);
    // }
    //
    // private void clearThreadLocalIn(Class<?> cls) {
    // Whitebox.getInternalState(cls, ThreadLocal.class).set(null);
    // final Class<?> clazz;
    // if (ClassLoaderUtil.hasClass(cls,
    // ClassLoader.getSystemClassLoader())) {
    // clazz = ClassLoaderUtil.loadClass(cls,
    // ClassLoader.getSystemClassLoader());
    // } else {
    // clazz = ClassLoaderUtil.loadClass(cls, cls.getClassLoader());
    // }
    // Whitebox.getInternalState(clazz, ThreadLocal.class).set(null);
    // }
    // }
    //
    // private static boolean isFinalJavaSystemClass(Class type) {
    // return type.getName().startsWith("java.")
    // && Modifier.isFinal(type.getModifiers());
    // }
    //
    // private static String toInstanceName(Class clazz) {
    // String className = clazz.getSimpleName();
    // if (className.length() == 0)
    // return clazz.getName();
    // else
    // return (new StringBuilder())
    // .append(className.substring(0, 1).toLowerCase())
    // .append(className.substring(1)).toString();
    // }
    //
    // private static <T, E extends Exception> T processException(T obj,
    // Class<E> exceptionClazz, boolean assertException) {
    //
    // if (obj == null) {
    // throw new IllegalArgumentException("obj must not be null");
    // }
    //
    // ExceptionProcessingInterceptor<E> interceptor = new
    // ExceptionProcessingInterceptor<E>(
    // obj, exceptionClazz, assertException);
    //
    // // PowerMockito.spy(Object object)
    // Object object = obj;
    // // MockCreator.mock(Whitebox.getType(object), false, true, object, null,
    // // (Method[])null);
    // Class<T> type = (Class<T>) Whitebox.getType(object);
    // boolean isStatic = false;
    // boolean isSpy = true;
    // Object delegator = object;
    // MockSettings mockSettings = null;
    // Method methods[] = null;
    // // MockCreator.mock(Class type, boolean isStatic, boolean isSpy, Object
    // // delegator, MockSettings mockSettings, Method methods[])
    // T mock = null;
    // String mockName = toInstanceName(type);
    // MockRepository.addAfterMethodRunner(new MockitoStateCleaner());
    // Class typeToMock;
    // if (isFinalJavaSystemClass(type))
    // typeToMock = (new ClassReplicaCreator()).createClassReplica(type);
    // else
    // typeToMock = type;
    // MockData<T> mockData = createMethodInvocationControl(mockName,
    // typeToMock, methods, isSpy, delegator, mockSettings);
    // mock = mockData.getMock();
    // if (isFinalJavaSystemClass(type) && !isStatic) {
    // mock = Whitebox.newInstance(type);
    // DefaultFieldValueGenerator.fillWithDefaultValues(mock);
    // }
    // if (isStatic)
    // MockRepository.putStaticMethodInvocationControl(type,
    // mockData.getMethodInvocationControl());
    // else
    // MockRepository.putInstanceMethodInvocationControl(mock,
    // mockData.getMethodInvocationControl());
    // if (isSpy)
    // (new LenientCopyTool()).copyToMock(delegator, mock);
    //
    // return mock;
    // }
    //
    // private static <T> MockData<T> createMethodInvocationControl(
    // final String mockName, Class<T> type, Method[] methods,
    // boolean isSpy, Object delegator, MockSettings mockSettings) {
    // final MockSettingsImpl settings;
    // if (mockSettings == null) {
    // settings = (MockSettingsImpl) Mockito.withSettings();
    // } else {
    // settings = (MockSettingsImpl) mockSettings;
    // }
    //
    // if (isSpy) {
    // settings.defaultAnswer(Mockito.CALLS_REAL_METHODS);
    // }
    //
    // settings.initiateMockName(type);
    // MockHandler<T> mockHandler = new MockHandler<T>(settings);
    // MethodInterceptorFilter filter = new MethodInterceptorFilter(
    // mockHandler, settings);
    // final T mock = (T) ClassImposterizer.INSTANCE.imposterise(filter, type);
    // final MockitoMethodInvocationControl invocationControl = new
    // MockitoMethodInvocationControl(
    // filter, isSpy && delegator == null ? new Object() : delegator,
    // mock, methods);
    //
    // return new MockData<T>(invocationControl, mock);
    // }

    @Test
    public void testProxyFactory_KnownLimitation_CannotProxyFinalClass()
            throws Exception {

        // no limitation :-)
        {
            FinalSomethingImpl obj = new FinalSomethingImpl();
            catchException(obj).doesNotBelongToAnyInterface();
            assertNull(caughtException());
        }

        // no limitation :-)
        {
            FinalSomethingImpl obj = new FinalSomethingImpl();
            catchException(obj).doThrow();
            assertTrue(caughtException() instanceof UnsupportedOperationException);
        }

        // limitation still exists if the proxied class is a Java system class
        try {
            StringBuilder obj = new StringBuilder();
            catchException(obj).charAt(-2);
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtException() is not defined now
        }

    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotInterceptFinalMethod()
            throws Exception {

        // limitation still exists if the method is final
        try {
            FinalMethodSomethingImpl obj = new FinalMethodSomethingImpl();
            catchException(obj).doThrow();
            fail("UnsupportedOperationException is not expected to be caught");
        } catch (UnsupportedOperationException e) {
            // OK
            // return value of caughtException() is not defined now
        }
    }
}
