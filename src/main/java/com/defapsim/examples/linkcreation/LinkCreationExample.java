package com.defapsim.examples.linkcreation;

import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.fognode.FogNodeCreator;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.LinkConfiguration;

/**
 * This example shows how to create an infrastructure and resolve the routes between the devices in the infrastructure.
 */

public class LinkCreationExample {

    public static void main(String[] args) {
        Infrastructure infrastructure = new Infrastructure();
        final FogNodeCreator fogNodeCreator = new FogNodeCreator();
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration().withComputingPower(20.F)
                .withMemory(30.F).withStorage(90.F).withInfrastructure(infrastructure);

        // Create three fog nodes
        Device fog_node_1 = fogNodeCreator.register(deviceConfiguration.withIdentifier("fog-node 1"));
        Device fog_node_2 = fogNodeCreator.register(deviceConfiguration.withIdentifier("fog-node 2"));
        Device fog_node_3 = fogNodeCreator.register(deviceConfiguration.withIdentifier("fog-node 3"));

        // Create two links:
        //                  fog_node_1 --(LINK-1a)-> fog_node_2     (latency 10.F)
        //                  fog_node_1 <-(LINK-1b)-- fog_node_2     (latency 10.F)
        Link.addUndirected(new LinkConfiguration().from(fog_node_1).to(fog_node_2)
                .withNameFromTo("LINK-1a").withNameToFrom("LINK-1b")
                .withLatencyFromTo(10.F).withLatencyToFrom(10.F));

        // Create one link:
        //                  fog_node_2 --(LINK-2)-> fog_node_3     (latency 15.F)
        Link.addDirected(new LinkConfiguration().from(fog_node_2).to(fog_node_3)
                .withNameFromTo("LINK-2")
                .withLatencyFromTo(15.F));

        // Resolve the routes between the devices in the infrastructure.
        infrastructure.resolveRoutes();
    }
}
