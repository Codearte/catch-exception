package com.googlecode.catchexception.support;

import static com.googlecode.catchexception.CatchException.verifyException;

import org.junit.Test;

import com.google.common.base.Supplier;

/**
 * Demonstrates how to catch exceptions that are thrown by constructors using
 * Google Guava.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
public class CatchExceptionInConstructorTest {

    private static class Thing {

        public Thing(String data) {
            throw new IllegalArgumentException("bad data: " + data);
        }
    }

    @Test
    public void testExceptionThrownInConstructor() throws Exception {

        Supplier<Thing> builder = new Supplier<Thing>() {
            @Override
            public Thing get() {
                return new Thing("baddata");
            }
        };
        verifyException(builder, IllegalArgumentException.class).get();
    }
}
