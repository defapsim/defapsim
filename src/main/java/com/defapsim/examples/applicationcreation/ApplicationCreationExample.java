package com.defapsim.examples.applicationcreation;

import com.defapsim.application.*;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.devices.enddevice.EndDeviceCreator;

/**
 * This example shows how an application can be created
 */

public class ApplicationCreationExample {

    public static void main(String[] args) {
        final EndDeviceCreator endDeviceCreator = new EndDeviceCreator();
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration();

        // Create one end device
        EndDevice end_device_1 = (EndDevice) endDeviceCreator.register(deviceConfiguration);

        // Create an application with the name "Example application"
        Application application = new Application("Example application");
        ComponentConfiguration componentConfiguration = new ComponentConfiguration();

        // Create three components
        Component component_1 = Component.createFromConfiguration(componentConfiguration
                .withIdentifier("Example component 1")
                .demandMemory(1.F).demandComputingPower(1.F).worstCaseExcecutionTime(25.F));
        Component component_2 = Component.createFromConfiguration(componentConfiguration
                .withIdentifier("Example component 2")
                .demandMemory(2.F).demandComputingPower(1.F).worstCaseExcecutionTime(50.F));
        Component component_3 = Component.createFromConfiguration(componentConfiguration
                .withIdentifier("Example component 3")
                .demandMemory(4.F).demandComputingPower(1.F).worstCaseExcecutionTime(40.F));

        // Bind the components to the application
        application.withComponent(component_1).withComponent(component_2)
                .withComponent(component_3);

        // Create two connectors:
        //                  component_1 --(Connector-1a)-> component_2
        //                  component_1 <-(Connector-1b)-- component_2
        Connector.addUndirected(new ConnectorConfiguration<Component, Component>()
                .from(component_1).to(component_2)
                .withNameFromTo("Connector-1a").withNameToFrom("Connector-1b"));

        // Create one connector:
        //                  component_2 --(Connector-2)-> component_3
        Connector.addDirected(new ConnectorConfiguration<Component, Component>()
                .from(component_2).to(component_3)
                .withNameFromTo("Connector-2"));

        // Create one connector:
        //                  component_1 --(Connector-3)-> end_device_1
        Connector.addDirected(new ConnectorConfiguration<Component, EndDevice>()
                .from(component_1).to(end_device_1)
                .withNameFromTo("Connector-3"));
    }
}
