package com.defapsim.application;

import com.defapsim.exceptions.NoNegativeValueException;
import com.defapsim.exceptions.StringEmptyException;

/**
 * An object of the ComponentConfiguration class defines the properties that a component receives.
 */
public class ComponentConfiguration {

    /**
     * Identifier is specified as a string to identify the component.
     */
    protected String identifier = "Generic Component";

    /**
     * memoryDemand is specified as a positive floating point value.
     */
    protected Float memoryDemand = 0.F;

    /**
     * storageDemand is specified as a positive floating point value.
     */
    protected Float storageDemand = 0.F;

    /**
     * computingPowerDemand is specified as a positive floating point value.
     */
    protected Float computingPowerDemand = 0.F;

    /**
     * The worst-case execution time of the component is specified as milliseconds.
     */
    protected Float worstCaseExecutionTime = 0.F;


    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public Float getMemoryDemand() {
        return this.memoryDemand;
    }

    public Float getStorageDemand() {
        return this.storageDemand;
    }

    public Float getComputingPowerDemand() {
        return this.computingPowerDemand;
    }

    public Float getWorstCaseExecutionTime() {
        return this.worstCaseExecutionTime;
    }

    /**
     * Setter (according to Expression Builder pattern)
     */

    public ComponentConfiguration withIdentifier(String identifier) throws StringEmptyException {
        if(identifier == null || identifier.length() <= 0) throw new StringEmptyException("Identifier cannot be empty");
        this.identifier = identifier;
        return this;
    }

    public ComponentConfiguration demandMemory(Float memory) throws NoNegativeValueException {
        //                memory <= 0.F
        if(memory == null || Float.compare(memory, 0.F) <= 0) throw new NoNegativeValueException("MEMORY cannot be null for component " + this.identifier);
        this.memoryDemand = memory;
        return this;
    }

    public ComponentConfiguration demandStorage(Float storage) throws NoNegativeValueException {
        //                    storage <= 0.F
        if(storage == null || Float.compare(storage, 0.F) <= 0)  throw new NoNegativeValueException("STORAGE cannot be null for component " + this.identifier);
        this.storageDemand = storage;
        return this;
    }

    public ComponentConfiguration demandComputingPower(Float computingPower) throws NoNegativeValueException {
        //                computingPower <= 0.F
        if(computingPower == null || Float.compare(computingPower, 0.F) <= 0) throw new NoNegativeValueException("COMPUTING-POWER cannot be null for component " + this.identifier);
        this.computingPowerDemand = computingPower;
        return this;
    }

    public ComponentConfiguration worstCaseExcecutionTime(Float exectime) throws NoNegativeValueException {
        //                     exectime <= 0.F
        if(exectime == null || Float.compare(exectime, 0.F) <= 0)  throw new NoNegativeValueException("Worst-Case execution time cannot be null for component " + this.identifier);
        this.worstCaseExecutionTime = exectime;
        return this;
    }
}
