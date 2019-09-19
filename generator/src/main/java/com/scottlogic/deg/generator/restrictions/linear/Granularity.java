package com.scottlogic.deg.generator.restrictions.linear;

public interface Granularity<T> {

    boolean isCorrectScale(T value);

    Granularity<T> merge(Granularity<T> otherGranularity);

    T getNext(T value);

    T trimToGranularity(T value);
}
