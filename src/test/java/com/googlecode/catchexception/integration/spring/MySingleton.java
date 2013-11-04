package com.googlecode.catchexception.integration.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Part of {@link SpringIntegrationTest}.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
@Component
public class MySingleton {

    @Autowired
    MySessionComponent mySessionComponent;

    public void sampleAction() {
        mySessionComponent.doNothing();
    }
}
