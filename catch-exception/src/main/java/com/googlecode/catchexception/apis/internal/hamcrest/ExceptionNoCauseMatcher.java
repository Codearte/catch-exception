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
package com.googlecode.catchexception.apis.internal.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Creates a {@link Matcher matcher} that matches an exception that has no
 * {@link Throwable#getCause() cause}.
 * 
 * @author rwoo
 * 
 * @param <T>
 *            an exception subclass
 */
public class ExceptionNoCauseMatcher<T extends Exception> extends
        BaseMatcher<T> {

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.Matcher#matches(java.lang.Object)
     */
    @Override
    public boolean matches(Object obj) {
        if (!(obj instanceof Exception)) {
            return false;
        }

        Exception exception = (Exception) obj;

        return exception.getCause() == null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("has no cause");
    }

}
