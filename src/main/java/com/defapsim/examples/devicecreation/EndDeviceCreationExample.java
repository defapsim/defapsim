package com.defapsim.examples.devicecreation;

import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.devices.enddevice.EndDeviceCreator;

/**
 * This example shows how to create an end device
 */

public class EndDeviceCreationExample {

    public static void main(String[] args) {
        // Initialize an "EndDeviceCreator". The "EndDeviceCreator" can generate end devices.
        final EndDeviceCreator endDeviceCreator = new EndDeviceCreator();
        // Initialize an "DeviceConfiguration". The device can be configured with the help of the "DeviceConfiguration".
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration()
                .withIdentifier("Example end device");          // The name of the end device should be "Example end device"
        // The "DeviceConfiguration" is handed over to the "EndDeviceCreator".
        // The "EndDeviceCreator" creates the end device on the basis of the "DeviceConfiguration".
        EndDevice endDevice = (EndDevice) endDeviceCreator.register(deviceConfiguration);
    }
}


