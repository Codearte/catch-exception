package com.googlecode.catchexception.internal;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;

/**
 * This {@link ProxyFactory} uses {@link Proxy JDK proxies} to create proxies,
 * i.e. the created proxy implements all interfaces of the underlying objects
 * including the marker interface {@link JdkProxy}.
 */
class JdkProxyFactory implements ProxyFactory {

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
        interfaces.add(JdkProxy.class);

        // create interceptor
        // MethodInterceptor interceptor = new
        // ExceptionProcessingMockitoCglibMethodInterceptor<E>(
        // obj, exceptionClazz, assertException);

        return (T) Enhancer.create(Object.class,
                interfaces.toArray(new Class<?>[interfaces.size()]),
                interceptor);
    }
}