package com.defapsim.evaluation;

/**
 * Attributes (in this case metrics) can be defined in an evaluation object.
 * The attributes can be filled with data during the execution of an algorithm.
 * Thus, an evaluation object can be passed to an algorithm by using the start() method.
 */
public class Evaluation {

    /**
     * algorithmName is specified as a string to identify the algorithm that is evaluated.
     */
    private String algorithmName = "Generic algorithm name";

    /**
     * applicationLatencyInitial is specified as a floating point number in milliseconds for the initial application latency.
     */
    private Float applicationLatencyInitial = 0.F;

    /**
     * applicationLatencyAfterOptimization is specified as a floating point number in milliseconds for the application latency after running the algorithm.
    */
    private Float applicationLatencyAfterOptimization = 0.F;

    /**
     * executionTimeOfTheAlgorithm is specified as a floating Point Number in milliseconds for the execution time of the algorithm. (It is strongly influenced by the system, this simulator is executed on)
     */
    private Float executionTimeOfTheAlgorithm = 0.F;

    /**
     * The number of used application components
     */
    private Integer applicationComponents = 0;

    /**
     * The number of used cloud servers
     */
    private Integer cloudServers = 0;

    /**
     * The number of used edge nodes
     */
    private Integer edgeNodes = 0;

    /**
     * The number of used end devices
     */
    private Integer endDevices = 0;

    /**
     * The number of used end devices
     */
    private Integer amountOfAuctions = 0;

    /**
     * The number of used end devices
     */
    private Integer amountOfTrades = 0;

    /**
     * The number of used end devices
     */
    private Integer amountOfMigrations = 0;

    /**
     * Getter
     */

    public String getAlgorithmName() {
        return this.algorithmName;
    }

    public Float getApplicationLatencyInitial() {
        return this.applicationLatencyInitial;
    }

    public Float getApplicationLatencyAfterOptimization() {
        return this.applicationLatencyAfterOptimization;
    }

    public Integer getAmountOfCombinedMigrations() {
        return this.amountOfMigrations + this.amountOfTrades * 2;
    }

    public Float getExecutionTimeOfTheAlgorithm() {
        return this.executionTimeOfTheAlgorithm;
    }

    public Integer getApplicationComponents() {
        return this.applicationComponents;
    }

    public Integer getCloudServers() {
        return this.cloudServers;
    }

    public Integer getEdgeNodes() {
        return this.edgeNodes;
    }

    public Integer getEndDevices() {
        return this.endDevices;
    }

    public Integer getAmountOfMigrations() {
        return this.amountOfMigrations;
    }

    public Integer getAmountOfTrades() {
        return this.amountOfTrades;
    }

    public Integer getAmountOfAuctions() {
        return this.amountOfAuctions;
    }


    /**
     * Setter (according to Expression Builder pattern)
     */

    public Evaluation withAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
        return this;
    }

    public Evaluation withApplicationLatencyInitial(Float applicationLatencyInitial) {
        this.applicationLatencyInitial = applicationLatencyInitial;
        return this;
    }

    public Evaluation withApplicationLatencyAfterOptimization(Float applicationLatencyAfterOptimization) {
        this.applicationLatencyAfterOptimization = applicationLatencyAfterOptimization;
        return this;
    }

    public Evaluation withAmountOfMigrations(Integer amountOfMigrations) {
        this.amountOfMigrations = amountOfMigrations;
        return this;
    }

    public Evaluation withAmountOfAuctions(Integer amountOfAuctions) {
        this.amountOfAuctions = amountOfAuctions;
        return this;
    }

    public Evaluation withAmountOfTrades(Integer amountOfTrades) {
        this.amountOfTrades = amountOfTrades;
        return this;
    }

    public Evaluation withExecutionTimeOfTheAlgorithm(Float executionTimeOfTheAlgorithm) {
        this.executionTimeOfTheAlgorithm = executionTimeOfTheAlgorithm;
        return this;
    }

    public Evaluation withApplicationComponents(Integer components) {
        this.applicationComponents = components;
        return this;
    }

    public Evaluation withCloudServers(Integer cloudServers) {
        this.cloudServers = cloudServers;
        return this;
    }

    public Evaluation withEdgeNodes(Integer edgeNodes) {
        this.edgeNodes = edgeNodes;
        return this;
    }

    public Evaluation withEndDevices(Integer endDevices) {
        this.endDevices = endDevices;
        return this;
    }


}
