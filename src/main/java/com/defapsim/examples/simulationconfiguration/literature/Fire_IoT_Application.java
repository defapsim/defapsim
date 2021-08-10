package com.defapsim.examples.simulationconfiguration.literature;

import com.defapsim.application.*;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.clouddevice.CloudServerCreator;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.DeviceCreator;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.devices.enddevice.EndDeviceCreator;
import com.defapsim.infrastructure.devices.fognode.FogNodeCreator;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.LinkConfiguration;
import com.defapsim.misc.Tupel;

/**
 * This infrastructure and application structure was taken from:
 * Antonio Brogi and Stefano Forti. QoS-aware deployment of IoT applications through the fog.
 * IEEE Internet of Things Journal, 4(5):1185–1192, 2017.
 */

public class Fire_IoT_Application {

    /**
     * Get the infrastructure and application from Brogi et al (see above).
     * @return      tupel(infrastructure, application)
     */
    public static Tupel getSimulationConfigurationTupel() {

        final DeviceCreator endDeviceCreator = new EndDeviceCreator();
        final FogNodeCreator fogNodeCreator = new FogNodeCreator();
        final CloudServerCreator cloudServerCreator = new CloudServerCreator();

        Infrastructure infrastructure = new Infrastructure("INFRASTRUCTURE");

        DeviceConfiguration deviceConfiguration = new DeviceConfiguration().withMemory(0.F).withComputingPower(0.F)
                .withProcessingSpeed(0.F).withInfrastructure(infrastructure);

        // Create four end devices
        Device end_device_1 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 1"));
        Device end_device_2 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 2"));
        Device end_device_3 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 3"));
        Device end_device_4 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 4"));

        // Create three fog nodes
        Device fog_node_1 = fogNodeCreator.register(deviceConfiguration.withIdentifier("fog-node 1").withMemory(2.F).withComputingPower(2.F).withProcessingSpeed(1.33F));
        Device fog_node_2 = fogNodeCreator.register(deviceConfiguration.withIdentifier("fog-node 2").withMemory(4.F).withComputingPower(1.F).withProcessingSpeed(1.46F));
        Device fog_node_3 = fogNodeCreator.register(deviceConfiguration.withIdentifier("fog-node 3").withMemory(10.F).withComputingPower(2.F).withProcessingSpeed(1.33F));

        // Create two cloud servers
        Device cloud_server_1 = cloudServerCreator.register(deviceConfiguration.withIdentifier("cloud-server-1").withMemory(Float.MAX_VALUE).withComputingPower(Float.MAX_VALUE).withProcessingSpeed(2.5F));
        Device cloud_server_2 = cloudServerCreator.register(deviceConfiguration.withIdentifier("cloud-server-2").withMemory(Float.MAX_VALUE).withComputingPower(Float.MAX_VALUE).withProcessingSpeed(3.0F));

        // Links: End devices to fog nodes
        Link.addUndirected(new LinkConfiguration().from(end_device_1).to(fog_node_1).withNameToFrom("LINK-1").withNameFromTo("LINK-1").withLatencyFromTo(0.F).withLatencyToFrom(0.F));
        Link.addUndirected(new LinkConfiguration().from(end_device_2).to(fog_node_1).withNameToFrom("LINK-2").withNameFromTo("LINK-2").withLatencyFromTo(0.F).withLatencyToFrom(0.F));
        Link.addUndirected(new LinkConfiguration().from(end_device_3).to(fog_node_2).withNameToFrom("LINK-3").withNameFromTo("LINK-3").withLatencyFromTo(0.F).withLatencyToFrom(0.F));
        Link.addUndirected(new LinkConfiguration().from(end_device_4).to(fog_node_2).withNameToFrom("LINK-4").withNameFromTo("LINK-4").withLatencyFromTo(0.F).withLatencyToFrom(0.F));

        // Links: Fog nodes to fog nodes
        Link.addUndirected(new LinkConfiguration().from(fog_node_1).to(fog_node_2).withNameToFrom("LINK-5").withNameFromTo("LINK-5").withLatencyFromTo(1.F).withLatencyToFrom(1.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_1).to(fog_node_3).withNameToFrom("LINK-6").withNameFromTo("LINK-6").withLatencyFromTo(5.F).withLatencyToFrom(5.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_2).to(fog_node_3).withNameToFrom("LINK-7").withNameFromTo("LINK-7").withLatencyFromTo(5.F).withLatencyToFrom(5.F));

        // Links: Fog nodes to cloud
        Link.addUndirected(new LinkConfiguration().from(fog_node_1).to(cloud_server_1).withNameToFrom("LINK-8").withNameFromTo("LINK-8").withLatencyFromTo(130.F).withLatencyToFrom(130.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_2).to(cloud_server_1).withNameToFrom("LINK-9").withNameFromTo("LINK-9").withLatencyFromTo(100.F).withLatencyToFrom(100.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_3).to(cloud_server_1).withNameToFrom("LINK-10").withNameFromTo("LINK-10").withLatencyFromTo(35.F).withLatencyToFrom(35.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_1).to(cloud_server_2).withNameToFrom("LINK-11").withNameFromTo("LINK-11").withLatencyFromTo(200.F).withLatencyToFrom(200.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_2).to(cloud_server_2).withNameToFrom("LINK-12").withNameFromTo("LINK-12").withLatencyFromTo(180.F).withLatencyToFrom(180.F));
        Link.addUndirected(new LinkConfiguration().from(fog_node_3).to(cloud_server_2).withNameToFrom("LINK-13").withNameFromTo("LINK-13").withLatencyFromTo(45.F).withLatencyToFrom(45.F));

        // Resolve the routes between the devices in the infrastructure.
        infrastructure.resolveRoutes();

        // Create an application
        Application application = new Application("Fire alarm IoT application");
        ComponentConfiguration componentConfiguration = new ComponentConfiguration();

        // Create three components
        Component fire_manager = Component.createFromConfiguration(componentConfiguration.withIdentifier("FIRE_MANAGER").demandMemory(1.F).demandComputingPower(1.F).worstCaseExcecutionTime(25.F)); // 1GB
        Component insights_backend = Component.createFromConfiguration(componentConfiguration.withIdentifier("INSIGHTS_BACKEND").demandMemory(2.F).demandComputingPower(1.F).worstCaseExcecutionTime(50.F)); // 2GB
        Component machine_learning_engine = Component.createFromConfiguration(componentConfiguration.withIdentifier("MACHINE_LEARNING_ENGINE").demandMemory(8.F).demandComputingPower(2.F).worstCaseExcecutionTime(90.F)); // 8GB

        // Bind the components to the application
        application.withComponent(fire_manager).withComponent(insights_backend).withComponent(machine_learning_engine);

        // Set location restrictions for the components
        insights_backend.addHostToBlacklist((ApplicationHostDevice) fog_node_1);
        insights_backend.addHostToBlacklist((ApplicationHostDevice) fog_node_2);
        insights_backend.addHostToBlacklist((ApplicationHostDevice) cloud_server_1);

        fire_manager.addHostToBlacklist((ApplicationHostDevice) fog_node_3);
        fire_manager.addHostToBlacklist((ApplicationHostDevice) cloud_server_1);

        machine_learning_engine.addHostToBlacklist((ApplicationHostDevice) fog_node_1);
        machine_learning_engine.addHostToBlacklist((ApplicationHostDevice) fog_node_2);

        // Create connectors between components
        Connector.addUndirected(new ConnectorConfiguration<Component, Component>().from(fire_manager).to(insights_backend)
                .withNameFromTo("Connector-1").withNameToFrom("Connector-1"));
        Connector.addUndirected(new ConnectorConfiguration<Component, Component>().from(insights_backend).to(machine_learning_engine)
                .withNameFromTo("Connector-2").withNameToFrom("Connector-2"));
        Connector.addDirected(new ConnectorConfiguration<Component, Component>().from(fire_manager).to(machine_learning_engine)
                .withNameFromTo("Connector-3").withNameToFrom("Connector-3"));

        // Create connectors between end devices and components
        Connector.addUndirected(new ConnectorConfiguration<EndDevice, Component>().from((EndDevice)end_device_1).to(fire_manager)
                .withNameFromTo("Connector-4").withNameToFrom("Connector-4"));
        Connector.addUndirected(new ConnectorConfiguration<EndDevice, Component>().from((EndDevice)end_device_2).to(fire_manager)
                .withNameFromTo("Connector-5").withNameToFrom("Connector-5"));

        return new Tupel(infrastructure, application);
    }
}
