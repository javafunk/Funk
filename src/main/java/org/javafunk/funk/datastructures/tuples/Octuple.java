/*
 * Copyright (C) 2011-Present Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk.datastructures.tuples;

import org.javafunk.funk.behaviours.ordinals.*;

import static org.javafunk.funk.Literals.iterableBuilderFrom;
import static org.javafunk.funk.Literals.iterableWith;

public class Octuple<R, S, T, U, V, W, X, Y>
        extends Septuple<R, S, T, U, V, W, X>
        implements Eighth<Y> {
    private final Y eighth;

    public Octuple(R first, S second, T third, U fourth, V fifth, W sixth, X seventh, Y eighth) {
        super(first, second, third, fourth, fifth, sixth, seventh);
        this.eighth = eighth;
    }

    @Override public Y eighth() {
        return eighth;
    }

    @Override public Iterable<Object> values() {
        return iterableBuilderFrom(super.values()).with(eighth()).build();
    }
}
