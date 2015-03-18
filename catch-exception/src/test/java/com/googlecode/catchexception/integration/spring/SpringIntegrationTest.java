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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.interfaces;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.thenThrown;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;

import io.codearte.catchexception.shade.mockito.cglib.proxy.Enhancer;
import io.codearte.catchexception.shade.mockito.cglib.proxy.NoOp;
import org.junit.Ignore;
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

    @Autowired
    MySessionComponent mySessionComponent;

    @Test
    public void test_Issue10_1() {
        when(new MySessionComponent()).doError();
        thenThrown(IllegalStateException.class);

        // this could fail due to duplicate class definitions
        myController.sampleAction();
    }

    /**
     * The test is ignored because activating the test would make the other test
     * for the same issue meaningless. Sounds a bit weird but this is how it
     * works.
     */
    @Test
    @Ignore
    public void test_Issue10_2_deactivatedToPreventSideEffectOnOtherTest() {
        myController.sampleAction();

        when(new MySessionComponent()).doError();
        // CatchException.caughtException().printStackTrace();
        thenThrown(IllegalStateException.class);
    }

    @Test
    @Ignore
    public void test_Issue13_1_failsATM() {

        when(mySessionComponent).doError();
        thenThrown(IllegalStateException.class);
    }

    @Test
    public void test_Issue13_2_workaroundIfThereAreInterfaces() {

        when((IMySessionComponent) interfaces(mySessionComponent)).doError();
        thenThrown(IllegalStateException.class);
    }

    @Test
    @Ignore
    public void test_Issue13_3_proxyCanBeProxiedTwice_UsingImposteriser_failsATM() {

        MySessionComponent comp1 = catchException(mySessionComponent);
        MySessionComponent comp2 = catchException(comp1);
        comp2.doError();
    }

    @Test
    @Ignore
    public void test_Issue13_3_proxyCanBeProxiedTwice_UsingCgLibEnhancer() {

        MySessionComponent comp1 = (MySessionComponent) Enhancer.create(
                MySessionComponent.class, new NoOp() {
                });

        MySessionComponent comp2 = (MySessionComponent) Enhancer.create(
                comp1.getClass(), new NoOp() {
                });

        MySessionComponent comp3 = (MySessionComponent) Enhancer.create(
                comp2.getClass(), new NoOp() {
                });

        when(comp1).doError(); // fails here
        when(comp2).doError();
        when(comp3).doError();
    }

}
