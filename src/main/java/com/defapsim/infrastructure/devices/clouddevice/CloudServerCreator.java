package com.defapsim.infrastructure.devices.clouddevice;

import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.DeviceCreator;

/**
 * Concrete creator for the cloud servers.
 */
public class CloudServerCreator extends DeviceCreator {

    /**
     * Implementation of the Factory Method for the cloud servers.
     * @param deviceConfiguration   The device configuration from which the device properties are taken
     * @return      One CloudServer instance
     */
    @Override
    protected Device createDevice(DeviceConfiguration deviceConfiguration) {
        Device cloudDevice = new CloudServer();
        return cloudDevice;
    }
}
