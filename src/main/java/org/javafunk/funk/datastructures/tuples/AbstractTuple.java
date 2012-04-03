package org.javafunk.funk.datastructures.tuples;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Iterator;

import static java.lang.String.format;
import static org.javafunk.funk.Lazy.zip;
import static org.javafunk.funk.Strings.join;

public abstract class AbstractTuple implements Iterable<Object> {
    public abstract Iterable<Object> values();

    @Override
    public String toString() {
        return format("(%s)", join(values(), ", "));
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other == this) {
            return true;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        EqualsBuilder equalsBuilder = new EqualsBuilder();
        Iterable<Pair<Object, Object>> zippedValues = zip(this.values(), ((AbstractTuple) other).values());
        for (Pair<Object, Object> values : zippedValues) {
            equalsBuilder.append(values.first(), values.second());
        }

        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        for (Object value : values()) {
            hashCodeBuilder.append(value);
        }
        return hashCodeBuilder.toHashCode();
    }

    public Iterator<Object> iterator(){
        return values().iterator();
    }

}
