/**
 * Copyright (C) 2011 rwoo@gmx.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.catchexception.throwable;

import static com.googlecode.catchexception.throwable.CatchThrowable.catchThrowable;
import static com.googlecode.catchexception.throwable.CatchThrowable.caughtThrowable;
import static com.googlecode.catchexception.throwable.CatchThrowable.interfaces;
import static com.googlecode.catchexception.throwable.CatchThrowable.resetCaughtThrowable;
import static com.googlecode.catchexception.throwable.CatchThrowable.verifyThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

import com.googlecode.catchexception.throwable.internal.InterfaceOnlyProxy;
import com.googlecode.catchexception.throwable.internal.ThrowableHolder;

/**
 * Tests {@link CatchThrowable}.
 * 
 * @author rwoo
 * @since 16.09.2011
 * 
 */
@SuppressWarnings("javadoc")
public class CatchThrowableTest {

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
        ThrowableHolder.set(new HttpRetryException("detail", 0));
    }

    protected void onTeardown() {
    }

    private static class NonVisible implements Something {

        private NonVisible(String fsdfsdfd) {
        }

        @Override
        public void doNothing() {
            //
        }

        @Override
        public void doThrow() {
            throw new UnsupportedOperationException("siodsdnsgz");
        }

        public void doesNotBelongToTheInterface() {
            //
        }

        @Override
        public void doThrowAssertionError() {
            //
        }

        @Override
        public void doThrowNoSuchMethodError() {
            //
        }
    }

    @Test
    public void testProxyFactory_CanProxyNonVisibleClass() throws Exception {
        catchThrowable(Collections.unmodifiableList(list)).size();
        assertNull(caughtThrowable());

        catchThrowable(Collections.unmodifiableList(list)).get(0);
        assertTrue(caughtThrowable() instanceof IndexOutOfBoundsException);

        catchThrowable(new NonVisible("sdfdfd")).doesNotBelongToTheInterface();
        assertNull(caughtThrowable());
    }

    @Test
    public void testProxyFactory_AnynomousType() throws Exception {

        Something obj = new PublicSomethingImpl() {

            @Override
            public void doThrow() {
                throw new UnknownFormatConversionException("sdfd");
            }
        };

        catchThrowable(obj).doThrow();
        assertTrue(caughtThrowable() instanceof UnknownFormatConversionException);
    }

    @Test
    public void testProxyFactory_ProtectedConstructor() {

        catchThrowable((Something) new ProtectedSomethingImpl()).doThrow();
        assertTrue(caughtThrowable() instanceof UnsupportedOperationException);
    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotProxyFinalClass() throws Exception {

        try {
            FinalSomethingImpl obj = new FinalSomethingImpl();
            catchThrowable(obj).doesNotBelongToAnyInterface();
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtThrowable() is not defined now
        }

        try {
            FinalSomethingImpl obj = new FinalSomethingImpl();
            catchThrowable(obj).doThrow();
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtThrowable() is not defined now
        }

        try {
            StringBuilder obj = new StringBuilder();
            catchThrowable(obj).charAt(-2);
            fail("Exception expected as the class is final");
        } catch (ClassCastException e) {
            // OK. e.printStackTrace();
            // return value of caughtThrowable() is not defined now
        }

    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotProxyFinalClass_theWorkaroundUsesInterface() throws Exception {
        Something obj = new FinalSomethingImpl();
        catchThrowable(obj).doThrow();
        assertTrue(caughtThrowable() instanceof UnsupportedOperationException);

        resetCaughtThrowable();
        catchThrowable((Something) new FinalSomethingImpl()).doThrow();
        assertTrue(caughtThrowable() instanceof UnsupportedOperationException);

        {
            resetCaughtThrowable();
            StringBuilder sb = new StringBuilder();
            catchThrowable((CharSequence) sb).charAt(-2);
            assertTrue(caughtThrowable() instanceof StringIndexOutOfBoundsException);
        }

        {
            resetCaughtThrowable();
            CharSequence sb = new StringBuilder();
            catchThrowable(sb).charAt(-2);
            assertTrue(caughtThrowable() instanceof StringIndexOutOfBoundsException);
        }
    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotInterceptFinalMethod() throws Exception {
        try {
            FinalMethodSomethingImpl obj = new FinalMethodSomethingImpl();
            catchThrowable(obj).doThrow();
            fail("UnsupportedOperationException is not expected to be caught");
        } catch (UnsupportedOperationException e) {
            // OK
            // return value of caughtThrowable() is not defined now
        }
    }

    @Test
    public void testProxyFactory_KnownLimitation_CannotInterceptFinalMethod_theWorkaroundUsesInterface()
            throws Exception {

        {
            Something obj = new FinalMethodSomethingImpl();
            catchThrowable(interfaces(obj)).doThrow();
            assertTrue(caughtThrowable() instanceof UnsupportedOperationException);
        }

        {
            FinalMethodSomethingImpl obj = new FinalMethodSomethingImpl();
            catchThrowable((Something) interfaces(obj)).doThrow();
            assertTrue(caughtThrowable() instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testProxyFactory_ProxyForClassNotInterface() throws Exception {
        ArrayList<String> arrayList = new ArrayList<String>();
        // must not fail due to a ClassCastException
        catchThrowable((List<String>) arrayList).get(0);
        assertTrue(caughtThrowable() instanceof IndexOutOfBoundsException);
    }

    @Test
    public void testCatchException_ObjExc_noExceptionThrown() throws Exception {

        catchThrowable(list, IndexOutOfBoundsException.class).size();
        assertNull(caughtThrowable());
    }

    @Test
    public void testCatchException_ObjExc_actualClassThrown() throws Exception {

        // test for actual class
        catchThrowable(list, IndexOutOfBoundsException.class).get(0);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testCatchException_ObjExc_subClassOfExpectedThrown() throws Exception {

        // test for super class
        catchThrowable(list, RuntimeException.class).get(0);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testCatchException_ObjExc_superClassOfExpectedThrown() throws Exception {

        try {
            catchThrowable(list, ArrayIndexOutOfBoundsException.class).get(0);
            fail("IndexOutOfBoundsException is expected (shall not be caught)");
        } catch (IndexOutOfBoundsException e) {
            assertNull(caughtThrowable());
        }
    }

    @Test
    public void testCatchException_ObjExc_otherClassThanExpectedThrown() throws Exception {

        try {
            catchThrowable(list, IllegalArgumentException.class).get(0);
            fail("IndexOutOfBoundsException is expected (shall not be caught)");
        } catch (IndexOutOfBoundsException e) {
            assertNull(caughtThrowable());
        }
    }

    @Test
    public void testCatchException_ObjExc_missingArgument_Exception() throws Exception {

        // test validation of the arguments
        try {
            catchThrowable(list, null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("throwableClazz must not be null", e.getMessage());
        }
    }

    @Test
    public void testCatchException_ObjExc_missingArgument_Object() throws Exception {

        try {
            catchThrowable(null, IllegalArgumentException.class);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testverifyThrowable_ObjExc_noExceptionThrown() throws Exception {

        try {
            verifyThrowable(list, IndexOutOfBoundsException.class).size();
            fail("ThrowableNotThrownAssertionError is expected");
        } catch (ThrowableNotThrownAssertionError e) {
            assertNull(caughtThrowable());
            assertEquals("Neither a throwable of type " + IndexOutOfBoundsException.class.getName()
                    + " nor another throwable was thrown", e.getMessage());
        }

    }

    @Test
    public void testverifyThrowable_ObjExc_actualClassThrown() throws Exception {

        // test for actual class
        verifyThrowable(list, IndexOutOfBoundsException.class).get(0);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testverifyThrowable_ObjExc_subClassOfExpectedThrown() throws Exception {

        // test for super class
        verifyThrowable(list, RuntimeException.class).get(0);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testverifyThrowable_ObjExc_superClassOfExpectedThrown() throws Exception {

        // test for sub class
        try {
            verifyThrowable(list, ArrayIndexOutOfBoundsException.class).get(0);
            fail("ThrowableNotThrownAssertionError is expected");
        } catch (ThrowableNotThrownAssertionError e) {
            assertNull(caughtThrowable());
            assertEquals("Throwable of type " + ArrayIndexOutOfBoundsException.class.getName()
                    + " expected but was not thrown." + " Instead a throwable of type "
                    + IndexOutOfBoundsException.class + " with message '" + expectedMessage + "' was thrown.",
                    e.getMessage());
        }
    }

    @Test
    public void testverifyThrowable_ObjExc_otherClassThanExpectedThrown() throws Exception {

        // test for other exception type
        try {
            verifyThrowable(list, IllegalArgumentException.class).get(0);
            fail("ThrowableNotThrownAssertionError is expected");
        } catch (ThrowableNotThrownAssertionError e) {
            assertNull(caughtThrowable());
            assertEquals("Throwable of type " + IllegalArgumentException.class.getName()
                    + " expected but was not thrown." + " Instead a throwable of type "
                    + IndexOutOfBoundsException.class + " with message '" + expectedMessage + "' was thrown.",
                    e.getMessage());
        }

    }

    @Test
    public void testverifyThrowable_ObjExc_missingArgument_Exception() throws Exception {

        // test validation of the arguments
        try {
            verifyThrowable(list, null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("throwableClazz must not be null", e.getMessage());
        }
    }

    @Test
    public void testverifyThrowable_ObjExc_missingArgument_Object() throws Exception {

        try {
            verifyThrowable(null, IllegalArgumentException.class);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testverifyThrowable_Obj_noExceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        // no exception thrown by size()
        try {
            verifyThrowable(list).size();
            fail("ThrowableNotThrownAssertionError is expected");
        } catch (ThrowableNotThrownAssertionError e) {
            assertNull(caughtThrowable());
            assertEquals("Throwable expected but not thrown", e.getMessage());
        }
    }

    @Test
    public void testverifyThrowable_Obj_exceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        verifyThrowable(list).get(0);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testverifyThrowable_Obj_missingArgument_Object() throws Exception {

        // test validation of the arguments
        try {
            verifyThrowable(null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testCatchException_Obj_noExceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        // no exception thrown by size()
        catchThrowable(list).size();
        assertNull(caughtThrowable());
    }

    @Test
    public void testCatchException_Obj_exceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        catchThrowable(list).get(0);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testCatchException_Obj_missingArgument_Object() throws Exception {

        // test validation of the arguments
        try {
            catchThrowable(null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testCatchException_TestedMethodThrowsError_CgLibProxy() throws Exception {

        PublicSomethingImpl obj = new PublicSomethingImpl();

        // catch AssertionError
        {
            PublicSomethingImpl proxy = catchThrowable(obj);
            assertFalse(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowAssertionError();
            assertTrue(caughtThrowable() instanceof AssertionError);
        }

        resetCaughtThrowable();

        // don't catch AssertionError
        try {
            PublicSomethingImpl proxy = catchThrowable(obj, NoSuchMethodError.class);
            assertFalse(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowAssertionError();
            fail("AssertionError must not be caught");
        } catch (AssertionError e) {
            if (e.getMessage().equals("AssertionError must not be caught")) {
                throw e;
            }
            assertEquals("123", e.getMessage());
        }

        resetCaughtThrowable();

        // catch NoSuchMethodError
        {
            PublicSomethingImpl proxy = catchThrowable(obj, NoSuchMethodError.class);
            assertFalse(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowNoSuchMethodError();
            assertTrue(caughtThrowable() instanceof NoSuchMethodError);
        }
    }

    @Test
    public void testCatchException_TestedMethodThrowsError_JdkProxy() throws Exception {

        Something obj = new FinalSomethingImpl();

        // catch AssertionError

        {
            Something proxy = catchThrowable(obj);
            assertTrue(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowAssertionError();
            assertTrue(caughtThrowable() instanceof AssertionError);
        }

        resetCaughtThrowable();

        // don't catch AssertionError
        try {
            Something proxy = catchThrowable(obj, NoSuchMethodError.class);
            assertTrue(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowAssertionError();
            fail("AssertionError must not be caught");
        } catch (AssertionError e) {
            if (e.getMessage().equals("AssertionError must not be caught")) {
                throw e;
            }
            assertEquals("123", e.getMessage());
        }

        // catch NoSuchMethodError
        {
            Something proxy = catchThrowable(obj, NoSuchMethodError.class);
            assertTrue(proxy instanceof InterfaceOnlyProxy);
            proxy.doThrowNoSuchMethodError();
            assertTrue(caughtThrowable() instanceof NoSuchMethodError);
        }
    }

    @Test
    public void testInterfaces_MissingArgument() throws Exception {

        try {
            interfaces(null);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            assertEquals("obj must not be null", e.getMessage());
        }
    }

    @Test
    public void testInterfaces_doesNotSubclass() throws Exception {

        try {
            @SuppressWarnings("unused")
            PublicSomethingImpl obj = interfaces(new PublicSomethingImpl());
            fail("ClassCastException is expected");
        } catch (ClassCastException e) {
            // OK
        }
    }

    @Test
    public void testInterfaces_delegates() throws Exception {

        try {
            Something obj = interfaces(new PublicSomethingImpl());
            obj.doThrow();
            fail("UnsupportedOperationException is expected");
        } catch (UnsupportedOperationException e) {
            // OK
        }
    }

}
