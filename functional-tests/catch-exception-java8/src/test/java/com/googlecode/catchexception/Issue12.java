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
package com.googlecode.catchexception;

import com.googlecode.catchexception.apis.CatchExceptionAssertJ;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;

public class Issue12 {

    @Test
    public void shouldAssertWithThen() throws Exception {

        // given: an empty list
        List myList = new ArrayList();

        // when: we try to get the first element of the list
        when(myList).get(1);

        // then: we expect an IndexOutOfBoundsException
        BDDAssertions.then(caughtException())
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("Index: 1, Size: 0")
                .hasNoCause();

    }

    @Test
    public void shouldAssertWithAssertThat() throws Exception {

        // given: an empty list
        List myList = new ArrayList();

        // when: we try to get the first element of the list
        when(myList).get(1);

        // then: we expect an IndexOutOfBoundsException
        Assertions.assertThat(caughtException())
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("Index: 1, Size: 0")
                .hasNoCause();

    }
}
