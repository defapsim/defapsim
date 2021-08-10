package com.defapsim.algorithms;

import com.defapsim.infrastructure.devices.Device;

/**
 * The algorithms for the placement of the component
 * All algorithms inherit from "PlacementAlgorithm"
 */
public abstract class PlacementAlgorithm {

    /**
     * The device that executes the PlacementAlgorithm
     */
    protected Device algorithmInitDevice;

    public void setAlgorithmInitDevice(Device algorithmInitDevice) {
        this.algorithmInitDevice = algorithmInitDevice;
    }

    /**
     * Used to parse arbitrary parameters for a concrete algorithm.
     * @param algorithmParameters       Arbitrary parameters which have to be casted
     */
    protected abstract void castAlgorithmParameters(Object ... algorithmParameters);

    /**
     * Start the algorithm.
     * @param algorithmParameters       Arbitrary parameters which have to be casted
     */
    public abstract void start(Object ... algorithmParameters);
}
