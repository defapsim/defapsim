package com.defapsim.infrastructure.devices.edgenode;

import com.defapsim.infrastructure.devices.*;

/**
 * Concrete creator for the edge nodes.
 */
public class EdgeNodeCreator extends DeviceCreator {

    /**
     * Implementation of the Factory Method for the edge nodes.
     * @param deviceConfiguration   The device configuration from which the device properties are taken
     * @return      One EdgeNode instance
     */
    @Override
    protected Device createDevice(DeviceConfiguration deviceConfiguration) {
        Device edgeNode = new EdgeNode();
        return edgeNode;
    }
}
