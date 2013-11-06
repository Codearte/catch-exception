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
