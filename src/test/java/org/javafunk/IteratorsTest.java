package org.javafunk;

import org.javafunk.collections.Bag;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.javafunk.Iterators.*;
import static org.javafunk.Literals.*;

public class IteratorsTest {
    @Test
    public void shouldWrapTheSuppliedIteratorInAnIterable() {
        // Given
        Iterator<String> iterator = listWith("a", "b", "c").iterator();

        // When
        Iterable<String> iterable = toIterable(iterator);

        // Then
        assertThat(iterable.iterator(), is(iterator));
    }

    @Test
    public void shouldReturnAnIteratorContainingNoElements() {
        // When
        Iterator<Integer> iterator = emptyIterator();

        // Then
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void shouldConvertTheSuppliedIteratorToAList() {
        // Given
        List<String> expectedList = listWith("a", "b", "c");
        Iterator<String> iterator = expectedList.iterator();

        // When
        List<String> actualList = toList(iterator);

        // Then
        assertThat(actualList, is(expectedList));
    }

    @Test
    public void shouldConvertTheSuppliedIteratorToASet() {
        // Given
        Iterator<String> iterator = listWith("a", "a", "c", "d").iterator();
        Set<String> expectedSet = setWith("a", "c", "d");

        // When
        Set<String> actualSet = toSet(iterator);

        // Then
        assertThat(actualSet, is(expectedSet));
    }

    @Test
    public void shouldConvertTheSuppliedIteratorToABag() {
        // Given
        List<String> elements = listWith("a", "a", "c", "d");
        Bag<String> expectedBag = bagWith("a", "a", "c", "d");

        // When
        Bag<String> actualBag = toBag(elements.iterator());

        // Then
        assertThat(actualBag, is(expectedBag));
    }
}