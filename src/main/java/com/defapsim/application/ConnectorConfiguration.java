package com.defapsim.application;

/**
 * A connector is logically created by the following notation: from ---> to (where from <--> to is also possible)
 * "from" indicates the source "Connectable"
 * "to" indicates the target "Connectable"
 * @param <S>   The source of the connector
 * @param <T>   The target of the connector
 */
public class ConnectorConfiguration<S extends Connectable, T extends Connectable> {

    /**
     * Identifier is specified as a string to identify the connector. Direction: from --(identifierA)-> to
     */
    private String identifierA = "Generic connector";

    /**
     * Identifier is specified as a string to identify the connector. Direction: from <-(identifierB)-- to
     */
    private String identifierB = "Generic connector";

    /**
     * The source of the connector
     */
    private S source;

    /**
     * The target of the connector
     */
    private T target;

    /**
     * Getter
     */

    public String getIdentifierA() {
        return identifierA;
    }

    public String getIdentifierB() {
        return identifierB;
    }

    public S getSource() {
        return this.source;
    }

    public T getTarget() {
        return  this.target;
    }

    /**
     * Setter (according to Expression Builder pattern)
     */

    public ConnectorConfiguration<S,T> from(S source) {
        this.source = source;
        return this;
    }

    public ConnectorConfiguration<S,T> to(T target) {
        this.target = target;
        return this;
    }

    public ConnectorConfiguration<S,T> withNameFromTo(String s) {
        this.identifierA = s;
        return this;
    }

    public ConnectorConfiguration<S,T> withNameToFrom(String s) {
        this.identifierB = s;
        return this;
    }
}
