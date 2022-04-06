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

public class Routing1 {

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

        Device cloud = cloudServerCreator.register(deviceConfiguration.withIdentifier("cloud-server"));

        Link.addUndirected(new LinkConfiguration().from(end_device_1).to(edge_node_1).withNameToFrom("LINK-1a").withNameFromTo("LINK-1b").withLatencyFromTo(10.F).withLatencyToFrom(10.F));
        Link.addUndirected(new LinkConfiguration().from(end_device_2).to(edge_node_1).withNameToFrom("LINK-2a").withNameFromTo("LINK-2b").withLatencyFromTo(5.F).withLatencyToFrom(5.F));

        Link.addUndirected(new LinkConfiguration().from(edge_node_1).to(edge_node_2).withNameToFrom("LINK-3a").withNameFromTo("LINK-3b").withLatencyFromTo(15.F).withLatencyToFrom(15.F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_1).to(edge_node_3).withNameToFrom("LINK-4a").withNameFromTo("LINK-4b").withLatencyFromTo(25.F).withLatencyToFrom(25.F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_2).to(edge_node_3).withNameToFrom("LINK-5a").withNameFromTo("LINK-5b").withLatencyFromTo(35.F).withLatencyToFrom(35.F));

        Link.addUndirected(new LinkConfiguration().from(edge_node_2).to(cloud).withNameToFrom("LINK-6a").withNameFromTo("LINK-6b").withLatencyFromTo(5.F).withLatencyToFrom(5.F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_3).to(cloud).withNameToFrom("LINK-7a").withNameFromTo("LINK-7b").withLatencyFromTo(10.F).withLatencyToFrom(10.F));

        // In the following, the routes are resolved individually for each device
        // Alternatively, each device should have been put into an infrastructure. Then you could resolve all routes
        // of the devices within the infrastructure by calling the method "resolveRoutes()" (see example
        // "LinkCreationExample").

        // Resolve the routes for the cloud server and print them in the console
        cloud.solveRoutes();
        System.out.println("Cloud Routes Solved");
        RoutePrinter.getInstance().printRoutes(cloud);

        // Resolve the routes for edge node 1 and print them in the console
        edge_node_1.solveRoutes();
        System.out.println("Edge Node 1 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_1);

        // Resolve the routes for edge node 2 and print them in the console
        edge_node_2.solveRoutes();
        System.out.println("Edge Node 2 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_2);

        // Resolve the routes for edge node 3 and print them in the console
        edge_node_3.solveRoutes();
        System.out.println("Edge Node 3 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_3);

    }
}
