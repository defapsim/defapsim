package com.defapsim.infrastructure.devices;

import com.defapsim.exceptions.NoNegativeValueException;
import com.defapsim.exceptions.StringEmptyException;
import com.defapsim.infrastructure.Infrastructure;

/**
 * An object of the DeviceConfiguration class defines the properties that a device receives.
 */
public class DeviceConfiguration {

    /**
     * Identifier is specified as a string to identify the device.
     */
    private String identifier = "Generic Device";

    /**
     * The infrastructure to which the device belongs
     */
    private Infrastructure infrastructure;

    /**
     * memory is specified as a positive floating point value.
     */
    private Float memory = 0.F;

    /**
     * storage is specified as a positive floating point value.
     */
    private Float storage = 0.F;

    /**
     * processingPower is specified as a positive floating point value.
     */
    private Float computingPower = 0.F;

    /**
     * processingSpeed is specified as a positive floating point value.
     */
    private Float processingSpeed = 0.F;

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public Infrastructure getInfrastructure() {
        return this.infrastructure;
    }

    public Float getMemory() {
        return this.memory;
    }

    public Float getStorage() {
        return this.storage;
    }

    public Float getComputingPower() {
        return this.computingPower;
    }

    public Float getProcessingSpeed() {
        return this.processingSpeed;
    }

    /**
     * Setter (according to Expression Builder pattern)
     */

    public DeviceConfiguration withIdentifier(String identifier) {
        if(identifier == null) throw new NullPointerException("Identifier cannot be null.");
        if(identifier.length() <= 0) throw new StringEmptyException("Identifier cannot be empty.");
        this.identifier = identifier;
        return this;
    }

    public DeviceConfiguration withInfrastructure(Infrastructure infrastructure) {
        if(infrastructure == null) throw new NullPointerException("Infrastructure cannot be null for device " + this.identifier + ".");
        this.infrastructure = infrastructure;
        return this;
    }

    public DeviceConfiguration withMemory(Float memory) {
        if(memory == null) throw new NullPointerException("Memory cannot be null for device " + this.identifier + ".");
        // memory < 0
        if(Float.compare(memory, 0.F) < 0) throw new NoNegativeValueException("Memory cannot be negative for device " + this.identifier + ".");
        this.memory = memory;
        return this;
    }

    public DeviceConfiguration withStorage(Float storage) {
        if(storage == null) throw new NullPointerException("Storage cannot be null for device " + this.identifier + ".");
        // storage < 0
        if(Float.compare(storage, 0.F) < 0)throw new NoNegativeValueException("Storage cannot be negative for device " + this.identifier + ".");
        this.storage = storage;
        return this;
    }

    public DeviceConfiguration withComputingPower(Float computingPower) {
        if(computingPower == null) throw new NullPointerException("ComputingPower cannot be null for device " + this.identifier + ".");
        // computingPower < 0
        if(Float.compare(computingPower, 0.F) < 0) throw new NoNegativeValueException("ComputingPower cannot be negative for device " + this.identifier + ".");
        this.computingPower = computingPower;
        return this;
    }

    public DeviceConfiguration withProcessingSpeed(Float processingSpeed) {
        if(processingSpeed == null) throw new NullPointerException("ProcessingSpeed cannot be null for device " + this.identifier + ".");
        // processingSpeed < 0
        if(Float.compare(processingSpeed, 0.F) < 0) throw new NoNegativeValueException("ProcessingSpeed cannot be negative for device " + this.identifier + ".");
        this.processingSpeed = processingSpeed;
        return this;
    }
}
