package com.tus.anomalydetector.models;

import java.util.Deque;
import java.util.LinkedList;

/**
 * A fixed-size deque (double-ended queue) for storing numeric elements.
 * <p>
 * When the deque reaches its maximum size, adding a new element to the front
 * will automatically remove the element at the end.
 *
 * @param <T> The type of elements stored, extending {@link Number}.
 */

public class FixedSizeDeque<T extends Number> {

    private final int maxSize;

    private final Deque<T> deque;

    /**
     * Constructs a FixedSizeDeque with the specified maximum size.
     *
     * @param maxSize the maximum number of elements allowed in the deque
     */
    public FixedSizeDeque(final int maxSize) {
        this.maxSize = maxSize;
        this.deque = new LinkedList<>();
    }

    /**
     * Adds an element to the front of the deque.
     * If the deque is full, the last element is removed to make space.
     *
     * @param element the element to add
     */
    public void addFirst(final T element) {
        if (this.deque.size() == this.maxSize) {
            this.deque.removeLast();
        }

        this.deque.addFirst(element);
    }

    /**
     * Returns the current number of elements in the deque.
     *
     * @return the size of the deque
     */
    public int size() {
        return this.deque.size();
    }

    /**
     * Calculates the sum of all elements in the deque.
     *
     * @return the sum of the elements
     */
    public double sum() {
        return this.deque.stream().mapToDouble(Number::doubleValue).sum();
    }

    /**
     * Calculates the mean (average) of all elements in the deque.
     *
     * @return the mean of the elements, or 0 if the deque is empty
     */
    public double mean() {
        return this.deque.stream().mapToDouble(Number::doubleValue).average().orElse(0);
    }

    /**
     * Calculates the variance of the elements in the deque.
     *
     * @return the variance, or 0 if the deque is empty
     */
    public double variance() {
        final double mean = this.mean();
        return this.deque.stream()
                .mapToDouble(Number::doubleValue).map(value -> Math.pow(value - mean, 2))
                .average().orElse(0);
    }

    /**
     * Calculates the standard deviation of the elements in the deque.
     *
     * @return the standard deviation, or 0 if the deque is empty
     */
    public double standardDeviation() {
        return Math.sqrt(this.variance());
    }
}
