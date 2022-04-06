package com.defapsim.examples.routing;

import com.defapsim.infrastructure.devices.clouddevice.CloudServerCreator;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.DeviceCreator;
import com.defapsim.infrastructure.devices.enddevice.EndDeviceCreator;
import com.defapsim.infrastructure.devices.edgenode.EdgeNodeCreator;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.LinkConfiguration;
import com.defapsim.misc.print.RoutePrinter;

/**
 * This is an example of how devices can be connected with links. It also shows how to resolve routes between devices.
 * Here, each device resolves the routes for itself.
 */

public class Routing2 {

    public static void main(String[] args) {
        final DeviceCreator endDeviceCreator = new EndDeviceCreator();
        final EdgeNodeCreator edgeNodeCreator = new EdgeNodeCreator();
        final CloudServerCreator cloudServerCreator = new CloudServerCreator();

        DeviceConfiguration deviceConfiguration = new DeviceConfiguration().withComputingPower(20.F)
                .withMemory(30.F).withStorage(90.F);

        Device end_device_1 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 1"));
        Device end_device_2 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 2"));

        Device edge_node_1 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 1"));
        Device edge_node_2 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 2"));
        Device edge_node_3 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 3"));
        Device edge_node_4 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 4"));

        Device cloud = cloudServerCreator.register(deviceConfiguration.withIdentifier("cloud-server"));

        Link.addUndirected(new LinkConfiguration().from(end_device_1).to(edge_node_1).withNameToFrom("LINK-1a").withNameFromTo("LINK-1b").withLatencyFromTo(10F).withLatencyToFrom(10F));
        Link.addUndirected(new LinkConfiguration().from(end_device_2).to(edge_node_4).withNameToFrom("LINK-2a").withNameFromTo("LINK-2b").withLatencyFromTo(5F).withLatencyToFrom(5F));

        Link.addDirected(new LinkConfiguration().from(edge_node_2).to(edge_node_4).withNameFromTo("LINK-3a").withLatencyFromTo(15F).withLatencyToFrom(15F));
        Link.addDirected(new LinkConfiguration().from(edge_node_4).to(edge_node_1).withNameFromTo("LINK-8a").withLatencyFromTo(45F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_1).to(edge_node_3).withNameFromTo("LINK-4a").withNameToFrom("LINK-4b").withLatencyFromTo(25F).withLatencyToFrom(95F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_2).to(edge_node_3).withNameToFrom("LINK-5a").withNameFromTo("LINK-5b").withLatencyFromTo(35F).withLatencyToFrom(35F));

        Link.addDirected(new LinkConfiguration().from(cloud).to(edge_node_2).withNameFromTo("LINK-6a").withLatencyFromTo(5F));
        Link.addDirected(new LinkConfiguration().from(edge_node_3).to(cloud).withNameFromTo("LINK-7a").withLatencyFromTo(10F));

        cloud.solveRoutes();
        System.out.println("Cloud Routes Solved");
        RoutePrinter.getInstance().printRoutes(cloud);

        edge_node_1.solveRoutes();
        System.out.println("Edge Node 1 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_1);

        edge_node_2.solveRoutes();
        System.out.println("Edge Node 2 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_2);

        edge_node_3.solveRoutes();
        System.out.println("Edge Node 3 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_3);

        edge_node_4.solveRoutes();
        System.out.println("Edge Node 4 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_4);

        end_device_1.solveRoutes();
        System.out.println("End Device 1 Routes Solved");
        RoutePrinter.getInstance().printRoutes(end_device_1);

        end_device_2.solveRoutes();
        System.out.println("End Device 2 Routes Solved");
        RoutePrinter.getInstance().printRoutes(end_device_2);
    }
}
