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
