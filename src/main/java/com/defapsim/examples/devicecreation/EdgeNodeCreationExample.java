package com.defapsim.examples.devicecreation;

import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.edgenode.EdgeNode;
import com.defapsim.infrastructure.devices.edgenode.EdgeNodeCreator;

/**
 * This example shows how to create a edge node and how to configure it
 */

public class EdgeNodeCreationExample {

    public static void main(String[] args) {
        // Initialize an "EdgeNodeCreator". The "EdgeNodeCreator" can generate edge nodes.
        final EdgeNodeCreator edgeNodeCreator = new EdgeNodeCreator();
        // Initialize an "DeviceConfiguration". The device can be configured with the help of the "DeviceConfiguration".
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration()
                .withMemory(30.F)                                  // The memory of the edge node should be 30.F
                .withComputingPower(20.F)                       // The computing power of the edge node should be 20.F
                .withStorage(90.F)                              // The storage of the edge node should be 90.F
                .withProcessingSpeed(3.F)                       // The processing speed of the edge node should be 3.F
                .withIdentifier("Example edge node");            // The name of the edge node should be "Example edge node"
        // The "DeviceConfiguration" is handed over to the "EdgeNodeCreator".
        // The "EdgeNodeCreator" creates the edge node on the basis of the "DeviceConfiguration".
        EdgeNode edgeNode = (EdgeNode) edgeNodeCreator.register(deviceConfiguration);
    }
}
