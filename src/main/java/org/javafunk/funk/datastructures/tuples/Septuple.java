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

public class Septuple<R, S, T, U, V, W, X>
        extends Sextuple<R, S, T, U, V, W>
        implements Seventh<X> {
    private final X seventh;

    public Septuple(R first, S second, T third, U fourth, V fifth, W sixth, X seventh) {
        super(first, second, third, fourth, fifth, sixth);
        this.seventh = seventh;
    }

    @Override public X seventh() {
        return seventh;
    }

    @Override public Iterable<Object> values() {
        return iterableBuilderFrom(super.values()).with(seventh()).build();
    }
}
