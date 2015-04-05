/**
 * Copyright (C) ${year} rwoo@gmx.de
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
package com.googlecode.catchexception.throwable.apis.internal.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Creates a {@link Matcher matcher} that matches an throwable that has no {@link Throwable#getCause() cause}.
 * 
 * @author rwoo
 * 
 * @param <T>
 *            an throwable subclass
 */
public class ThrowableNoCauseMatcher<T extends Throwable> extends BaseMatcher<T> {

    /*
     * (non-Javadoc)
     * 
     * @see org.hamcrest.Matcher#matches(java.lang.Object)
     */
    @Override
    public boolean matches(Object obj) {
        if (!(obj instanceof Throwable))
            return false;

        Throwable throwable = (Throwable) obj;

        return throwable.getCause() == null;
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
