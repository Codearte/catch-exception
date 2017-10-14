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

import com.googlecode.catchexception.apis.BDDCatchException;
import com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers;

/**
 * The methods of this class catch and verify exceptions in <em>a single line of
 * code</em> and make them available for further analysis.
 *
 * This Javadoc content is also available on the <a
 * href="http://code.google.com/p/catch-exception/" >catch-exception</a> web
 * page.
 *
 * <h3>Documentation</h3>
 *
 * <b> <a href="#1">1. How to use catch-exception?</a>

 * <a href="#2">2. What is this stuff actually good for?</a>

 * <a href="#3">3. How does it work internally?</a>

 * <a href="#4">4. When is the caught exception reset?</a>

 * <a href="#5">5. My code throws a ClassCastException. Why?</a>

 * <a href="#6">6. The exception is not caught. Why?</a>

 * <a href="#7">7. Do I have to care about memory leaks?</a>

 * <a href="#8">8. The caught exception is not available in another thread.
 * Why?</a>

 * <a href="#9">9. How do I catch an exception thrown by a static method?</a>

 * <a href="#11">11. Can I catch errors instead of exceptions?</a>

 *
 *
 *
 * </b>
 *
 * <h4>1. How to use catch-exception?</h4>
 *
 * The most basic usage is:
 * <code>import static com.googlecode.catchexception.CatchException.*;
 *
 * // call customerService.prepareBilling(Prize.Zero)
 * // and catch the exception if any is thrown
 * catchException(customerService).prepareBilling(Prize.Zero);
 *
 * // assert that an IllegalArgumentException was thrown
 * assert caughtException() instanceof IllegalArgumentException;</code>
 *
 * You can combine the two lines of code in a single one if you like:
 * <code>// call customerService.prepareBilling(Prize.Zero)
 * // and throw an ExceptionNotThrownAssertionError if
 * // the expected exception is not thrown
 * verifyException(customerService, IllegalArgumentException.class).prepareBilling(Prize.Zero);</code>
 * There is a minor difference between both variants. In the first variant you
 * must start the JVM with option <code>-ea</code> to enable the assertion. The
 * second variant does not use JDK assertions and ,therefore, always verifies
 * the caught exception.
 *
 * A third variant allows you to select the type of exceptions you want to catch
 * (no verification involved):
 * <code>// catch IllegalArgumentExceptions but no other exceptions
 * catchException(customerService, IllegalArgumentException.class).prepareBilling(Prize.Zero);</code>
 *
 * The fourth and last variant verifies that some exception is thrown, i.e. the
 * type of the exception does not matter:
 * <code>verifyException(customerService).prepareBilling(Prize.Zero);</code>
 *
 * In all variants you can use <code>caughtException()</code> afterwards to
 * inspect the caught exception.
 *
 * Finally, there some alternative ways to catch and verify exceptions:
 * <ul>
 * <li>{@link BDDCatchException} - a BDD-like approach,
 * <li> {@link CatchExceptionHamcrestMatchers} - Hamcrest assertions
 * </ul>
 * <h3>2. What is this stuff actually good for?</h3>
 *
 * This class targets concise and robust code in tests. Dadid Saff, a commiter
 * to JUnit, has <a
 * href="http://shareandenjoy.saff.net/2006/12/assertthrownexception_20.html"
 * >discussed</a> this approach in 2007. Let me summarize the arguments here.
 *
 * There are two advantages of the approach proposed here in comparison to the
 * use of try/catch blocks.
 * <ul>
 * <li>The test is more concise and easier to read.
 * <li>The test cannot be corrupted by a missing assertion. Assume you forgot to
 * type <code>fail()</code> behind the method call that is expected to throw an
 * exception.
 * </ul>
 *
 * There are also some advantages of this approach in comparison to test
 * runner-specific mechanisms that catch and verify exceptions.
 * <ul>
 * <li>A single test can verify more than one thrown exception.
 * <li>The test can verify the properties of the thrown exception after the
 * exception is caught.
 * <li>The test can specify by which method call the exception must be thrown.
 * <li>The test does not depend on a specific test runner (JUnit4, TestNG).
 * </ul>
 *
 * <h3>3. How does it work internally?</h3>
 *
 * The method <code>catchException(obj)</code> wraps the given object with a
 * proxy that catches the exception, then (optionally) verifies the exception,
 * and finally attaches the exception to the current <a
 * name="threadlocal">thread</a> for further analysis. The <a
 * href="#proxies">known limitations</a> for proxies apply.
 *
 * Is both memory consumption and runtime a concern for you? Then use try/catch
 * blocks instead of this class. Because in this case the creation of proxies is
 * an unnecessary overhead. If only either memory consumption or runtime is an
 * issue for you, feel free to configure the cache of the underlying proxy
 * factories as appropriate.
 * <h3>4. When is the caught exception reset?</h3>
 *
 * The Method {@link #caughtException()} returns the exception thrown by the
 * last method call on a proxied object in the current thread, i.e. it is reset
 * by calling a method on the proxied object. If the called method has not
 * thrown an exception, <code>caughtException()</code> returns null.
 *
 * To reset the caught exception manually, call {@link #resetCaughtException()}.
 * At the moment there is no way to reset exceptions that have been caught in
 * other threads.
 * <h3>5. My code throws a ClassCastException. Why?</h3>
 *
 * Example:
 * <code>StringBuilder sb = new StringBuilder();
 * catchException(sb).charAt(-2); // throws ClassCastException</code>
 *
 * Probably you have tested a final class. Proxy factories usually try to
 * subclass the type of the proxied object. This is not possible if the original
 * class is final. But there is a way out. If the tested method belongs to an
 * interface, then you can cast the argument (here: <code>sb</code>) to that
 * interface or ,easier, change the declared type of the argument to the
 * interface type. This works because the created proxy is not longer required
 * to have the same type as the original class but it must only have the same
 * interface.
 * <code>// first variant
 * StringBuilder sb = new StringBuilder();
 * catchException((CharSequence) sb).charAt(-2); // works fine
 *
 * // second variant
 * CharSequence sb = new StringBuilder();
 * catchException(sb).charAt(-2); // works fine</code> If the tested
 * method does no belong to an interface fall back to the try/catch-blocks or
 * use <a
 * href="http://code.google.com/p/catch-exception/wiki/Dependencies">Powermock
 * </a>.
 * <code>// example for
 * PowerMock with JUnit4
 * &#064;RunWith(PowerMockRunner.class)
 * &#064;PrepareForTest({ MyFinalType.class })
 * public class MyTest {
 * </code>
 * <h3>6. Do I have to care about memory leaks?</h3>
 *
 * This library uses a {@link ThreadLocal}. ThreadLocals are known to cause
 * memory leaks if they refer to a class the garbage collector would like to
 * collect. If you use this library only for testing, then memory leaks do not
 * worry you. If you use this library for other purposes than testing, you
 * should care.
 *
 * <h3>7. The caught exception is not available in another thread. Why?</h3>
 *
 * The caught exception is saved <a href="#threadlocal">at the thread</a> the
 * exception is thrown in. This is the reason the exception is not visible
 * within any other thread.
 * <h3>8. Is there a way to get rid of the throws clause in my test
 * method?</h3>
 *
 * Example:
 * <code>public void testSomething() throws Exception {
 * ...
 * catchException(obj).do(); // do() throws a checked exception</code> No,
 * although the exception is always caught you cannot omit the throws clause in
 * your test method.
 * <h3>11. Can I catch errors instead of exceptions?</h3>
 *
 * Yes, have a look at
 * {@code com.googlecode.catchexception.throwable.CatchThrowable} (in module
 * catch-throwable).
 *
 * @author rwoo
 * @since 16.09.2011
 */
public class CatchException {

    /**
     * Returns the exception caught during the last call in the current thread.
     *
     * @param <E> This type parameter makes some type casts redundant.
     * @return Returns the exception caught during the last call in the current
     * thread - if the call was made through a proxy that has been created via
     * {@link #verifyException(ThrowingCallable, Class) verifyException()} or
     * {@link #catchException(ThrowingCallable, Class) catchException()}. Returns
     * null when no exception was caught.
     */
    public static <E extends Exception> E caughtException() {
        return ExceptionHolder.get();
    }

    public static <E extends Exception> E caughtException(Class<E> caughtExceptionType) {
        return ExceptionHolder.get();
    }

    /**
     * Use it to verify that an exception is thrown and to get access to the
     * thrown exception (for further verifications).
     *
     * The following example verifies that obj.doX() throws a Exception:
     * <code>verifyException(obj).doX(); // catch and verify
     * assert "foobar".equals(caughtException().getMessage()); // further analysis
     * </code>
     *
     * If <code>doX()</code> does not throw a <code>Exception</code>, then a
     * {@link ExceptionNotThrownAssertionError} is thrown. Otherwise the thrown
     * exception can be retrieved via {@link #caughtException()}.
     *
     *
     * @param actor The instance that shall be proxied. Must not be
     *              <code>null</code>.
     */
    public static void verifyException(ThrowingCallable actor) {
        verifyException(actor, Exception.class);
    }

    /**
     * Use it to verify that an exception of specific type is thrown and to get
     * access to the thrown exception (for further verifications).
     *
     * The following example verifies that obj.doX() throws a MyException:
     * <code>verifyException(obj, MyException.class).doX(); // catch and verify
     * assert "foobar".equals(caughtException().getMessage()); // further analysis
     * </code>
     *
     * If <code>doX()</code> does not throw a <code>MyException</code>, then a
     * {@link ExceptionNotThrownAssertionError} is thrown. Otherwise the thrown
     * exception can be retrieved via {@link #caughtException()}.
     *
     *
     * @param actor The instance that shall be proxied. Must not be
     *              <code>null</code>.
     * @param clazz The type of the exception that shall be thrown by the
     *              underlying object. Must not be <code>null</code>.
     */
    public static void verifyException(ThrowingCallable actor, Class<? extends Exception> clazz) {
        validateArguments(actor, clazz);
        catchException(actor, clazz, true);
    }

    /**
     * Use it to catch an exception and to get access to the thrown exception
     * (for further verifications).
     *
     * In the following example you catch exceptions that are thrown by
     * obj.doX():
     * <code>catchException(obj).doX(); // catch
     * if (caughtException() != null) {
     * assert "foobar".equals(caughtException().getMessage()); // further analysis
     * }</code>
     * If <code>doX()</code> throws a exception, then {@link #caughtException()}
     * will return the caught exception. If <code>doX()</code> does not throw a
     * exception, then {@link #caughtException()} will return <code>null</code>.
     *
     *
     * @param actor The instance that shall be proxied. Must not be
     *              <code>null</code>.
     */
    public static void catchException(com.googlecode.catchexception.ThrowingCallable actor) {
        validateArguments(actor, Exception.class);
        catchException(actor, Exception.class, false);
    }

    /**
     * Use it to catch an exception of a specific type and to get access to the
     * thrown exception (for further verifications).
     *
     * In the following example you catch exceptions of type MyException that
     * are thrown by obj.doX():
     * <code>catchException(obj, MyException.class).doX(); // catch
     * if (caughtException() != null) {
     * assert "foobar".equals(caughtException().getMessage()); // further analysis
     * }</code>
     * If <code>doX()</code> throws a <code>MyException</code>, then
     * {@link #caughtException()} will return the caught exception. If
     * <code>doX()</code> does not throw a <code>MyException</code>, then
     * {@link #caughtException()} will return <code>null</code>. If
     * <code>doX()</code> throws an exception of another type, i.e. not a
     * subclass but another class, then this exception is not thrown and
     * {@link #caughtException()} will return <code>null</code>.
     *
     *
     * @param actor The instance that shall be proxied. Must not be
     *              <code>null</code>.
     * @param clazz The type of the exception that shall be caught. Must not be
     *              <code>null</code>.
     */
    public static void catchException(ThrowingCallable actor, Class<? extends Exception> clazz) {
        validateArguments(actor, clazz);
        catchException(actor, clazz, false);
    }

    private static void catchException(ThrowingCallable actor, Class<? extends Exception> clazz,
                                       boolean assertException) {
        resetCaughtException();
        Exception exception = ExceptionCaptor.captureThrowable(actor);
        if (exception == null) {
            if (!assertException) {
                return;
            } else {
                throw new ExceptionNotThrownAssertionError(clazz);
            }
        }
        // is the thrown exception of the expected type?
        if (clazz.isAssignableFrom(exception.getClass())) {
            ExceptionHolder.set(exception);
        } else {
            if (assertException) {
                throw new ExceptionNotThrownAssertionError(clazz, exception);
            } else {
                ExceptionUtil.sneakyThrow(exception);
            }
        }
    }

    private static void validateArguments(ThrowingCallable actor, Class<? extends Exception> clazz) {
        if (actor == null) throw new IllegalArgumentException("obj must not be null");
        if (clazz == null) throw new IllegalArgumentException("exceptionClazz must not be null");
    }

    /**
     * Sets the {@link #caughtException() caught exception} to null. This does
     * not affect exceptions saved at threads other than the current one.
     *
     * Actually you probably never need to call this method because each method
     * call on a proxied object in the current thread resets the caught
     * exception. But if you want to improve test isolation or if you want to
     * 'clean up' after testing (to avoid memory leaks), call the method before
     * or after testing.
     */
    public static void resetCaughtException() {
        ExceptionHolder.set(null);
    }

}
