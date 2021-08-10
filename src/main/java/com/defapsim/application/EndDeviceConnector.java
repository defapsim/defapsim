package com.defapsim.application;

/**
 * Connector between component and end device
 */
public class EndDeviceConnector extends Connector {

    public EndDeviceConnector(String identifier, Connectable target) {
        super(identifier, target);
    }
}
