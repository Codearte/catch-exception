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
import static com.googlecode.catchexception.throwable.CatchThrowable.verifyThrowable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.catchexception.throwable.internal.ThrowableHolder;

/**
 * Tests {@link CatchThrowable}.
 *
 * @author rwoo
 * @since 16.09.2011
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

    @Test
    public void testCatchException_ObjExc_noExceptionThrown() throws Exception {

        catchThrowable(list::size, IndexOutOfBoundsException.class);
        assertNull(caughtThrowable());
    }

    @Test
    public void testCatchException_ObjExc_actualClassThrown() throws Exception {

        // test for actual class
        catchThrowable(() -> list.get(0), IndexOutOfBoundsException.class);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testCatchException_ObjExc_subClassOfExpectedThrown() throws Exception {

        // test for super class
        catchThrowable(() -> list.get(0), RuntimeException.class);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testCatchException_ObjExc_superClassOfExpectedThrown() throws Exception {

        try {
            catchThrowable(() -> list.get(0), ArrayIndexOutOfBoundsException.class);
            fail("IndexOutOfBoundsException is expected (shall not be caught)");
        } catch (IndexOutOfBoundsException e) {
            assertNull(caughtThrowable());
        }
    }

    @Test
    public void testCatchException_ObjExc_otherClassThanExpectedThrown() throws Exception {

        try {
            catchThrowable(() -> list.get(0), IllegalArgumentException.class);
            fail("IndexOutOfBoundsException is expected (shall not be caught)");
        } catch (IndexOutOfBoundsException e) {
            assertNull(caughtThrowable());
        }
    }

    @Test
    public void testCatchException_ObjExc_missingArgument_Exception() throws Exception {

        // test validation of the arguments
        try {
            catchThrowable(list::size, null);
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
            verifyThrowable(list::size, IndexOutOfBoundsException.class);
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
        verifyThrowable(() -> list.get(0), IndexOutOfBoundsException.class);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testverifyThrowable_ObjExc_subClassOfExpectedThrown() throws Exception {

        // test for super class
        verifyThrowable(() -> list.get(0), RuntimeException.class);
        assertEquals(expectedMessage, caughtThrowable().getMessage());
    }

    @Test
    public void testverifyThrowable_ObjExc_superClassOfExpectedThrown() throws Exception {

        // test for sub class
        try {
            verifyThrowable(() -> list.get(0), ArrayIndexOutOfBoundsException.class);
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
            verifyThrowable(() -> list.get(0), IllegalArgumentException.class);
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
            verifyThrowable(list::size, null);
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
            verifyThrowable(list::size);
            fail("ThrowableNotThrownAssertionError is expected");
        } catch (ThrowableNotThrownAssertionError e) {
            assertNull(caughtThrowable());
            assertEquals("Throwable expected but not thrown", e.getMessage());
        }
    }

    @Test
    public void testverifyThrowable_Obj_exceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        verifyThrowable(() -> list.get(0));
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
        catchThrowable(list::size);
        assertNull(caughtThrowable());
    }

    @Test
    public void testCatchException_Obj_exceptionThrown() throws Exception {

        List<String> list = new ArrayList<String>();

        catchThrowable(() -> list.get(0));
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
    public void testProtected() throws Exception {
        PublicSomethingImpl obj = new PublicSomethingImpl();
        catchThrowable(obj::dooo);
        assertTrue(caughtThrowable() instanceof MyThrowable);
    }

}
