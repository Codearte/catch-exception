package com.googlecode.catchexception.internal;

import java.util.HashSet;
import java.util.Set;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;

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