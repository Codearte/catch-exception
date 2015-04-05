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
package com.googlecode.catchexception.throwable.apis.matcher;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Copied from Mockito internal
 */
public class Find extends BaseMatcher<String> implements Serializable {

    private static final long serialVersionUID = 8895781429480404872L;
    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return actual != null && Pattern.compile(regex).matcher((String) actual).find();
    }

    public void describeTo(Description description) {
        description.appendText("find(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")");
    }

}