package com.googlecode.catchexception.integration.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Part of {@link SpringIntegrationTest}.
 * 
 * @author rwoo
 * 
 */
@SuppressWarnings("javadoc")
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MySessionComponent {

    public void doError() {
        throw new IllegalStateException();
    }

    public void doNothing() {
    }
}
