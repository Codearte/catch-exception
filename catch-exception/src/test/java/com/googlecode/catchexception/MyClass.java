package com.googlecode.catchexception;

@SuppressWarnings("javadoc")
public class MyClass {

    public static String myStaticMethod() {
        throw new IllegalStateException("Sorry, bad time for calling!");
    }

    public static String myOtherStaticMethod(int a, String b, int[] c) {
        if (a == 123) {
            throw new IllegalArgumentException("Boring, try other arguments!");
        } else {
            return "Interesting numbers, mate!";
        }
    }
}
