package com.defapsim.infrastructure.devices.enddevice;

import com.defapsim.application.Connectable;
import com.defapsim.application.EndDeviceConnector;
import com.defapsim.infrastructure.devices.Device;

import java.util.LinkedList;
import java.util.List;

/**
 * An end device implements a connectable and can thus be a source and a target of a connector
 */
public class EndDevice extends Device implements Connectable {

    private List<EndDeviceConnector> connectors = new LinkedList<>();

    public EndDevice() {
        // EMPTY DEFAULT CONSTRUCTOR
    }

    public List<EndDeviceConnector> getConnectors() {
        return this.connectors;
    }

    public void addEndDeviceConnector(EndDeviceConnector endDeviceConnector) {
        this.connectors.add(endDeviceConnector);
    }
}
