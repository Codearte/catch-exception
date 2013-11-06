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
package com.googlecode.catchexception.integration.spring;

import static com.googlecode.catchexception.apis.CatchExceptionBdd.thenThrown;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * The test code is taken from an excellent <a href=
 * "http://blog.piotrturski.net/2013/06/codegenerationexception-and-proxies.html"
 * >blog</a> of Piotr.Turski.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class SpringIntegrationTest {

    @Configuration
    @ComponentScan
    static class TestAppContext {
    } // just registers 2 components

    @Autowired
    MySingleton myController;

    @Test
    public void test1() {
        when(new MySessionComponent()).doError();
        thenThrown(IllegalStateException.class);

        // this could fail due to duplicate class definitions
        myController.sampleAction();
    }

    // @Test
    // public void test2() {
    // myController.sampleAction();
    //
    // when(new MySessionComponent()).doError();
    // // CatchException.caughtException().printStackTrace();
    // thenThrown(IllegalStateException.class);
    // }
}
