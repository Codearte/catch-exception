package com.googlecode.catchexception;

import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.stubbing.ConsecutiveStubbing;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.Invocation;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.googlecode.catchexception.internal.ExceptionHolder;

/**
 * Proof of concept for catching exceptions that are thrown by static methods.
 * 
 * @author rwoo
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ MyClass.class })
@SuppressWarnings("javadoc")
public class PowermockPreparedTest2 {

    @Before
    public void before() {
        PowerMockito.mockStatic(MyClass.class);
    }

    @Test
    public void test_catch_exception_in_static_method_without_parameters()
            throws Exception {

        catchStaticException(MyClass.myStaticMethod());
        assertTrue(caughtException() instanceof IllegalStateException);
    }

    @Test
    public void test_catch_exception_in_static_method_with_parameters()
            throws Exception {

        catchStaticException(MyClass.myOtherStaticMethod(123, "456", new int[] {
                78, 9 }));
        assertTrue(caughtException() instanceof IllegalArgumentException);
    }

    @Test
    public void test_catch_exception_in_static_method_with_parameters_no_exception_expected()
            throws Exception {

        String result = catchStaticException(MyClass.myOtherStaticMethod(42,
                "42", new int[] { 42, 42 }));
        assertNull(caughtException());
        assertEquals("Interesting numbers, mate!", result);
    }

    /**
     * This method could become part of CatchException.
     */
    @SuppressWarnings("unchecked")
    private <T> T catchStaticException(T methodCall)
            throws NoSuchFieldException, IllegalAccessException {

        // define that the real method shall be called
        ConsecutiveStubbing<String> stubbing = (ConsecutiveStubbing<String>) when(
                methodCall).thenCallRealMethod();

        // get the method that shall be invoked
        Field field = ConsecutiveStubbing.class
                .getDeclaredField("invocationContainerImpl");
        field.setAccessible(true);
        InvocationContainerImpl invocationContainer = (InvocationContainerImpl) field
                .get(stubbing);
        StubbedInvocationMatcher subbedInvocation = invocationContainer
                .getStubbedInvocations().get(0);
        Invocation invocation = subbedInvocation.getInvocation();
        Method method = invocation.getMethod();
        Object[] rawArguments = invocation.getRawArguments();

        // invoke the static method
        try {
            T result = (T) method.invoke(null, rawArguments);

            // no exception is thrown
            ExceptionHolder.set(null);

            return result;

        } catch (InvocationTargetException e) {

            // exception is thrown
            Throwable targetThrowable = e.getTargetException();
            if (targetThrowable instanceof Exception) {
                ExceptionHolder.set((Exception) targetThrowable);
            } else {
                // silently ignore the throwable
            }

            return null;
        }
    }

}
