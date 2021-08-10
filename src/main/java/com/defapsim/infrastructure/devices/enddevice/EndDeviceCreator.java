package com.defapsim.infrastructure.devices.enddevice;

import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceCreator;

/**
 * Concrete creator for the end devices.
 */
public class EndDeviceCreator extends DeviceCreator {

    /**
     * Implementation of the Factory Method for the end device.
     * @param deviceConfiguration   The device configuration from which the device properties are taken
     * @return      One EndDevice instance
     */
    @Override
    protected Device createDevice(DeviceConfiguration deviceConfiguration) {
        Device endDevice = new EndDevice();
        return endDevice;
    }
}
