package com.googlecode.catchexception.internal;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

/**
 * This {@link ProxyFactory} uses Mockito's jmock package to create proxies.
 */
public class MockitoCglibJmockObjenesisProxyFactory implements ProxyFactory {

    /**
     * That proxy factory is used if this factory cannot be used.
     */
    private ProxyFactory fallbackProxyFactory = new JdkProxyFactory();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.googlecode.catchexception.internal.ProxyFactory#createProxy(java.
     * lang.Object, java.lang.Class, boolean)
     */
    @SuppressWarnings("unchecked")
    public <T, E extends Exception> T createProxy(T obj,
            Class<E> exceptionClazz, boolean assertException) {

        // validate arguments
        if (obj == null) {
            throw new IllegalArgumentException("obj must not be null");
        }
        if (exceptionClazz == null) {
            throw new IllegalArgumentException(
                    "exceptionClazz must not be null");
        }

        if (!ClassImposterizer.INSTANCE.canImposterise(obj.getClass())) {
            // delegate
            return fallbackProxyFactory.createProxy(obj, exceptionClazz,
                    assertException);
        }

        // create interceptor
        MethodInterceptor interceptor = new ExceptionProcessingMockitoCglibMethodInterceptor<E>(
                obj, exceptionClazz, assertException);

        // create proxy
        T proxy;
        try {
            proxy = (T) ClassImposterizer.INSTANCE.imposterise(interceptor,
                    obj.getClass(), CglibProxy.class);
        } catch (MockitoException e) {
            // delegate
            return fallbackProxyFactory.createProxy(obj, exceptionClazz,
                    assertException);
        }

        // (do we need this any time? just copied from Mockito's MockUtil)
        new LenientCopyTool().copyToMock(obj, proxy);

        return proxy;
    }
}