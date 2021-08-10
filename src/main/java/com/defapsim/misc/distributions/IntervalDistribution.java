package com.defapsim.misc.distributions;

import java.util.Random;

/**
 * The class IntervalDistribution determines a random value between a minimum and a maximum.
 * Optionally, a seed can be specified so that the value is deterministic.
 */
public class IntervalDistribution implements Distribution {

    private Double min;
    private Double max;

    private Random random;

    private Integer seed;

    public IntervalDistribution(Double min, Double max) {
        this.min = min;
        this.max = max;
        this.random = new Random();
    }

    public IntervalDistribution(Double min, Double max, Integer seed) {
        this.min = min;
        this.max = max;
        this.random = new Random(seed);
    }

    public Double getValue() {
        return this.random.nextDouble() * (this.max - this.min) + this.min;
    }
}
