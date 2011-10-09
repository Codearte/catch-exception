package com.googlecode.catchexception;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.CatchException.interfaces;
import static com.googlecode.catchexception.CatchException.resetCaughtException;
import static com.googlecode.catchexception.CatchException.verifyException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UnknownFormatConversionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.catchexception.internal.SubclassProxy;
import com.googlecode.catchexception.internal.ExceptionHolder;
import com.googlecode.catchexception.internal.InterfaceOnlyProxy;

/**
 * Tests {@link CatchException}.
 * 
 * @author rwoo
 * @since 16.09.2011
 * 
 */
@SuppressWarnings("javadoc")
public class CatchExceptionTest {

    private final List<String> list = new ArrayList<String>();

    /**
     * The message of the exception thrown by new ArrayList<String>().get(0).
     */
    private final String expectedMessage = "Index: 0, Size: 0";

    @Before
    public void setUp() {
        onSetup();
    }

    @After
    public void tearDown() {
        onTeardown();
    }

    protected void onSetup() {
        // set any exception so that we have clear state before the test
        ExceptionHolder.set(new HttpRetryException("detail", 0));
    }

    protected void onTeardown() {
    }

    private static class NonVisible implements Something {

        private NonVisible(String fsdfsdfd) {
        }

        public void doNothing() {
            //
        }

        public void doThrow() {
            throw new UnsupportedOperationException("siodsdnsgz");
        }

        public void doesNotBelongToTheInterface() {
            //
        }

        public void doThrowAssertionError() {
            //
        }
    }

    @Test
    public void testProxyFactory_CanProxyNonVisibleClass() throws Exception {
        catchException(Collections.unmodifiableList(list)).size();
        assertNull(caughtException());

        catchException(Collections.unmodifiableList(list)).get(0);
        assertTrue(caughtException() instanceof IndexOutOfBoundsException);

        catchException(new NonVisible("sdfdfd")).doesNotBelongToTheInterface();
        assertNull(caughtException());
    }

    @Test
    public void testProxyFactory_AnynomousType() throws Exception {

        Something obj = new PublicSomethingImpl() {

            @Override
            public void doThrow() {
                throw new UnknownFormatConversionException("sdfd");
            }
        };

        catchException(obj).doThrow();
        assertTrue(caughtException() instanceof UnknownFormatConversionException);
    }

    @Test
    public void testProxyFactory_ProtectedConstructor() {

        catchException((Something) new ProtectedSomethingImpl()).doThrow();
        assertTrue(caughtException() instanceof UnsupportedOperationException);
    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotProxyFinalClass()
            throws Exception {

        try {
            FinalSomethingImpl obj = new FinalSomethingImpl();
            catchException(obj).doesNotBelongToAnyInterface();
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtException() is not defined now
        }

        try {
            FinalSomethingImpl obj = new FinalSomethingImpl();
            catchException(obj).doThrow();
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtException() is not defined now
        }

        try {
            StringBuilder obj = new StringBuilder();
            catchException(obj).charAt(-2);
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtException() is not defined now
        }

    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotProxyFinalClass_theWorkaroundUsesInterface()
            throws Exception {
        Something obj = new FinalSomethingImpl();
        catchException(obj).doThrow();
        assertTrue(caughtException() instanceof UnsupportedOperationException);

        resetCaughtException();
        catchException((Something) new FinalSomethingImpl()).doThrow();
        assertTrue(caughtException() instanceof UnsupportedOperationException);

        {
            resetCaughtException();
            StringBuilder sb = new StringBuilder();
            catchException((CharSequence) sb).charAt(-2);
            assertTrue(caughtException() instanceof StringIndexOutOfBoundsException);
        }

        {
            resetCaughtException();
            CharSequence sb = new StringBuilder();
            catchException(sb).charAt(-2);
            assertTrue(caughtException() instanceof StringIndexOutOfBoundsException);
        }
    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotInterceptFinalMethod()
            throws Exception {
        try {
            FinalMethodSomethingImpl obj = new FinalMethodSomethingImpl();
            catchException(obj).doThrow();
            fail("UnsupportedOperationException is not expected to be caught");
        } catch (UnsupportedOperationException e) {
            // OK
            // return value of caughtException() is not defined now
        }
    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotInterceptFinalMethod_theWorkaroundUsesInterface()
            throws Exception {

        {
            Something obj = new FinalMethodSomethingImpl();
            catchException(interfaces(obj)).doThrow();
            assertTrue(caughtException() instanceof UnsupportedOperationException);
        }

        {
            FinalMethodSomethingImpl obj = new FinalMethodSomethingImpl();
            catchException((Something) interfaces(obj)).doThrow();
            assertTrue(caughtException() instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testProxyFactory_ProxyForClassNotInterface() throws Exception {
        ArrayList<String> arrayList = new ArrayList<String>();
        // must not fail due to a ClassCastException
        catchException((List<String>) arrayList).get(0);
        assertTrue(caughtException() instanceof IndexOutOfBoundsException);
    }

    @Test
    public void testCatchException_ObjExc_noExceptionThrown() throws Exception {

        catchException(list, IndexOutOfBoundsException.class).size();
        assertNull(caughtException());
    }

    @Test
    public void testCatchException_ObjExc_actualClassThrown() throws Exception {

        // test for actual class
        catchException(list, IndexOutOfBoundsException.class).get(0);
        assertEquals(expectedMessage, caughtException().getMessage());
    }

    @Test
    public void testCatchException_ObjExc_subClassOfExpectedThrown()
            throws Exception {

        // test for super class
        catchException(list, RuntimeException.class).get(0);
        assertEquals(expectedMessage, caughtException().getMessage());
    }

    @Test
    public void testCatchException_ObjExc_superClassOfExpectedThrown()
            throws Exception {

        try {
            catchException(list, ArrayIndexOutOfBoundsException.class).get(0);
            fail("IndexOutOfBoundsException is expected (shall not be caught)");
        } catch (IndexOutOfBoundsException e) {
            assertNull(caughtException());
        }
    }

    @Test
    public void testCatchException_ObjExc_otherClassThanExpectedThrown()
            throws Exception {

        try {
            catchException(list, IllegalArgumentException.class).get(0);
            fail("IndexOutOfBoundsException is expected (shall not be caught)");
        } catch (IndexOutOfBoundsException e) {
            assertNull(caughtException());
        }
    }

    @Test
    public void testCatchException_ObjExc_missingArgument_Exception()
            throws Exception {

        // test validation of the arguments
        try {
            catchException(list, null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("exceptionClazz must not be null", e.getMessage());
        }
    }

    @Test
    public void testCatchException_ObjExc_missingArgument_Object()
            throws Exception {

        try {
            catchException(null, IllegalArgumentException.class);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testVerifyException_ObjExc_noExceptionThrown() throws Exception {

        try {
            verifyException(list, IndexOutOfBoundsException.class).size();
            fail("ExceptionNotThrownAssertionError is expected");
        } catch (ExceptionNotThrownAssertionError e) {
            assertNull(caughtException());
            assertEquals("Neither an exception of type "
                    + IndexOutOfBoundsException.class.getName()
                    + " nor another exception was thrown", e.getMessage());
        }

    }

    @Test
    public void testVerifyException_ObjExc_actualClassThrown() throws Exception {

        // test for actual class
        verifyException(list, IndexOutOfBoundsException.class).get(0);
        assertEquals(expectedMessage, caughtException().getMessage());
    }

    @Test
    public void testVerifyException_ObjExc_subClassOfExpectedThrown()
            throws Exception {

        // test for super class
        verifyException(list, RuntimeException.class).get(0);
        assertEquals(expectedMessage, caughtException().getMessage());
    }

    @Test
    public void testVerifyException_ObjExc_superClassOfExpectedThrown()
            throws Exception {

        // test for sub class
        try {
            verifyException(list, ArrayIndexOutOfBoundsException.class).get(0);
            fail("ExceptionNotThrownAssertionError is expected");
        } catch (ExceptionNotThrownAssertionError e) {
            assertNull(caughtException());
            assertEquals("Exception of type "
                    + ArrayIndexOutOfBoundsException.class.getName()
                    + " expected but was not thrown."
                    + " Instead an exception of type "
                    + IndexOutOfBoundsException.class + " with message '"
                    + expectedMessage + "' was thrown.", e.getMessage());
        }
    }

    @Test
    public void testVerifyException_ObjExc_otherClassThanExpectedThrown()
            throws Exception {

        // test for other exception type
        try {
            verifyException(list, IllegalArgumentException.class).get(0);
            fail("ExceptionNotThrownAssertionError is expected");
        } catch (ExceptionNotThrownAssertionError e) {
            assertNull(caughtException());
            assertEquals(
                    "Exception of type "
                            + IllegalArgumentException.class.getName()
                            + " expected but was not thrown."
                            + " Instead an exception of type "
                            + IndexOutOfBoundsException.class
                            + " with message '" + expectedMessage
                            + "' was thrown.", e.getMessage());
        }

    }

    @Test
    public void testVerifyException_ObjExc_missingArgument_Exception()
            throws Exception {

        // test validation of the arguments
        try {
            verifyException(list, null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("exceptionClazz must not be null", e.getMessage());
        }
    }

    @Test
    public void testVerifyException_ObjExc_missingArgument_Object()
            throws Exception {

        try {
            verifyException(null, IllegalArgumentException.class);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testVerifyException_Obj_noExceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        // no exception thrown by size()
        try {
            verifyException(list).size();
            fail("ExceptionNotThrownAssertionError is expected");
        } catch (ExceptionNotThrownAssertionError e) {
            assertNull(caughtException());
            assertEquals("Exception expected but not thrown", e.getMessage());
        }
    }

    @Test
    public void testVerifyException_Obj_exceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        verifyException(list).get(0);
        assertEquals(expectedMessage, caughtException().getMessage());
    }

    @Test
    public void testVerifyException_Obj_missingArgument_Object()
            throws Exception {

        // test validation of the arguments
        try {
            verifyException(null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testCatchException_Obj_noExceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        // no exception thrown by size()
        catchException(list).size();
        assertNull(caughtException());
    }

    @Test
    public void testCatchException_Obj_exceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        catchException(list).get(0);
        assertEquals(expectedMessage, caughtException().getMessage());
    }

    @Test
    public void testCatchException_Obj_missingArgument_Object()
            throws Exception {

        // test validation of the arguments
        try {
            catchException(null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testCatchException_TestedMethodThrowsError_CgLibProxy()
            throws Exception {

        PublicSomethingImpl obj = new PublicSomethingImpl();

        try {
            PublicSomethingImpl proxy = catchException(obj);
            assertTrue(proxy instanceof SubclassProxy);
            proxy.doThrowAssertionError();
            fail("AssertionError is expected");
        } catch (AssertionError e) {
            assertEquals("123", e.getMessage());
        }
    }

    @Test
    public void testCatchException_TestedMethodThrowsError_JdkProxy()
            throws Exception {

        Something obj = new FinalSomethingImpl();

        try {
            Something proxy = catchException(obj);
            assertTrue(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowAssertionError();
            fail("AssertionError is expected");
        } catch (AssertionError e) {
            assertEquals("123", e.getMessage());
        }
    }

}
