/*
 * Copyright (C) 2011-Present Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Literals.iterableWith;

public class EagerlyProductTest {
    @Test
    public void shouldCalculateTheProductOfTheSuppliedIntegers() throws Exception {
        // Given
        Iterable<Integer> input = iterableWith(1, 2, 3, 4, 5);

        // When
        Integer result = Eagerly.product(input);

        // Then
        assertThat(result, is(120));
    }

    @Test
    public void shouldCalculateTheProductOfTheSuppliedLongs() throws Exception {
        // Given
        Iterable<Long> input = iterableWith(1L, 2L, 3L, 4L, 5L);

        // When
        Long result = Eagerly.product(input);

        // Then
        assertThat(result, is(120L));
    }

    @Test
    public void shouldCalculateTheProductOfTheSuppliedBigIntegers() throws Exception {
        // Given
        Iterable<BigInteger> input = iterableWith(new BigInteger("123"), new BigInteger("456"), new BigInteger("789"));

        // When
        BigInteger result = Eagerly.product(input);

        // Then
        assertThat(result, is(new BigInteger("44253432")));
    }

    @Test
    public void shouldCalculateTheProductOfTheSuppliedFloats() throws Exception {
        // Given
        Iterable<Float> input = iterableWith(1.1F, 1.2F, 1.3F, 1.4F, 1.5F);

        // When
        Float result = Eagerly.product(input);

        // Then
        assertThat(result.doubleValue(), is(closeTo(3.6036F, 0.0001)));
    }
    
    @Test
    public void shouldCalculateTheProductOfTheSuppliedDoubles() throws Exception {
        // Given
        Iterable<Double> input = iterableWith(1.1D, 1.2D, 1.3D, 1.4D, 1.5D);

        // When
        Double result = Eagerly.product(input);

        // Then
        assertThat(result, is(closeTo(3.6036F, 0.0001)));
    }

    @Test
    public void shouldCalculateTheProductOfTheSuppliedBigDecimals() throws Exception {
        // Given
        Iterable<BigDecimal> input = iterableWith(new BigDecimal("1.23"), new BigDecimal("4.56"), new BigDecimal("7.89"));

        // When
        BigDecimal result = Eagerly.product(input);

        // Then
        assertThat(result, is(new BigDecimal("44.253432")));
    }
}
