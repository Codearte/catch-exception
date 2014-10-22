# Catch-exception

Catch and verify exceptions in a single line of code

[![Build Status](https://travis-ci.org/Codearte/catch-exception.svg)](https://travis-ci.org/Codearte/catch-exception)

This library catches exceptions in a single line of code and makes them available for further analysis.

# Usage
The most basic usage is:

```java
import static com.googlecode.catchexception.CatchException.*;

// given: an empty list
List myList = new ArrayList();

// when: we try to get the first element of the list
// then: catch the exception if any is thrown
catchException(myList).get(1);

// then: we expect an IndexOutOfBoundsException
assert caughtException() instanceof IndexOutOfBoundsException;
```

The last both lines of code can be combined in a single line of code if you like:

```java
verifyException(myList, IndexOutOfBoundsException.class).get(1);
```
More information about the usage you find in the [Javadoc documentation](http://javadoc.catch-exception.googlecode.com/hg/index.html?com/googlecode/catchexception/CatchException.html).

# BDD-like
If you prefer a BDD-like approach, you can use [BDDAssertions](http://joel-costigliola.github.io/assertj/core/api/org/assertj/core/api/BDDAssertions.html) and BDDCatchException for another code style:

```java
import static com.googlecode.catchexception.CatchException.*;
import static com.googlecode.catchexception.apis.BDDCatchException.*;
import static org.assertj.core.api.BDDAssertions.then;

// given: an empty list
List myList = new ArrayList();

// when: we try to get the first element of the list
when(myList).get(1);

// then: we expect an IndexOutOfBoundsException
then(caughtException())
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index: 1, Size: 0")
        .hasNoCause();
```
The assertions used here are part of [AssertJ 1.6.0](http://joel-costigliola.github.io/assertj/).

# Hamcrest
If you prefer [Hamcrest](http://code.google.com/p/hamcrest/) matchers to express assertions, you can use [CatchExceptionHamcrestMatchers](http://javadoc.catch-exception.googlecode.com/hg/index.html?com/googlecode/catchexception/apis/CatchExceptionHamcrestMatchers.html) with the following code style:

```java
import static com.googlecode.catchexception.CatchException.*;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.*;

// given: an empty list
List myList = new ArrayList();

// when: we try to get the first element of the list
catchException(myList).get(1);

// then: we expect an IndexOutOfBoundsException with message "Index: 1, Size: 0"
assertThat(caughtException(),
  allOf(
    is(IndexOutOfBoundsException.class),
    hasMessage("Index: 1, Size: 0"),
    hasNoCause()
  )
);
```

# Catch constructor exceptions
Catch-exception does not provide an API to to catch exceptions that are thrown by constructors. Use try-catch-blocks instead. Alternatively, you can use the builder pattern if this makes sense anyway for your application:

```java
import com.google.common.base.Supplier; // Google Guava

Supplier<Thing> builder = new Supplier<Thing>() {
   @Override
    public Thing get() {
       return new Thing("baddata");
   }
};
verifyException(builder).get();
```

Thanks to the community for the example.

# Catch throwables
If you want to catch both throwables and exceptions have a look at the [catch-throwable packages](http://javadoc.catch-exception.googlecode.com/hg/index.html?com/googlecode/catchexception/throwable/package-summary.html). They provide the same API as the catch-exception packages but they belong to a different maven module.

# JUnit4
If you want to handle expected exceptions, the [documentation](http://javadoc.catch-exception.googlecode.com/hg/index.html?com/googlecode/catchexception/CatchException.html) of catch-exception names some general reasons to prefer catch-exception in comparison to mechanisms that are provided by testing frameworks.

But some reasons that are specific to JUnit4 are outlined only here.

## Collecting errors
If you want to combine the JUnit4's rules [ExpectedException](http://kentbeck.github.com/junit/javadoc/latest/org/junit/rules/ExpectedException.html) and [ErrorCollector](http://kentbeck.github.com/junit/javadoc/latest/org/junit/rules/ErrorCollector.html) you will find out: this won't work.

Catch-exception instead can be easily combined with the error collecting rule:

```java
@Rule
public ErrorCollector collector = new ErrorCollector();

@Test
public void testErrorCollectorWithExpectedException() {

    // collect first error
    collector.checkThat("a", equalTo("b"));

    // collect second error
    catchException(new ArrayList()).get(1);
    collector.checkThat(caughtException(), is(IllegalArgumentException.class));

    // collect third error
    collector.checkThat(1, equalTo(2));
}
```
## Theories respectively parameterized tests
Sometimes you want to test for an [optional exception in a parameterized test](http://stackoverflow.com/questions/7275859/testing-for-optional-exception-in-parameterized-junit-4-test?rq=1). JUnit4's [ExpecteException](http://kentbeck.github.com/junit/javadoc/latest/org/junit/rules/ExpectedException.html) rule does not help in this case. This is another use case where catch-exception comes in quite handy.

# Download
Go to the [Installation page](http://code.google.com/p/catch-exception/wiki/Installation) to get the latest release. This page provides also the Maven coordinates, prerequisites, and information about dependencies to other libraries.

# Future enhancements
Nothing to do at the moment. There are no open issues and no open feature requests (last update: 10 Feb 2012).

# Credits
Thanks to Szczepan Faber who made some suggestions about a BDD-like syntax for catch-exception. Finally, his ideas triggered the enhancements that came with the 1.0.4 release.

# Questions, Suggestions, Issues
Questions and suggestions are welcome and can be sent to the [discussion group](http://groups.google.com/group/catch-exception-discuss). Issues can be reported on the [Issues page](http://code.google.com/p/catch-exception/issues/list) of this project.

Please give me feedback of any kind. It is highly appreciated.