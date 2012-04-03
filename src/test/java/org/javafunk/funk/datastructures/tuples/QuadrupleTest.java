/*
 * Copyright (C) 2011 Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk.datastructures.tuples;

import org.javafunk.funk.Literals;
import org.javafunk.funk.functors.ordinals.First;
import org.javafunk.funk.functors.ordinals.Fourth;
import org.javafunk.funk.functors.ordinals.Second;
import org.javafunk.funk.functors.ordinals.Third;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.funk.Iterables.materialize;
import static org.javafunk.funk.Literals.tuple;

public class QuadrupleTest {
    @Test
    public void shouldReturnTheFirstObject() {
        // Given
        First<Integer> quadruple = tuple(5, "Five", true, 3.6);

        // When
        Integer first = quadruple.first();

        //Then
        assertThat(first, is(5));
    }

    @Test
    public void shouldReturnTheSecondObject() {
        // Given
        Second<String> quadruple = tuple(5, "Five", true, 3.6);

        // When
        String second = quadruple.second();

        //Then
        assertThat(second, is("Five"));
    }

    @Test
    public void shouldReturnTheThirdObject() {
        // Given
        Third<Boolean> quadruple = tuple(5, "Five", true, 3.6);

        // When
        Boolean third = quadruple.third();

        //Then
        assertThat(third, is(true));
    }

    @Test
    public void shouldReturnTheFourthObject() {
        // Given
        Fourth<Double> quadruple = tuple(5, "Five", true, 3.6);

        // When
        Double fourth = quadruple.fourth();

        //Then
        assertThat(fourth, is(3.6));
    }

    @Test
    public void shouldUseTheStringValueOfTheFirstSecondThirdAndFourthInToString() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple = tuple(5, "Five", true, 3.6);

        // When
        String stringRepresentation = quadruple.toString();

        // Then
        assertThat(stringRepresentation, is("(5, Five, true, 3.6)"));
    }

    @Test
    public void shouldBeEqualIfHasSameFirstSecondThirdAndFourth() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple1 = tuple(5, "Five", true, 3.6);
        Quadruple<Integer, String, Boolean, Double> quadruple2 = tuple(5, "Five", true, 3.6);

        // When
        Boolean isEqual = quadruple1.equals(quadruple2);

        // Then
        assertThat(isEqual, is(true));
    }

    @Test
    public void shouldNotBeEqualIfHasDifferentFirst() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple1 = tuple(5, "Five", true, 3.6);
        Quadruple<Integer, String, Boolean, Double> quadruple2 = tuple(10, "Five", true, 3.6);

        // When
        Boolean isEqual = quadruple1.equals(quadruple2);

        // Then
        assertThat(isEqual, is(false));
    }

    @Test
    public void shouldNotBeEqualIfHasDifferentSecond() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple1 = tuple(5, "Five", true, 3.6);
        Quadruple<Integer, String, Boolean, Double> quadruple2 = tuple(5, "Ten", true, 3.6);

        // When
        Boolean isEqual = quadruple1.equals(quadruple2);

        // Then
        assertThat(isEqual, is(false));
    }

    @Test
    public void shouldNotBeEqualIfHasDifferentThird() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple1 = tuple(5, "Five", true, 3.6);
        Quadruple<Integer, String, Boolean, Double> quadruple2 = tuple(5, "Five", false, 3.6);

        // When
        Boolean isEqual = quadruple1.equals(quadruple2);

        // Then
        assertThat(isEqual, is(false));
    }

    @Test
    public void shouldNotBeEqualIfHasDifferentFourth() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple1 = tuple(5, "Five", true, 3.6);
        Quadruple<Integer, String, Boolean, Double> quadruple2 = tuple(5, "Five", true, 4.2);

        // When
        Boolean isEqual = quadruple1.equals(quadruple2);

        // Then
        assertThat(isEqual, is(false));
    }

    @Test
    public void shouldBeIterable() {
        // Given
        Quadruple<Integer, String, Boolean, Double> quadruple1 = tuple(5, "Five", true, 3.6);
        List<Object> expected = Literals.<Object>listWith(5, "Five", true, 3.6);

        // When
        Boolean isEqual = materialize(quadruple1).equals(expected);

        // Then
        assertThat(isEqual, is(true));
    }
}
