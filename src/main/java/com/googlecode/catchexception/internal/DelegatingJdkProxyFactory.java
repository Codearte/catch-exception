package com.googlecode.catchexception.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

/**
 * Creates a proxy that implements all interfaces of the underlying object and
 * delegates all calls to that underlying object.
 */
public class DelegatingJdkProxyFactory {

    /**
     * @param <T>
     *            avoids a cast in source code.
     * @param obj
     *            the object the proxy delegates all calls to
     * @return Returns a proxy that implements all interfaces of the underlying
     *         object and delegates all calls to that underlying object.
     */
    @SuppressWarnings("unchecked")
    public <T> T createProxy(T obj) {

        InvocationHandler invocationHandler = new DelegatingJdkInvocationHandler(
                obj);

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

        // create the proxy
        return (T) Proxy.newProxyInstance( //
                obj.getClass().getClassLoader(), //
                interfaces.toArray(new Class<?>[interfaces.size()]), //
                invocationHandler);
    }
}