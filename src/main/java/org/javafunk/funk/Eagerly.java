/*
 * Copyright (C) 2011-Present Funk committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.javafunk.funk;

import org.javafunk.funk.datastructures.IntegerRange;
import org.javafunk.funk.datastructures.tuples.Pair;
import org.javafunk.funk.functors.*;
import org.javafunk.funk.functors.functions.BinaryFunction;
import org.javafunk.funk.functors.functions.UnaryFunction;
import org.javafunk.funk.functors.predicates.BinaryPredicate;
import org.javafunk.funk.functors.predicates.UnaryPredicate;
import org.javafunk.funk.functors.procedures.UnaryProcedure;
import org.javafunk.funk.monads.Option;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.javafunk.funk.Accumulators.*;
import static org.javafunk.funk.Iterables.materialize;
import static org.javafunk.funk.Iterators.asIterable;
import static org.javafunk.funk.Literals.tuple;
import static org.javafunk.funk.functors.adapters.ActionUnaryProcedureAdapter.actionUnaryProcedure;
import static org.javafunk.funk.functors.adapters.EquivalenceBinaryPredicateAdapter.equivalenceBinaryPredicate;
import static org.javafunk.funk.functors.adapters.IndexerUnaryFunctionAdapter.indexerUnaryFunction;
import static org.javafunk.funk.functors.adapters.MapperUnaryFunctionAdapter.mapperUnaryFunction;
import static org.javafunk.funk.functors.adapters.ReducerBinaryFunctionAdapter.reducerBinaryFunction;

/**
 * @since 1.0
 */
public class Eagerly {
    private Eagerly() {}

    public static <S, T> T reduce(
            Iterable<? extends S> iterable,
            T initialValue,
            BinaryFunction<T, ? super S, T> function) {
        T accumulator = initialValue;
        for (S element : iterable) {
            accumulator = function.call(accumulator, element);
        }
        return accumulator;
    }

    public static <S, T> T reduce(
            Iterable<? extends S> iterable,
            T initialValue,
            Reducer<? super S, T> reducer) {
        return reduce(iterable, initialValue, reducerBinaryFunction(reducer));
    }

    public static <T> T reduce(
            Iterable<T> iterable,
            BinaryFunction<T, ? super T, T> function) {
        Iterator<T> iterator = iterable.iterator();
        T firstElement = iterator.next();
        Iterable<T> restOfElements = asIterable(iterator);
        return reduce(restOfElements, firstElement, function);
    }

    public static <T> T reduce(Iterable<T> iterable, Reducer<T, T> reducer) {
        return reduce(iterable, reducerBinaryFunction(reducer));
    }

    public static Integer sumIntegers(Iterable<Integer> iterable) {
        return reduce(iterable, integerAdditionAccumulator());
    }

    public static Long sumLongs(Iterable<Long> iterable) {
        return reduce(iterable, longAdditionAccumulator());
    }

    public static BigInteger sumBigIntegers(Iterable<BigInteger> iterable) {
        return reduce(iterable, bigIntegerAdditionAccumulator());
    }

    public static Double sumDoubles(Iterable<Double> iterable) {
        return reduce(iterable, doubleAdditionAccumulator());
    }

    public static Float sumFloats(Iterable<Float> iterable) {
        return reduce(iterable, floatAdditionAccumulator());
    }

    public static BigDecimal sumBigDecimals(Iterable<BigDecimal> iterable) {
        return reduce(iterable, bigDecimalAdditionAccumulator());
    }

    public static Integer multiplyIntegers(Iterable<Integer> iterable) {
        return reduce(iterable, integerMultiplicationAccumulator());
    }

    public static Long multiplyLongs(Iterable<Long> iterable) {
        return reduce(iterable, longMultiplicationAccumulator());
    }

    public static BigInteger multiplyBigIntegers(Iterable<BigInteger> iterable) {
        return reduce(iterable, bigIntegerMultiplicationAccumulator());
    }

    public static Float multiplyFloats(Iterable<Float> iterable) {
        return reduce(iterable, floatMultiplicationAccumulator());
    }

    public static Double multiplyDoubles(Iterable<Double> iterable) {
        return reduce(iterable, doubleMultiplicationAccumulator());
    }

    public static BigDecimal multiplyBigDecimals(Iterable<BigDecimal> iterable) {
        return reduce(iterable, bigDecimalMultiplicationAccumulator());
    }

    public static <T> Boolean any(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        for (T item : iterable) {
            if (predicate.evaluate(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> Boolean all(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        for (T item : iterable) {
            if (!predicate.evaluate(item)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Boolean none(
            Iterable<T> items,
            UnaryPredicate<? super T> predicate) {
        return !any(items, predicate);
    }

    public static <T> T max(Iterable<T> iterable, final Comparator<? super T> comparator) {
        return reduce(iterable, new Reducer<T, T>() {
            public T accumulate(T currentMax, T element) {
                return (element != null && comparator.compare(element, currentMax) > 0) ?
                        element :
                        currentMax;
            }
        });
    }

    public static <T extends Comparable<T>> T max(Iterable<T> iterable) {
        return reduce(iterable, new Reducer<T, T>() {
            public T accumulate(T currentMax, T element) {
                return (element != null && element.compareTo(currentMax) > 0) ?
                        element :
                        currentMax;
            }
        });
    }

    public static <T> T min(Iterable<T> iterable, final Comparator<? super T> comparator) {
        return reduce(iterable, new Reducer<T, T>() {
            public T accumulate(T currentMin, T element) {
                return (element != null && comparator.compare(element, currentMin) < 0) ?
                        element :
                        currentMin;
            }
        });
    }

    public static <T extends Comparable<T>> T min(Iterable<T> iterable) {
        return reduce(iterable, new Reducer<T, T>() {
            public T accumulate(T currentMin, T element) {
                return (element != null && element.compareTo(currentMin) < 0) ?
                        element :
                        currentMin;
            }
        });
    }

    /**
     * Maps an {@code Iterable} of elements of type {@code S} into a {@code Collection}
     * of elements of type {@code T} using the supplied {@code UnaryFunction}. The
     * {@code UnaryFunction} will be provided with each element in the input
     * {@code Iterable} and the value returned from the {@code UnaryFunction} will be
     * used in place of the input element in the returned {@code Collection}. For a
     * more mathematical description of the map higher order function, see the
     * <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">
     * map article on Wikipedia</a>.
     *
     * <p>Since a {@code Collection} instance is returned, the mapping is performed
     * eagerly, i.e., the {@code UnaryFunction} is applied to each element in the input
     * {@code Iterable} immediately.</p>
     *
     * <p>{@code map} does not discriminate against {@code null} values in the input
     * {@code Iterable}, they are passed to the function in the same way as any other
     * value. Similarly, any {@code null} values returned are retained in the output
     * {@code Collection}. Thus, the input and output collections will always be of
     * the same size.</p>
     *
     * <h4>Example Usage:</h4>
     *
     * Consider a collection of {@code Person} objects where a {@code Person} is defined
     * by the following class:
     * <blockquote>
     * <pre>
     *   public class Person {
     *       private Name name;
     *
     *       public Person(Name name) {
     *           this.name = name;
     *       }
     *
     *       public Name getName() {
     *           return name;
     *       };
     *
     *       ...
     *   }
     * </pre>
     * </blockquote>
     * and a {@code Name} is defined by the following class:
     * <blockquote>
     * <pre>
     *   public class Name {
     *       private String firstName;
     *       private String lastName;
     *
     *       public Name(String firstName, String lastName) {
     *           this.firstName = firstName;
     *           this.lastName = lastName;
     *       }
     *
     *       public String getFirstName() {
     *           return firstName;
     *       }
     *
     *       public String getLastName() {
     *           return lastName;
     *       }
     *
     *       ...
     *   }
     * </pre>
     * </blockquote>
     * Say we have an in memory database of all employees at a company:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Person&gt; people = Literals.listWith(
     *           new Person(new Name("Julio", "Tilman")),
     *           new Person(new Name("Roslyn", "Snipe")),
     *           new Person(new Name("Tameka", "Brickhouse")));
     * </pre>
     * </blockquote>
     * and we need to generate a report of all names, last name first, first name second,
     * hyphen separated. In order to do this we need to convert, or <em>map</em>, each
     * {@code Person} instance to the required {@code String}. This can be achieved
     * as follow:
     * <blockquote>
     * <pre>
     *   Collection&lt;String&gt; names = Eagerly.map(people, new UnaryFunction&lt;Person, String&gt;() {
     *       &#64;Override public String call(Person person) {
     *           return person.getLastName() + "-" + person.getFirstName;
     *       }
     *   });
     * </pre>
     * </blockquote>
     * The resulting collection is equivalent to the following:
     * <blockquote>
     * <pre>
     *   Collection&lt;String&gt; names = Literals.collectionWith(
     *           "Tilman-Julio",
     *           "Snipe-Roslyn",
     *           "Brickhouse-Tameka");
     * </pre>
     * </blockquote>
     *
     * @param iterable The {@code Iterable} of elements to be mapped.
     * @param function A {@code UnaryFunction} which, given an element from the input iterable,
     *                 returns that element mapped to a new value potentially of a different type.
     * @param <S>      The type of the input elements, i.e., the elements to map.
     * @param <T>      The type of the output elements, i.e., the mapped elements.
     * @return A {@code Collection} containing each instance of {@code S} from the input
     *         {@code Iterable} mapped to an instance of {@code T}.
     */
    public static <S, T> Collection<T> map(
            Iterable<S> iterable,
            UnaryFunction<? super S, T> function) {
        return materialize(Lazily.map(iterable, function));
    }

    /**
     * Maps an {@code Iterable} of elements of type {@code S} into a {@code Collection}
     * of elements of type {@code T} using the supplied {@code Mapper}. The
     * {@code Mapper} will be provided with each element in the input {@code Iterable}
     * and the value returned from the {@code Mapper} will be used in place of the
     * input element in the returned {@code Collection}. For a more mathematical
     * description of the map higher order function, see the
     * <a href="http://en.wikipedia.org/wiki/Map_(higher-order_function)">
     * map article on Wikipedia</a>.
     *
     * <p>This override of
     * {@link #map(Iterable, org.javafunk.funk.functors.functions.UnaryFunction)} is
     * provided to allow a {@code Mapper} to be used in place of a {@code UnaryFunction}
     * to enhance readability and better express intent. The contract of the function
     * is identical to that of the {@code UnaryFunction} version of {@code map}.</p>
     *
     * <p>For example usage and further documentation, see
     * {@link #map(Iterable, org.javafunk.funk.functors.functions.UnaryFunction)}.</p>
     *
     * @param iterable The {@code Iterable} of elements to be mapped.
     * @param mapper   A {@code Mapper} which, given an element from the input iterable,
     *                 returns that element mapped to a new value potentially of a different type.
     * @param <S>      The type of the input elements, i.e., the elements to map.
     * @param <T>      The type of the output elements, i.e., the mapped elements.
     * @return A {@code Collection} containing each instance of {@code S} from the input
     *         {@code Iterable} mapped to an instance of {@code T}.
     * @see #map(Iterable, org.javafunk.funk.functors.functions.UnaryFunction)
     */
    public static <S, T> Collection<T> map(Iterable<S> iterable, Mapper<? super S, T> mapper) {
        return map(iterable, mapperUnaryFunction(mapper));
    }

    public static <S, T> Collection<Pair<S, T>> zip(Iterable<S> iterable1, Iterable<T> iterable2) {
        return materialize(Lazily.zip(iterable1, iterable2));
    }

    public static <T> Collection<Pair<Integer, T>> enumerate(Iterable<T> iterable) {
        return materialize(Lazily.enumerate(iterable));
    }

    public static <T> Collection<Boolean> equate(
            Iterable<T> first,
            Iterable<T> second,
            BinaryPredicate<? super T, ? super T> predicate) {
        return materialize(Lazily.equate(first, second, predicate));
    }

    public static <T> Collection<Boolean> equate(
            Iterable<T> first,
            Iterable<T> second,
            Equivalence<? super T> equivalence) {
        return equate(first, second, equivalenceBinaryPredicate(equivalence));
    }

    public static <S, T> Collection<Pair<T, S>> index(
            Iterable<S> iterable,
            UnaryFunction<? super S, T> function) {
        return materialize(Lazily.index(iterable, function));
    }

    public static <S, T> Collection<Pair<T, S>> index(
            Iterable<S> iterable,
            Indexer<? super S, T> indexer) {
        return index(iterable, indexerUnaryFunction(indexer));
    }

    public static <S, T> Map<T, Collection<S>> group(
            Iterable<S> iterable,
            UnaryFunction<? super S, T> indexer) {
        Map<T, Collection<S>> groupedElements = new HashMap<T, Collection<S>>();
        for (S element : iterable) {
            T index = indexer.call(element);
            if (!groupedElements.containsKey(index)) {
                groupedElements.put(index, new ArrayList<S>());
            }
            groupedElements.get(index).add(element);
        }
        return groupedElements;
    }

    public static <S, T> Map<T, Collection<S>> group(
            Iterable<S> iterable,
            Indexer<? super S, T> indexer) {
        return group(iterable, indexerUnaryFunction(indexer));
    }

    public static <T> void each(
            Iterable<T> targets,
            UnaryProcedure<? super T> procedure) {
        materialize(Lazily.each(targets, procedure));
    }

    public static <T> void each(
            Iterable<T> targets,
            Action<? super T> action) {
        each(targets, actionUnaryProcedure(action));
    }

    /**
     * Filters those elements from the input {@code Iterable} of type {@code T}
     * that satisfy the supplied {@code Predicate} into a {@code Collection}
     * of elements of type {@code T} . The {@code Predicate} will be provided
     * with each element in the input {@code Iterable} and the element will be
     * retained in the output {@code Collection} if and only if the
     * {@code Predicate} returns {@code true}. If it returns {@code false},
     * the element will be discarded.
     *
     * <p>For a more complete description of the filter higher order function,
     * see the <a href="http://en.wikipedia.org/wiki/Filter_(higher-order_function)">
     * filter article on Wikipedia</a>.</p>
     *
     * <p>Since a {@code Collection} instance is returned, the filtering is performed
     * eagerly, i.e., the {@code UnaryPredicate} is applied to each element in the input
     * {@code Iterable} immediately.</p>
     *
     * <p>{@code filter} does not discriminate against {@code null} values in the input
     * {@code Iterable}, they are passed to the {@code Predicate} in the same way as
     * any other value. Similarly, if the {@code Predicate} returns {@code true} when
     * called with a {@code null} value, the {@code null} value will be retained in
     * the output {@code Collection}.</p>
     *
     * <h4>Example Usage</h4>
     *
     * Consider a collection of {@code Pet} objects where a {@code Pet} is defined
     * by the following interface:
     * <blockquote>
     * <pre>
     *   public interface Pet {
     *       String getName();
     *   }
     * </pre>
     * </blockquote>
     * Now, consider {@code Pet} has two implementations, {@code Cat} and
     * {@code Dog}, defined by the following classes:
     * <blockquote>
     * <pre>
     *   public class Dog implements Pet {
     *       private String name;
     *
     *       public Dog(String name) {
     *           this.name = name;
     *       }
     *
     *       &#64;Override
     *       public String getName() {
     *           return String.format("Bark, bark, %s, bark", name);
     *       }
     *   }
     *
     *   public class Cat implements Pet {
     *       private String name;
     *
     *       public Cat(String name) {
     *           this.name = name;
     *       }
     *
     *       &#64;Override
     *       public String getName() {
     *           return String.format("%s, miaow", name);
     *       }
     *   }
     * </pre>
     * </blockquote>
     * Say we have an in memory database of all pets in a neighbourhood:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Pet&gt; pets = Literals.listWith(
     *           new Dog("Fido"),
     *           new Dog("Bones"),
     *           new Cat("Fluff"),
     *           new Dog("Graham"),
     *           new Cat("Ginger"));
     * </pre>
     * </blockquote>
     * It's vaccination season for the dogs in the neighbourhood so we need to
     * get hold of a list of all of them. We can do this using {@code filter}:
     * <blockquote>
     * <pre>
     *   Collection&lt;Pet&gt; names = Eagerly.filter(pets, new Predicate&lt;Pet&gt;() {
     *       &#64;Override public boolean evaluate(Pet pet) {
     *           return pet instanceof Dog;
     *       }
     *   });
     * </pre>
     * </blockquote>
     * Note, we used an anonymous {@code Predicate} instance. The {@code Predicate} interface
     * is equivalent to the {@code UnaryPredicate} interface and exists to simplify the
     * eighty percent case with predicates.
     *
     * <p>The resulting collection is equivalent to the following:</p>
     * <blockquote>
     * <pre>
     *   Collection&lt;Pet&gt; names = Literals.collectionWith(
     *           new Dog("Fido"),
     *           new Dog("Bones"),
     *           new Dog("Graham"));
     * </pre>
     * </blockquote>
     *
     * @param iterable  The {@code Iterable} of elements to be filtered.
     * @param predicate A {@code UnaryPredicate} which, given an element from the input iterable,
     *                  returns {@code true} if that element should be retained and {@code false}
     *                  if it should be discarded.
     * @param <T>       The type of the elements to be filtered.
     * @return A {@code Collection} containing those elements of type {@code T} from the input
     *         {@code Iterable} that evaluate to {@code true} when passed to the supplied
     *         {@code UnaryPredicate}.
     */
    public static <T> Collection<T> filter(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return materialize(Lazily.filter(iterable, predicate));
    }

    public static <T> Collection<T> reject(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return materialize(Lazily.reject(iterable, predicate));
    }

    /**
     * Returns an {@code Option} over the first element in the supplied {@code Iterable}.
     * If the {@code Iterable} is empty, {@code None} is returned, otherwise, a
     * {@code Some} is returned over the first value found.
     *
     * <p>This method has a return type of {@code Option} rather than returning the
     * first value directly since, in the case of an empty {@code Iterable}, an
     * exception would have to be thrown using that approach. Instead, the
     * {@code Option} can be queried for whether it contains a value or not,
     * avoiding any exception handling.</p>
     *
     * <p>Since an {@code Option} instance is returned, the element retrieval is performed
     * eagerly, i.e., an attempt is made to retrieve the first element from the underlying
     * {@code Iterable} immediately.</p>
     *
     * <h4>Example Usage:</h4>
     *
     * Given an {@code Iterable} of {@code Integer} instances:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterableWith(5, 4, 3, 2, 1);
     * </pre>
     * </blockquote>
     * The first element in the {@code Iterable} can be obtained as follows:
     * <blockquote>
     * <pre>
     *   Option&lt;Integer&gt; valueOption = first(values);
     *   Integer value = valueOption.get(); // => 5
     * </pre>
     * </blockquote>
     * Similarly, we can handle the empty {@code Iterable} case gracefully:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterable();
     *   Option&lt;Integer&gt; valueOption = first(values);
     *   Integer value = valueOption.getOrElse(10); // => 10
     * </pre>
     * </blockquote>
     *
     * @param iterable The {@code Iterable} from which the first element is required.
     * @param <T>      The type of the elements in the supplied {@code Iterable}.
     * @return An {@code Option} instance representing the first element in the supplied
     *         {@code Iterable}.
     */
    public static <T> Option<T> first(Iterable<? extends T> iterable) {
        Iterator<? extends T> iterator = iterable.iterator();
        if (iterator.hasNext()) {
            return Option.some(iterator.next());
        } else {
            return Option.none();
        }
    }

    /**
     * Returns an {@code Option} over the first element in the supplied {@code Iterable}
     * that satisfies the supplied {@code UnaryPredicate}. If the {@code Iterable} is
     * empty, {@code None} is returned, otherwise, a {@code Some} is returned over
     * the first matching value found.
     *
     * <p>This method has a return type of {@code Option} rather than returning the
     * first matching value directly since, in the case of an empty {@code Iterable},
     * an exception would have to be thrown using that approach. Instead, the
     * {@code Option} can be queried for whether it contains a value or not,
     * avoiding an exception handling.</p>
     *
     * <p>Since an {@code Option} instance is returned, the element retrieval is performed
     * eagerly, i.e., an attempt is made to retrieve the first matching element from the
     * underlying {@code Iterable} immediately.</p>
     *
     * <h4>Example Usage:</h4>
     *
     * Given an {@code Iterable} of {@code Integer} instances:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterableWith(5, 4, 3, 2, 1);
     * </pre>
     * </blockquote>
     * The first even element in the {@code Iterable} can be obtained as follows:
     * <blockquote>
     * <pre>
     *   Option&lt;Integer&gt; valueOption = first(values, new Predicate&lt;Integer&gt;(){
     *       &#64;Override public boolean evaluate(Integer integer) {
     *           return integer % 2 == 0;
     *       }
     *   });
     *   Integer value = valueOption.get(); // => 4
     * </pre>
     * </blockquote>
     * Note, we used an anonymous {@code Predicate} instance. The {@code Predicate} interface
     * is equivalent to the {@code UnaryPredicate} interface and exists to simplify the
     * eighty percent case with predicates.
     *
     * <p>Thanks to the {@code Option} returned, we can handle the empty {@code Iterable}
     * case gracefully:</p>
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterable();
     *   Option&lt;Integer&gt; valueOption = first(values, new Predicate&lt;Integer&gt;(){
     *       &#64;Override public boolean evaluate(Integer integer) {
     *           return integer % 2 == 0;
     *       }
     *   });
     *   Integer value = valueOption.getOrElse(10); // => 10
     * </pre>
     * </blockquote>
     * Similarly, if no elements match the supplied {@code UnaryPredicate}, we are returned
     * a {@code None}:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterable(9, 7, 5, 3, 1);
     *   Option&lt;Integer&gt; valueOption = first(values, new Predicate&lt;Integer&gt;(){
     *       &#64;Override public boolean evaluate(Integer integer) {
     *           return integer % 2 == 0;
     *       }
     *   });
     *   valueOption.hasValue(); // => false
     * </pre>
     * </blockquote>
     *
     * @param iterable  The {@code Iterable} to search for an element matching the supplied
     *                  {@code UnaryPredicate}.
     * @param predicate A {@code UnaryPredicate} that must be satisfied by an element in the
     *                  supplied {@code Iterable}.
     * @param <T>       The type of the elements in the supplied {@code Iterable}.
     * @return An {@code Option} instance representing the first element in the supplied
     *         {@code Iterable} satisfying the supplied {@code UnaryPredicate}.
     */
    public static <T> Option<T> first(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return first(filter(iterable, predicate));
    }

    /**
     * Returns a {@code Collection} containing the first <em>n</em> elements in the supplied
     * {@code Iterable} where <em>n</em> is given by the supplied integer value. If the
     * {@code Iterable} is empty, an empty {@code Collection} is returned, otherwise,
     * a {@code Collection} containing the first <em>n</em> elements is returned.
     *
     * <p>In the case that the supplied {@code Iterable} does not contain enough
     * elements to satisfy the required number, a {@code Collection} containing
     * as many elements as possible is returned.</p>
     *
     * <p>Since a {@code Collection} instance is returned, the element retrieval is performed
     * eagerly, i.e., an attempt is made to retrieve the elements from the underlying
     * {@code Iterable} immediately.</p>
     *
     * <h4>Example Usage:</h4>
     *
     * Given an {@code Iterable} of {@code Integer} instances:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterableWith(5, 4, 3, 2, 1);
     * </pre>
     * </blockquote>
     * Using {@code first}, we can obtain the first three elements in the {@code Iterable}.
     * The following two lines are equivalent in this case:
     * <blockquote>
     * <pre>
     *   Collection&lt;Integer&gt; firstThreeValues = first(values, 3);
     *   Collection&lt;Integer&gt; equivalentValues = Literals.collectionWith(5, 4, 3);
     * </pre>
     * </blockquote>
     * If the input {@code Iterable} does not contain enough elements, we are returned a
     * {@code Collection} with as many elements as possible. The following two lines are
     * equivalent:
     * <blockquote>
     * <pre>
     *   Collection&lt;Integer&gt; firstSixValues = first(values, 6);
     *   Collection&lt;Integer&gt; equivalentValues = Literals.collectionWith(5, 4, 3, 2, 1);
     * </pre>
     * </blockquote>
     * Similarly, if the input {@code Iterable} contains no elements, an empty
     * {@code Collection} is returned:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterable();
     *   Collection&lt;Integer&gt; firstThreeElements = first(values, 3);
     *   firstThreeElements.isEmpty(); // => true
     * </pre>
     * </blockquote>
     *
     * @param iterable The {@code Iterable} from which the first <em>n</em> elements
     *                 should be taken.
     * @param <T>      The type of the elements in the supplied {@code Iterable}.
     * @return A {@code Collection} instance containing the required number of elements
     *         (or less) from the supplied {@code Iterable}.
     */
    public static <T> Collection<T> first(
            Iterable<T> iterable,
            int numberOfElementsRequired) {
        return take(iterable, numberOfElementsRequired);
    }

    /**
     * Returns a {@code Collection} containing the first <em>n</em> elements in the supplied
     * {@code Iterable} that satisfy the supplied {@code UnaryPredicate} where <em>n</em>
     * is given by the supplied integer value. If the {@code Iterable} is empty,
     * an empty {@code Collection} is returned, otherwise, a {@code Collection} containing
     * the first <em>n</em> matching elements is returned.
     *
     * <p>In the case that the supplied {@code Iterable} does not contain enough
     * matching elements to satisfy the required number, a {@code Collection} containing
     * as many elements as possible is returned.</p>
     *
     * <p>Since a {@code Collection} instance is returned, the element retrieval is performed
     * eagerly, i.e., an attempt is made to retrieve matching elements from the underlying
     * {@code Iterable} immediately.</p>
     *
     * <h4>Example Usage:</h4>
     *
     * Given an {@code Iterable} of {@code Integer} instances:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterableWith(8, 7, 6, 5, 4, 3, 2, 1);
     * </pre>
     * </blockquote>
     * Using {@code first}, we can obtain the first three even elements in the {@code Iterable}.
     * The following two expressions are equivalent:
     * <blockquote>
     * <pre>
     *   Collection&lt;Integer&gt; firstThreeEvens = first(values, 3, new Predicate&lt;Integer&gt;(){
     *       &#64;Override public boolean evaluate(Integer integer) {
     *           return integer % 2 == 0;
     *       }
     *   });
     *   Collection&lt;Integer&gt; equivalentEvens = Literals.collectionWith(8, 6, 4);
     * </pre>
     * </blockquote>
     * Note, we used an anonymous {@code Predicate} instance. The {@code Predicate} interface
     * is equivalent to the {@code UnaryPredicate} interface and exists to simplify the
     * eighty percent case with predicates.
     *
     * <p>If the input {@code Iterable} does not contain enough elements satisfying the
     * supplied {@code UnaryPredicate}, we are returned a {@code Collection} with as
     * many matching elements as possible. The following two lines are equivalent:</p>
     * <blockquote>
     * <pre>
     *   Collection&lt;Integer&gt; firstFiveEvens = first(values, 5, new Predicate&lt;Integer&gt;(){
     *       &#64;Override public boolean evaluate(Integer integer) {
     *           return integer % 2 == 0;
     *       }
     *   });
     *   Collection&lt;Integer&gt; equivalentEvens = Literals.collectionWith(8, 6, 4, 2);
     * </pre>
     * </blockquote>
     * Similarly, if no elements match the supplied {@code UnaryPredicate}, we are returned an
     * empty {@code Collection}:
     * <blockquote>
     * <pre>
     *   Iterable&lt;Integer&gt; values = Literals.iterable(9, 7, 5, 3, 1);
     *   Collection&lt;Integer&gt; firstThreeEvens = first(values, 3, new Predicate&lt;Integer&gt;(){
     *       &#64;Override public boolean evaluate(Integer integer) {
     *           return integer % 2 == 0;
     *       }
     *   });
     *   firstThreeEvens.isEmpty(); // => true
     * </pre>
     * </blockquote>
     *
     * @param iterable  The {@code Iterable} to search for elements matching the supplied
     *                  {@code UnaryPredicate}.
     * @param predicate A {@code UnaryPredicate} that must be satisfied by elements in the
     *                  supplied {@code Iterable}.
     * @param <T>       The type of the elements in the supplied {@code Iterable}.
     * @return A {@code Collection} instance containing the required number of elements
     *         (or less) from the supplied {@code Iterable} matching the supplied
     *         {@code UnaryPredicate}.
     */
    public static <T> Collection<T> first(
            Iterable<T> iterable,
            int numberOfElementsRequired,
            UnaryPredicate<? super T> predicate) {
        return first(filter(iterable, predicate), numberOfElementsRequired);
    }

    public static <T> Option<T> last(Iterable<T> iterable) {
        return first(slice(iterable, -1, null));
    }

    public static <T> Option<T> last(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return last(filter(iterable, predicate));
    }

    public static <T> Collection<T> last(
            Iterable<T> iterable,
            int numberOfElementsRequired) {
        if (numberOfElementsRequired < 0) {
            throw new IllegalArgumentException(
                    "Number of elements required cannot be negative");
        }
        if (numberOfElementsRequired == 0) {
            return emptyList();
        }
        return slice(iterable, -numberOfElementsRequired, null);
    }

    public static <T> Collection<T> last(
            Iterable<T> iterable,
            int numberOfElementsRequired,
            UnaryPredicate<? super T> predicate) {
        return last(filter(iterable, predicate), numberOfElementsRequired);
    }

    public static <T> Collection<T> rest(Iterable<T> iterable) {
        return slice(iterable, 1, null);
    }

    public static <T> Collection<T> take(Iterable<T> iterable, int numberToTake) {
        return materialize(Lazily.take(iterable, numberToTake));
    }

    public static <T> Collection<T> drop(Iterable<T> iterable, int numberToDrop) {
        return materialize(Lazily.drop(iterable, numberToDrop));
    }

    public static <T> Pair<Collection<T>, Collection<T>> partition(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        Pair<Iterable<T>, Iterable<T>> partition = Lazily.partition(iterable, predicate);
        return tuple(materialize(partition.first()), materialize(partition.second()));
    }

    public static <T> Collection<Collection<T>> batch(Iterable<T> iterable, int batchSize) {
        Collection<Collection<T>> result = new ArrayList<Collection<T>>();
        Iterable<Iterable<T>> batches = Lazily.batch(iterable, batchSize);
        for (Iterable<T> batch : batches) {
            result.add(materialize(batch));
        }
        return result;
    }

    public static void times(
            int numberOfTimes,
            UnaryProcedure<? super Integer> procedure) {
        if (numberOfTimes < 0) {
            throw new IllegalArgumentException(
                    "The number of times to execute the function cannot be less than zero.");
        }
        for (int i = 0; i < numberOfTimes; i++) {
            procedure.execute(i);
        }
    }

    public static void times(int numberOfTimes, Action<? super Integer> action) {
        times(numberOfTimes, actionUnaryProcedure(action));
    }

    public static <T> Collection<T> takeWhile(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return materialize(Lazily.takeWhile(iterable, predicate));
    }

    public static <T> Collection<T> takeUntil(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return materialize(Lazily.takeUntil(iterable, predicate));
    }

    public static <T> Collection<T> dropWhile(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return materialize(Lazily.dropWhile(iterable, predicate));
    }

    public static <T> Collection<T> dropUntil(
            Iterable<T> iterable,
            UnaryPredicate<? super T> predicate) {
        return materialize(Lazily.dropUntil(iterable, predicate));
    }

    public static <T> Collection<T> slice(
            Iterable<T> iterable,
            Integer start,
            Integer stop,
            Integer step) {
        List<? extends T> inputCollection = Iterables.asList(iterable);

        if (inputCollection.size() == 0) {
            return Collections.emptyList();
        }

        int startIndex = SliceHelper.resolveStartIndex(start, inputCollection.size());
        int stopIndex = SliceHelper.resolveStopIndex(stop, inputCollection.size());
        int stepSize = SliceHelper.resolveStepSize(step);

        IntegerRange requiredElementIndices = new IntegerRange(startIndex, stopIndex, stepSize);
        List<T> outputCollection = new ArrayList<T>();

        for (Integer elementIndex : requiredElementIndices) {
            outputCollection.add(inputCollection.get(elementIndex));
        }

        return outputCollection;
    }

    public static <T> Collection<T> slice(
            Iterable<T> iterable,
            Integer start,
            Integer stop) {
        return slice(iterable, start, stop, 1);
    }

    public static <T> Collection<T> repeat(
            Iterable<T> iterable,
            int numberOfTimesToRepeat) {
        return materialize(Lazily.repeat(iterable, numberOfTimesToRepeat));
    }

    static <T> Option<T> second(Iterable<? extends T> iterable) {
        return first(Lazily.rest(iterable));
    }

    private static class SliceHelper {
        private static int resolveStartIndex(Integer start, Integer numberOfElements) {
            if (start == null || start + numberOfElements < 0) {
                return 0;
            } else if (start < 0) {
                return start + numberOfElements;
            } else if (start > numberOfElements) {
                return numberOfElements - 1;
            } else {
                return start;
            }
        }

        private static int resolveStopIndex(Integer stop, Integer numberOfElements) {
            if (stop == null || stop > numberOfElements) {
                return numberOfElements;
            } else if (stop + numberOfElements < 0) {
                return -1;
            } else if (stop < 0) {
                return stop + numberOfElements;
            } else {
                return stop;
            }
        }

        private static int resolveStepSize(Integer step) {
            if (step == null) {
                return 1;
            } else if (step == 0) {
                throw new IllegalArgumentException("Step size cannot be zero");
            } else {
                return step;
            }
        }
    }
}
