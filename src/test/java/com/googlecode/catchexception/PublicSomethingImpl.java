package com.googlecode.catchexception;

@SuppressWarnings("javadoc")
public class PublicSomethingImpl implements Something {

    public PublicSomethingImpl() {
        super();
    }

    public void doNothing() {
        //
    }

    public void doThrow() {
        throw new UnsupportedOperationException();
    }

    public void doesNotBelongToAnyInterface() {
        //
    }

    public void doThrowAssertionError() {
        throw new AssertionError(123);
    }
}