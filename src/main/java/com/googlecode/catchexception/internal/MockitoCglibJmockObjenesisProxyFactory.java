package com.googlecode.catchexception.internal;

import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.jmock.ClassImposterizer;

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
    public <T, E extends Exception> T createProxy(Class<?> targetClass,
            MethodInterceptor interceptor) {

        // can we subclass the class of the target?
        if (!ClassImposterizer.INSTANCE.canImposterise(targetClass)) {
            // delegate
            return fallbackProxyFactory.createProxy(targetClass, interceptor);
        }

        // create proxy
        T proxy;
        try {
            proxy = (T) ClassImposterizer.INSTANCE.imposterise(interceptor,
                    targetClass, CglibProxy.class);
        } catch (MockitoException e) {
            // delegate
            return fallbackProxyFactory.createProxy(targetClass, interceptor);
        }

        return proxy;
    }
}