package com.defapsim.infrastructure.devices.fognode;

import com.defapsim.infrastructure.devices.*;

/**
 * Concrete creator for the fog nodes.
 */
public class FogNodeCreator extends DeviceCreator {

    /**
     * Implementation of the Factory Method for the fog nodes.
     * @param deviceConfiguration   The device configuration from which the device properties are taken
     * @return      One FogNode instance
     */
    @Override
    protected Device createDevice(DeviceConfiguration deviceConfiguration) {
        Device fogNode = new FogNode();
        return fogNode;
    }
}
