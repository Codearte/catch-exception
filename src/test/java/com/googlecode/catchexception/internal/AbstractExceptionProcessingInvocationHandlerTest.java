package com.googlecode.catchexception.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * AbstractExceptionProcessingInvocationHandler
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
public class AbstractExceptionProcessingInvocationHandlerTest {

    @Test
    public void testSafeReturnValue() throws Exception {
        AbstractExceptionProcessingInvocationHandler<Exception> handler = new AbstractExceptionProcessingInvocationHandler<Exception>(
                new Object(), Exception.class, false);

        assertEquals((byte) 0, handler.safeReturnValue(Byte.TYPE));
        assertEquals((short) 0, handler.safeReturnValue(Short.TYPE));
        assertEquals(0, handler.safeReturnValue(Integer.TYPE));
        assertEquals((long) 0, handler.safeReturnValue(Long.TYPE));
        assertEquals((float) 0.0, handler.safeReturnValue(Float.TYPE));
        assertEquals(0.0, handler.safeReturnValue(Double.TYPE));
        assertEquals(false, handler.safeReturnValue(Boolean.TYPE));
        assertEquals(' ', handler.safeReturnValue(Character.TYPE));

    }
}
