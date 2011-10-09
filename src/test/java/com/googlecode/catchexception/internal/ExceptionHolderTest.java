package com.googlecode.catchexception.internal;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests {@link ExceptionHolder}
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
public class ExceptionHolderTest {

    @Test
    public void testCaughtExceptionIsNull() throws Exception {

        ExceptionHolder.set(null);
        assertNull(ExceptionHolder.get());
    }
}
