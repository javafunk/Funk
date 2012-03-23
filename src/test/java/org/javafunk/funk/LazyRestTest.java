package org.javafunk.funk;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.javafunk.funk.Iterables.materialize;
import static org.javafunk.funk.Literals.listWith;
import static org.junit.Assert.assertThat;

public class LazyRestTest {
    @Test
    public void shouldReturnTheRestOfTheIterable(){
        //given
        Iterable<String> iterable = listWith("a", "b", "c", "d");
        Iterable<String> expectedRest = listWith("b", "c", "d");

        //when
        Iterable<String> rest = Lazy.rest(iterable);
        
        //then
        assertThat(materialize(rest), equalTo(expectedRest));
    }

    @Test
    public void shouldReturnEmptyIterableForAnIterableWithOneElement(){
        //given
        Iterable<String> iterable = listWith("a");
        Iterable<String> expectedRest = Iterables.empty();

        //when
        Iterable<String> rest = Lazy.rest(iterable);

        //then
        assertThat(materialize(rest), equalTo(expectedRest));
    }

    @Test
    public void shouldReturnEmptyIterableForAnEmptyIterable(){
        //given
        Iterable<String> iterable = Iterables.empty();
        Iterable<String> expectedRest = Iterables.empty();

        //when
        Iterable<String> rest = Lazy.rest(iterable);

        //then
        assertThat(materialize(rest), equalTo(expectedRest));
    }
}
