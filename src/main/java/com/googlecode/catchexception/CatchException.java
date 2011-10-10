package com.googlecode.catchexception;

import org.mockito.cglib.proxy.MethodInterceptor;

import com.googlecode.catchexception.internal.DelegatingInterceptor;
import com.googlecode.catchexception.internal.ExceptionHolder;
import com.googlecode.catchexception.internal.ExceptionProcessingInterceptor;
import com.googlecode.catchexception.internal.InterfaceOnlyProxyFactory;
import com.googlecode.catchexception.internal.SubclassProxyFactory;

/**
 * The methods of this class catch and verify exceptions in <em>a single line of
 * code</em> and make them available for further analysis.
 * 
 * <h1>Documentation</h1>
 * <p>
 * <b> <a href="#1">1. How to use catch-exception?</a><br/>
 * <a href="#2">2. What is this stuff actually good for?</a> <br/>
 * <a href="#3">3. How does it work internally?</a><br/>
 * <a href="#4">4. When is the caught exception reset?</a><br/>
 * <a href="#5">5. My code throws a ClassCastException. Why?</a> <br/>
 * <a href="#6">6. The exception is not caught. Why?</a> <br/>
 * <a href="#7">7. Do I have to care about memory leaks?</a> <br/>
 * <a href="#8">8. The caught exception is not available in another thread.
 * Why?</a><br/>
 * <a href="#9">9. How do I catch an exception thrown by a static method?</a> <br/>
 * <a href="#10">10. Is there a way to get rid of the throws clause in my test
 * method?</a> <br/>
 * <a href="#11">11. Can I catch errors instead of exceptions?</a> <br/>
 * 
 * 
 * 
 * </b>
 * <p>
 * <h3 id="1">1. How to use catch-exception?</h3>
 * <p>
 * The most basic usage is:
 * <code><pre class="prettyprint lang-java">import static com.googlecode.catchexception.CatchException.*;

// call customerService.prepareBilling(Prize.Zero)
// and catch the exception if any is thrown 
catchException(customerService).prepareBilling(Prize.Zero);

// assert that an IllegalArgumentException was thrown
assert caughtException() instanceof IllegalArgumentException;</pre></code>
 * <p>
 * You can combine the two lines of code in a single one if you like:
 * <code><pre class="prettyprint lang-java">// call customerService.prepareBilling(Prize.Zero)
// and throw an ExceptionNotThrownAssertionError if
// the expected exception is not thrown 
verifyException(customerService, IllegalArgumentException.class).prepareBilling(Prize.Zero);</pre></code>
 * There is a minor difference between both variants. In the first variant you
 * must start the JVM with option <code>-ea</code> to enable the assertion. The
 * second variant does not use JDK assertions and ,therefore, always verifies
 * the caught exception.
 * <p>
 * A third variant allows you to select the type of exceptions you want to catch
 * (no verification involved):
 * <code><pre class="prettyprint lang-java">// catch IllegalArgumentExceptions but no other exceptions 
catchException(customerService, IllegalArgumentException.class).prepareBilling(Prize.Zero);</pre></code>
 * <p>
 * The fourth and last variant verifies that some exception is thrown, i.e. the
 * type of the exception does not matter:
 * <code><pre class="prettyprint lang-java">verifyException(customerService).prepareBilling(Prize.Zero);</pre></code>
 * <p>
 * In all variants you can use <code>caughtException()</code> afterwards to
 * inspect the caught exception.
 * <h3 id="2">2. What is this stuff actually good for?</h3>
 * <p>
 * This class targets concise and robust code in tests. Dadid Saff, a commiter
 * to JUnit, has <a
 * href="http://shareandenjoy.saff.net/2006/12/assertthrownexception_20.html"
 * >discussed</a> this approach in 2007. Let me summarize the arguments here.
 * <p>
 * There are two advantages of the approach proposed here in comparison to the
 * use of try/catch blocks.
 * <ul>
 * <li>The test is more concise and easier to read.
 * <li>The test cannot be corrupted by a missing assertion. Assume you forgot to
 * type <code>fail()</code> behind the method call that is expected to throw an
 * exception.
 * </ul>
 * <p>
 * There are also some advantages of this approach in comparison to test
 * runner-specific mechanisms that catch and verify exceptions.
 * <ul>
 * <li>A single test can verify more than one thrown exception.
 * <li>The test can verify the properties of the thrown exception after the
 * exception is caught.
 * <li>The test can specify by which method call the exception must be thrown.
 * <li>The test does not depend on a specific test runner (JUnit4, TestNG).
 * </ul>
 * <p>
 * <h3 id="3">3. How does it work internally?</h3>
 * <p>
 * The method <code>catchException(obj)</code> wraps the given object with a
 * proxy that catches the exception, then (optionally) verifies the exception,
 * and finally attaches the exception to the current <a
 * name="threadlocal">thread</a> for further analysis. The <a
 * href="#proxies">known limitations</a> for proxies apply.
 * <p>
 * Is both memory consumption and runtime a concern for you? Then use try/catch
 * blocks instead of this class. Because in this case the creation of proxies is
 * an unnecessary overhead. If only either memory consumption or runtime is an
 * issue for you, feel free to configure the cache of the underlying proxy
 * factories as appropriate.
 * <h3 id="4">4. When is the caught exception reset?</h3>
 * <p>
 * The Method {@link #caughtException()} returns the exception thrown by the
 * last method call on a proxied object in the current thread, i.e. it is reset
 * by calling a method on the proxied object. If the called method has not
 * thrown an exception, <code>caughtException()</code> returns null.
 * <p>
 * To reset the caught exception manually, call {@link #resetCaughtException()}.
 * At the moment there is no way to reset exceptions that have been caught in
 * other threads.
 * <h3 id="5"><a name="proxies" />5. My code throws a ClassCastException. Why?</h3>
 * <p>
 * Example:
 * <code><pre class="prettyprint lang-java">StringBuilder sb = new StringBuilder();
catchException(sb).charAt(-2); // throws ClassCastException</pre></code>
 * <p>
 * Probably you have tested a final class. Proxy factories usually try to
 * subclass the type of the proxied object. This is not possible if the original
 * class is final. But there is a way out. If the tested method belongs to an
 * interface, then you can cast the argument (here: <code>sb</code>) to that
 * interface or ,easier, change the declared type of the argument to the
 * interface type. This works because the created proxy is not longer required
 * to have the same type as the original class but it must only have the same
 * interface.
 * <code><pre class="prettyprint lang-java">// first variant
StringBuilder sb = new StringBuilder();
catchException((CharSequence) sb).charAt(-2); // works fine

// second variant
CharSequence sb = new StringBuilder();
catchException(sb).charAt(-2); // works fine</pre></code> If the tested
 * method does no belong to an interface fall back to the try/catch-blocks.
 * <h3 id="6">6. The exception is not caught. Why?</h3>
 * <p>
 * Example:
 * <code><pre class="prettyprint lang-java">ServiceImpl impl = new ServiceImpl();
catchException(impl).do(); // do() is a final method that throws an exception</pre></code>
 * <p>
 * Probably you have tested a final method. If that tested method belongs to an
 * interface you could use {@link #interfaces(Object)} to fix that problem. But
 * then the syntax starts to become ugly.
 * <code><pre class="prettyprint lang-java">Service api = new ServiceImpl();
catchException(interfaces(api)).do(); // works fine</pre></code> I recommend
 * to use try/catch blocks in such cases.
 * 
 * <h3 id="7">7. Do I have to care about memory leaks?</h3>
 * <p>
 * This library uses a {@link ThreadLocal}. ThreadLocals are known to cause
 * memory leaks if they refer to a class the garbage collector would like to
 * collect. If you use this library only for testing, then memory leaks do not
 * worry you. If you use this library for other purposes than testing, you
 * should care.
 * <p>
 * <h3 id="8">8. The caught exception is not available in another thread. Why?</h3>
 * <p>
 * The caught exception is saved <a href="#threadlocal">at the thread</a> the
 * exception is thrown in. This is the reason the exception is not visible
 * within any other thread.
 * <h3 id="9">9. How do I catch an exception thrown by a static method?</h3>
 * <p>
 * Unfortunately, catch-exception does not support this. Fall back on try/catch
 * blocks.
 * <h3 id="10">10. Is there a way to get rid of the throws clause in my test
 * method?</h3>
 * <p>
 * Example:
 * <code><pre class="prettyprint lang-java">public void testSomething() throws Exception {
    ...
    catchException(obj).do(); // do() throws a checked exception</pre></code> No,
 * although the exception is always caught you cannot omit the throws clause in
 * your test method.
 * <h3 id="11">11. Can I catch errors instead of exceptions?</h3>
 * <p>
 * At the moment you can't catch {@link Throwable throwables} that are not
 * {@link Exception exceptions}. But this could be easily changed if someone
 * wants to get this feature.
 * 
 * @author rwoo
 * @since 16.09.2011
 */
public class CatchException {

    /**
     * Returns the exception caught during the last call on the proxied object
     * in the current thread.
     * 
     * @param <E>
     *            This type parameter makes some type casts redundant.
     * @return Returns the exception caught during the last call on the proxied
     *         object in the current thread - if the call was made through a
     *         proxy that has been created via
     *         {@link #verifyException(Object, Class) verifyException()} or
     *         {@link #catchException(Object, Class) catchException()}. Returns
     *         null the proxy has not caught an exception. Returns null if the
     *         caught exception belongs to a class that is no longer
     *         {@link ClassLoader loaded}.
     */
    public static <E extends Exception> E caughtException() {
        return ExceptionHolder.<E> get();
    }

    /**
     * Use it to verify that an exception is thrown and to get access to the
     * thrown exception (for further verifications).
     * <p>
     * The following example verifies that obj.doX() throws a Exception:
     * <code><pre class="prettyprint lang-java">verifyException(obj).doX(); // catch and verify
assert "foobar".equals(caughtException().getMessage()); // further analysis
</pre></code>
     * <p>
     * If <code>doX()</code> does not throw a <code>Exception</code>, then a
     * {@link ExceptionNotThrownAssertionError} is thrown. Otherwise the thrown
     * exception can be retrieved via {@link #caughtException()}.
     * <p>
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * 
     * @param obj
     *            The instance that shall be proxied. Must not be
     *            <code>null</code>.
     * @return Returns an object that verifies that each invocation on the
     *         underlying object throws an exception.
     */
    public static <T> T verifyException(T obj) {
        return verifyException(obj, Exception.class);
    }

    /**
     * Use it to verify that an exception of specific type is thrown and to get
     * access to the thrown exception (for further verifications).
     * <p>
     * The following example verifies that obj.doX() throws a MyException:
     * <code><pre class="prettyprint lang-java">verifyException(obj, MyException.class).doX(); // catch and verify
assert "foobar".equals(caughtException().getMessage()); // further analysis
</pre></code>
     * <p>
     * If <code>doX()</code> does not throw a <code>MyException</code>, then a
     * {@link ExceptionNotThrownAssertionError} is thrown. Otherwise the thrown
     * exception can be retrieved via {@link #caughtException()}.
     * <p>
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * 
     * @param <E>
     *            The type of the exception that shall be caught.
     * @param obj
     *            The instance that shall be proxied. Must not be
     *            <code>null</code>.
     * @param clazz
     *            The type of the exception that shall be thrown by the
     *            underlying object. Must not be <code>null</code>.
     * @return Returns an object that verifies that each invocation on the
     *         underlying object throws an exception of the given type.
     */
    public static <T, E extends Exception> T verifyException(T obj,
            Class<E> clazz) {

        return processException(obj, clazz, true);
    }

    /**
     * Use it to catch an exception and to get access to the thrown exception
     * (for further verifications).
     * <p>
     * In the following example you catch exceptions that are thrown by
     * obj.doX():
     * <code><pre class="prettyprint lang-java">catchException(obj).doX(); // catch
if (caughtException() != null) {
    assert "foobar".equals(caughtException().getMessage()); // further analysis
}</pre></code>
     * If <code>doX()</code> throws a exception, then {@link #caughtException()}
     * will return the caught exception. If <code>doX()</code> does not throw a
     * exception, then {@link #caughtException()} will return <code>null</code>.
     * <p>
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
     */
    public static <T> T catchException(T obj) {

        return processException(obj, Exception.class, false);
    }

    /**
     * Use it to catch an exception of a specific type and to get access to the
     * thrown exception (for further verifications).
     * <p>
     * In the following example you catch exceptions of type MyException that
     * are thrown by obj.doX():
     * <code><pre class="prettyprint lang-java">catchException(obj, MyException.class).doX(); // catch
if (caughtException() != null) {
    assert "foobar".equals(caughtException().getMessage()); // further analysis
}</pre></code>
     * If <code>doX()</code> throws a <code>MyException</code>, then
     * {@link #caughtException()} will return the caught exception. If
     * <code>doX()</code> does not throw a <code>MyException</code>, then
     * {@link #caughtException()} will return <code>null</code>. If
     * <code>doX()</code> throws an exception of another type, i.e. not a
     * subclass but another class, then this exception is not thrown and
     * {@link #caughtException()} will return <code>null</code>.
     * <p>
     * 
     * @param <T>
     *            The type of the given <code>obj</code>.
     * 
     * @param <E>
     *            The type of the exception that shall be caught.
     * @param obj
     *            The instance that shall be proxied. Must not be
     *            <code>null</code>.
     * @param clazz
     *            The type of the exception that shall be caught. Must not be
     *            <code>null</code>.
     * @return Returns a proxy for the given object. The proxy catches
     *         exceptions of the given type when a method on the proxy is
     *         called.
     */
    public static <T, E extends Exception> T catchException(T obj,
            Class<E> clazz) {

        return processException(obj, clazz, false);
    }

    /**
     * Creates a proxy that processes exceptions thrown by the underlying
     * object.
     * <p>
     * Delegates to
     * {@link SubclassProxyFactory#createProxy(Class, MethodInterceptor)} which
     * itself might delegate to
     * {@link InterfaceOnlyProxyFactory#createProxy(Class, MethodInterceptor)}.
     */
    @SuppressWarnings("javadoc")
    private static <T, E extends Exception> T processException(T obj,
            Class<E> exceptionClazz, boolean assertException) {

        if (obj == null) {
            throw new IllegalArgumentException("obj must not be null");
        }

        return new SubclassProxyFactory().<T> createProxy(obj.getClass(),
                new ExceptionProcessingInterceptor<E>(obj, exceptionClazz,
                        assertException));

    }

    /**
     * Returns a proxy that implements all interfaces of the underlying object.
     * 
     * @param <T>
     *            must be an interface the object implements
     * @param obj
     *            the object that created proxy will delegate all calls to
     * @return Returns a proxy that implements all interfaces of the underlying
     *         object and delegates all calls to that underlying object.
     */
    public static <T> T interfaces(T obj) {

        if (obj == null) {
            throw new IllegalArgumentException("obj must not be null");
        }

        return new InterfaceOnlyProxyFactory().<T> createProxy(obj.getClass(),
                new DelegatingInterceptor(obj));
    }

    /**
     * Sets the {@link #caughtException() caught exception} to null. This does
     * not affect exceptions saved at threads other than the current one.
     * <p>
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
