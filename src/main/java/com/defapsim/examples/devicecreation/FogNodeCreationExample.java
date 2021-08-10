package com.defapsim.examples.devicecreation;

import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.fognode.FogNode;
import com.defapsim.infrastructure.devices.fognode.FogNodeCreator;

/**
 * This example shows how to create a fog node and how to configure it
 */

public class FogNodeCreationExample {

    public static void main(String[] args) {
        // Initialize an "FogNodeCreator". The "FogNodeCreator" can generate fog nodes.
        final FogNodeCreator fogNodeCreator = new FogNodeCreator();
        // Initialize an "DeviceConfiguration". The device can be configured with the help of the "DeviceConfiguration".
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration()
                .withMemory(30.F)                                  // The memory of the fog node should be 30.F
                .withComputingPower(20.F)                       // The computing power of the fog node should be 20.F
                .withStorage(90.F)                              // The storage of the fog node should be 90.F
                .withProcessingSpeed(3.F)                       // The processing speed of the fog node should be 3.F
                .withIdentifier("Example fog node");            // The name of the fog node should be "Example fog node"
        // The "DeviceConfiguration" is handed over to the "FogNodeCreator".
        // The "FogNodeCreator" creates the fog node on the basis of the "DeviceConfiguration".
        FogNode fogNode = (FogNode) fogNodeCreator.register(deviceConfiguration);
    }
}
