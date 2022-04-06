package com.defapsim.examples.linkcreation;

import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.edgenode.EdgeNodeCreator;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.LinkConfiguration;

/**
 * This example shows how to create an infrastructure and resolve the routes between the devices in the infrastructure.
 */

public class LinkCreationExample {

    public static void main(String[] args) {
        Infrastructure infrastructure = new Infrastructure();
        final EdgeNodeCreator edgeNodeCreator = new EdgeNodeCreator();
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration().withComputingPower(20.F)
                .withMemory(30.F).withStorage(90.F).withInfrastructure(infrastructure);

        // Create three edge nodes
        Device edge_node_1 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 1"));
        Device edge_node_2 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 2"));
        Device edge_node_3 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 3"));

        // Create two links:
        //                  edge_node_1 --(LINK-1a)-> edge_node_2     (latency 10.F)
        //                  edge_node_1 <-(LINK-1b)-- edge_node_2     (latency 10.F)
        Link.addUndirected(new LinkConfiguration().from(edge_node_1).to(edge_node_2)
                .withNameFromTo("LINK-1a").withNameToFrom("LINK-1b")
                .withLatencyFromTo(10.F).withLatencyToFrom(10.F));

        // Create one link:
        //                  edge_node_2 --(LINK-2)-> edge_node_3     (latency 15.F)
        Link.addDirected(new LinkConfiguration().from(edge_node_2).to(edge_node_3)
                .withNameFromTo("LINK-2")
                .withLatencyFromTo(15.F));

        // Resolve the routes between the devices in the infrastructure.
        infrastructure.resolveRoutes();
    }
}
