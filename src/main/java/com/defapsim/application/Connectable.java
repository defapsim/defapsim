package com.defapsim.application;

import java.util.List;

/**
 *  All classes implementing a Connectable can be used as source and target for the application connectors
 * @param <S>
 */
public interface Connectable<S extends Connector> {
    public List<S> getConnectors();
    String getIdentifier();
}
