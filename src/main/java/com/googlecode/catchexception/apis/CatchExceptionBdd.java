package com.googlecode.catchexception.apis;

import org.fest.assertions.Assertions;
import org.fest.assertions.ThrowableAssert;

import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.ExceptionNotThrownAssertionError;

/**
 * Supports <a
 * href="http://en.wikipedia.org/wiki/Behavior_Driven_Development">BDD</a>
 * approach to catch and verify exceptions (<i>given/when/then</i>).
 * <p>
 * EXAMPLE:
 * <code><pre class="prettyprint lang-java">// given an empty list
List myList = new ArrayList();

// when we try to get first element of the list
when(myList).get(1);

// then we expect an IndexOutOfBoundsException
then(caughtException())
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index: 1, Size: 0") 
        .hasNoCause();
        
// then we expect an IndexOutOfBoundsException (alternatively)
thenThrown(IndexOutOfBoundsException.class);
</pre></code>
 * 
 * @author rwoo
 * 
 */
public class CatchExceptionBdd {

    /**
     * Use it together with {@link #then(Exception)} or
     * {@link #thenThrown(Class)} in order to catch an exception and to get
     * access to the thrown exception (for further verifications).
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * 
     * @param obj
     *            The instance that shall be proxied. Must not be
     *            <code>null</code>.
     * @return Returns a proxy for the given object. The proxy catches
     *         exceptions of the given type when a method on the proxy is
     *         called.
     * @see CatchException#catchException(Object)
     */
    public static <T> T when(T obj) {
        return CatchException.catchException(obj);
    }

    /**
     * Throws an assertion if no exception is thrown or if an exception of an
     * unexpected type is thrown.
     * <p>
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">// given a list with nine members
List myList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

// when we try to get the 500th member of the fellowship
when(myList).get(500);

// then we expect an IndexOutOfBoundsException
thenThrown(IndexOutOfBoundsException.class);
</pre></code>
     * 
     * @param actualExceptionClazz
     *            the expected type of the caught exception.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void thenThrown(Class actualExceptionClazz) {
        Exception e = CatchException.caughtException();
        if (e == null) {
            // no exception caught -> assertion failed
            throw new ExceptionNotThrownAssertionError(actualExceptionClazz);
        } else if (!actualExceptionClazz.isAssignableFrom(CatchException
                .caughtException().getClass())) {
            // caught exception is of wrong type -> assertion failed
            throw new ExceptionNotThrownAssertionError(actualExceptionClazz, e);
        } else {
            // the caught exception is of the expected type -> nothing to do :-)
        }
    }

    /**
     * Enables <a href="http://code.google.com/p/fest/">FEST</a> assertions
     * about the caught exception.
     * <p>
     * EXAMPLE:
     * <code><pre class="prettyprint lang-java">// given an empty list
List myList = new ArrayList();

// when we try to get first element of the list
when(myList).get(1);

// then we expect an IndexOutOfBoundsException
then(caughtException())
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index: 1, Size: 0") 
        .hasNoCause();
</pre></code>
     * 
     * @param actualException
     *            the value to be the target of the assertions methods.
     * @return Returns the created assertion object.
     * @see Assertions#assertThat(Throwable)
     */
    public static ThrowableAssert then(Exception actualException) {
        // delegate to FEST assertions
        return Assertions.assertThat(actualException);
    }

}
