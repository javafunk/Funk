/*
 * Copyright (C) 2011-Present Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk.monads;

import org.javafunk.funk.functors.Mapper;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;
import static org.javafunk.funk.testclasses.Matchers.hasAllElementsEqualTo;

public class EitherTest {
    @Test
    public void shouldBeLeftForLeft() {
        // Given
        Either<String, Integer> either = Either.left("LEFT");

        // When
        boolean left = either.isLeft();

        // Then
        assertThat(left, is(true));
    }

    @Test
    public void shouldNotBeRightForLeft() {
        // Given
        Either<String, Integer> either = Either.left("LEFT");

        // When
        boolean right = either.isRight();

        // Then
        assertThat(right, is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionOnGetRightForLeft() {
        // Given
        Either<String, Integer> either = Either.left("LEFT");

        // When
        either.getRight();

        // Then a NoSuchElementException is thrown
    }

    @Test
    public void shouldReturnLeftValueForGetLeft() {
        // Given
        Either<String, Integer> either = Either.right(1);

        // When
        Integer right = either.getRight();

        // Then
        assertThat(right, is(1));
    }

    @Test
    public void shouldNotBeLeftForRight() {
        // Given
        Either<String, Integer> either = Either.right(1);

        // When
        boolean left = either.isLeft();

        // Then
        assertThat(left, is(false));
    }

    @Test
    public void shouldBeRightForRight() {
        // Given
        Either<String, Integer> either = Either.right(1);

        // When
        boolean right = either.isRight();

        // Then
        assertThat(right, is(true));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionOnGetLeftForRight() {
        // Given
        Either<String, Integer> either = Either.right(1);

        // When
        either.getLeft();

        // Then a NoSuchElementException is thrown
    }

    @Test
    public void shouldReturnRightValueForGetRight() {
        // Given
        Either<String, Integer> either = Either.right(1);

        // When
        Integer right = either.getRight();

        // Then
        assertThat(right, is(1));
    }

    @Test
    public void shouldMapRightValueUsingMapperIfRight() throws Exception {
        // Given
        Either<Exception, String> either = Either.right("Hello");
        Either<Exception, Integer> expected = Either.right(5);

        // When
        Either<Exception, Integer> actual = either.map(new Mapper<String, Integer>() {
            @Override public Integer map(String string) {
                return string.length();
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfMappingUsingNullMapperIfRight() throws Exception {
        // Given
        Either<Exception, String> either = Either.right("Hello");

        // When
        either.map((Mapper<String, Integer>) null);

        // Then a NullPointerException is thrown
    }

    @Test
    public void shouldPropagateLeftValueOfCorrectRightTypeForMapWithMapper() throws Exception {
        // Given
        RuntimeException exception = new RuntimeException("an error");
        Either<RuntimeException, String> either = Either.left(exception);
        Either<RuntimeException, Integer> expected = Either.left(exception);

        // When
        Either<RuntimeException, Integer> actual = either.map(new Mapper<String, Integer>() {
            @Override public Integer map(String string) {
                return string.length();
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfMappingUsingNullMapperIfLeft() throws Exception {
        // Given
        Either<String, String> either = Either.left("Hello");

        // When
        either.map((Mapper<String, Integer>) null);

        // Then a NullPointerException is thrown
    }

    @Test
    public void shouldMapRightValueUsingUnaryFunctionIfRight() throws Exception {
        // Given
        Either<Exception, String> either = Either.right("Hello");
        Either<Exception, Integer> expected = Either.right(5);

        // When
        Either<Exception, Integer> actual = either.map(new UnaryFunction<String, Integer>() {
            @Override public Integer call(String string) {
                return string.length();
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfMappingUsingNullUnaryFunctionIfRight() throws Exception {
        // Given
        Either<Exception, String> either = Either.right("Hello");

        // When
        either.map((UnaryFunction<String, Integer>) null);

        // Then a NullPointerException is thrown
    }

    @Test
    public void shouldPropagateLeftValueOfCorrectRightTypeForMapWithUnaryFunction() throws Exception {
        // Given
        RuntimeException exception = new RuntimeException("an error");
        Either<RuntimeException, String> either = Either.left(exception);
        Either<RuntimeException, Integer> expected = Either.left(exception);

        // When
        Either<RuntimeException, Integer> actual = either.map(new UnaryFunction<String, Integer>() {
            @Override public Integer call(String string) {
                return string.length();
            }
        });

        // Then
        assertThat(actual, is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIfMappingUsingNullUnaryFunctionIfLeft() throws Exception {
        // Given
        Either<String, String> either = Either.left("Hello");

        // When
        either.map((UnaryFunction<String, Integer>) null);

        // Then a NullPointerException is thrown
    }

    @Test
    public void shouldBeEqualIfBothLeftOverSameValue() throws Exception {
        // Given
        Either<String, Integer> first = Either.left("Hello");
        Either<String, Long> second = Either.left("Hello");

        // When
        boolean firstEqualsSecond = first.equals(second);
        boolean secondEqualsFirst = second.equals(first);

        // Then
        assertThat(iterableWith(firstEqualsSecond, secondEqualsFirst),
                hasAllElementsEqualTo(true));
    }

    @Test
    public void shouldNotBeEqualIfBothLeftOverDifferentValues() throws Exception {
        // Given
        Either<String, Integer> first = Either.left("Hello");
        Either<String, Long> second = Either.left("Goodbye");

        // When
        boolean firstEqualsSecond = first.equals(second);
        boolean secondEqualsFirst = second.equals(first);

        // Then
        assertThat(iterableWith(firstEqualsSecond, secondEqualsFirst),
                hasAllElementsEqualTo(false));
    }

    @Test
    public void shouldNotBeEqualIfOneLeftAndOneRightOverDifferentValues() throws Exception {
        // Given
        Either<String, Integer> first = Either.left("Hello");
        Either<Long, String> second = Either.right("Goodbye");

        // When
        boolean firstEqualsSecond = first.equals(second);
        boolean secondEqualsFirst = second.equals(first);

        // Then
        assertThat(iterableWith(firstEqualsSecond, secondEqualsFirst),
                hasAllElementsEqualTo(false));
    }

    @Test
    public void shouldNotBeEqualIfOneLeftAndOneRightOverSameValue() throws Exception {
        // Given
        Either<String, Integer> first = Either.left("Hello");
        Either<Long, String> second = Either.right("Hello");

        // When
        boolean firstEqualsSecond = first.equals(second);
        boolean secondEqualsFirst = second.equals(first);

        // Then
        assertThat(iterableWith(firstEqualsSecond, secondEqualsFirst),
                hasAllElementsEqualTo(false));
    }

    @Test
    public void shouldBeEqualIfBothRightOverSameValue() throws Exception {
        // Given
        Either<Integer, String> first = Either.right("Hello");
        Either<Long, String> second = Either.right("Hello");

        // When
        boolean firstEqualsSecond = first.equals(second);
        boolean secondEqualsFirst = second.equals(first);

        // Then
        assertThat(iterableWith(firstEqualsSecond, secondEqualsFirst),
                hasAllElementsEqualTo(true));
    }

    @Test
    public void shouldNotBeEqualIfBothRightOverDifferentValues() throws Exception {
        // Given
        Either<Integer, String> first = Either.right("Hello");
        Either<Long, String> second = Either.right("Goodbye");

        // When
        boolean firstEqualsSecond = first.equals(second);
        boolean secondEqualsFirst = second.equals(first);

        // Then
        assertThat(iterableWith(firstEqualsSecond, secondEqualsFirst),
                hasAllElementsEqualTo(false));
    }
}
