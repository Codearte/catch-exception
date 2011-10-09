package com.googlecode.catchexception;

@SuppressWarnings("javadoc")
public class FinalMethodSomethingImpl extends PublicSomethingImpl {

    public final void doIt() {
        throw new IllegalArgumentException("test_abfdzuf");
    }

    @Override
    final public void doThrow() {
        super.doThrow();
    }
}