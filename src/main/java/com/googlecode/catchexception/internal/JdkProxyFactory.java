package com.googlecode.catchexception.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.mockito.internal.creation.jmock.SearchingClassLoader;

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
    public <T, E extends Exception> T createProxy(T obj,
            Class<E> exceptionClazz, boolean assertException) {

        InvocationHandler invocationHandler = new ExceptionProcessingJdkInvocationHandler<E>(
                obj, exceptionClazz, assertException);

        // get all the interfaces (is there an easier way?)
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        Class<?> clazz = obj.getClass();
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

        // get class loader
        ClassLoader classLoader;
        if (JdkProxy.class.getClassLoader().equals(
                obj.getClass().getClassLoader())) {
            classLoader = obj.getClass().getClassLoader();
        } else {
            // combine the class loader of the object and the class loader of
            // JdkProxy
            classLoader = new SearchingClassLoader(
                    JdkProxy.class.getClassLoader(), obj.getClass()
                            .getClassLoader());
        }

        // create the proxy
        return (T) Proxy.newProxyInstance( //
                classLoader, //
                interfaces.toArray(new Class<?>[interfaces.size()]), //
                invocationHandler);
    }
}