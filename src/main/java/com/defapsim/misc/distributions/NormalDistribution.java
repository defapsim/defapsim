package com.defapsim.misc.distributions;

import java.util.Random;

/**
 * Generates values according to a normal distribution
 */
public class NormalDistribution implements Distribution {

    /**
     * Private static Singleton instance
     */
    private static final NormalDistribution instance = new NormalDistribution();

    /**
     * Private constructor to prevent it from being accessed
     */
    private NormalDistribution() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static NormalDistribution getInstance() {
        return instance;
    }

    private Double mean;
    private Double standardDeviation;

    private Random random;

    public NormalDistribution setParameterValues(Double mean, Double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.random = new Random();
        return this;
    }

    public NormalDistribution setParameterValues(Double mean, Double standardDeviation, Integer seed) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.random = new Random(seed);
        return this;
    }

    public Double getValue() {
        return this.random.nextGaussian() * standardDeviation + mean;
    }

}
