package routesolvertest;

import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.DeviceCreator;
import com.defapsim.infrastructure.devices.clouddevice.CloudServerCreator;
import com.defapsim.infrastructure.devices.enddevice.EndDeviceCreator;
import com.defapsim.infrastructure.devices.edgenode.EdgeNodeCreator;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.LinkConfiguration;
import com.defapsim.infrastructure.links.Route;
import com.defapsim.misc.print.RoutePrinter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.assertj.core.api.Assertions;

/**
 * This test case checks if the RouteSolver resolves the routes between devices correctly
 */

public class RouteSolverTest {

    public static Device end_device_1;
    public static Device end_device_2;

    public static Device edge_node_1;
    public static Device edge_node_2;
    public static Device edge_node_3;
    public static Device edge_node_4;

    public static Device cloud;

    @BeforeAll
    static void initDjkstraRouteSolverTest() {
        final DeviceCreator endDeviceCreator = new EndDeviceCreator();
        final EdgeNodeCreator edgeNodeCreator = new EdgeNodeCreator();
        final CloudServerCreator cloudServerCreator = new CloudServerCreator();

        Infrastructure infrastructure = new Infrastructure();

        DeviceConfiguration deviceConfiguration = new DeviceConfiguration().withComputingPower(20.F)
                .withMemory(30.F).withStorage(90.F).withInfrastructure(infrastructure);

        end_device_1 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 1"));
        end_device_2 = endDeviceCreator.register(deviceConfiguration.withIdentifier("end-device 2"));

        edge_node_1 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 1"));
        edge_node_2 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 2"));
        edge_node_3 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 3"));
        edge_node_4 = edgeNodeCreator.register(deviceConfiguration.withIdentifier("edge-node 4"));

        cloud = cloudServerCreator.register(deviceConfiguration.withIdentifier("cloud-server"));

        Link.addUndirected(new LinkConfiguration().from(end_device_1).to(edge_node_1).withNameToFrom("LINK-1a").withNameFromTo("LINK-1b").withLatencyFromTo(10F).withLatencyToFrom(10F));
        Link.addUndirected(new LinkConfiguration().from(end_device_2).to(edge_node_4).withNameToFrom("LINK-2a").withNameFromTo("LINK-2b").withLatencyFromTo(5F).withLatencyToFrom(5F));

        Link.addDirected(new LinkConfiguration().from(edge_node_2).to(edge_node_4).withNameFromTo("LINK-3a").withLatencyFromTo(15F).withLatencyToFrom(15F));
        Link.addDirected(new LinkConfiguration().from(edge_node_4).to(edge_node_1).withNameFromTo("LINK-8a").withLatencyFromTo(45F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_1).to(edge_node_3).withNameFromTo("LINK-4a").withNameToFrom("LINK-4b").withLatencyFromTo(25F).withLatencyToFrom(95F));
        Link.addUndirected(new LinkConfiguration().from(edge_node_2).to(edge_node_3).withNameToFrom("LINK-5a").withNameFromTo("LINK-5b").withLatencyFromTo(35F).withLatencyToFrom(35F));

        Link.addDirected(new LinkConfiguration().from(edge_node_3).to(cloud).withNameFromTo("LINK-7a").withLatencyFromTo(10F));
        Link.addDirected(new LinkConfiguration().from(cloud).to(edge_node_2).withNameFromTo("LINK-6a").withLatencyFromTo(5F));
    }

    @Test
    void testDjkstraRouteSolverCloudServer() {

        cloud.solveRoutes();
        System.out.println("Cloud Routes Solved");
        RoutePrinter.getInstance().printRoutes(cloud);

        //EDGE-NODE 1
        Assertions.assertThat(cloud.getRouteTo(edge_node_1))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(cloud.getRouteTo(edge_node_1))
                .extracting(Route::getLatency)
                .isEqualTo(65.0F);

        //EDGE-NODE 2
        Assertions.assertThat(cloud.getRouteTo(edge_node_2))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(cloud.getRouteTo(edge_node_2))
                .extracting(Route::getLatency)
                .isEqualTo(5.0F);

        //EDGE-NODE 3
        Assertions.assertThat(cloud.getRouteTo(edge_node_3))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(cloud.getRouteTo(edge_node_3))
                .extracting(Route::getLatency)
                .isEqualTo(40.0F);

        //EDGE-NODE 4
        Assertions.assertThat(cloud.getRouteTo(edge_node_4))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(cloud.getRouteTo(edge_node_4))
                .extracting(Route::getLatency)
                .isEqualTo(20.0F);

        //END-DEVICE 1
        Assertions.assertThat(cloud.getRouteTo(end_device_1))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(cloud.getRouteTo(end_device_1))
                .extracting(Route::getLatency)
                .isEqualTo(75.0F);

        //END-DEVICE 2
        Assertions.assertThat(cloud.getRouteTo(end_device_2))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(cloud.getRouteTo(end_device_2))
                .extracting(Route::getLatency)
                .isEqualTo(25.0F);
    }

    @Test
    void testDjkstraRouteSolverEdgeNode1() {

        edge_node_1.solveRoutes();
        System.out.println("Edge-Node 1 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_1);

        //CLOUD-SERVER
        Assertions.assertThat(edge_node_1.getRouteTo(cloud))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_1.getRouteTo(cloud))
                .extracting(Route::getLatency)
                .isEqualTo(35.0F);

        //EDGE-NODE 2
        Assertions.assertThat(edge_node_1.getRouteTo(edge_node_2))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(edge_node_1.getRouteTo(edge_node_2))
                .extracting(Route::getLatency)
                .isEqualTo(40.0F);

        //EDGE-NODE 3
        Assertions.assertThat(edge_node_1.getRouteTo(edge_node_3))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_1.getRouteTo(edge_node_3))
                .extracting(Route::getLatency)
                .isEqualTo(25.0F);

        //EDGE-NODE 4
        Assertions.assertThat(edge_node_1.getRouteTo(edge_node_4))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(edge_node_1.getRouteTo(edge_node_4))
                .extracting(Route::getLatency)
                .isEqualTo(55.0F);

        //END-DEVICE 1
        Assertions.assertThat(edge_node_1.getRouteTo(end_device_1))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_1.getRouteTo(end_device_1))
                .extracting(Route::getLatency)
                .isEqualTo(10.0F);

        //END-DEVICE 2
        Assertions.assertThat(edge_node_1.getRouteTo(end_device_2))
                .extracting(Route::getHops)
                .asList().hasSize(5);

        Assertions.assertThat(edge_node_1.getRouteTo(end_device_2))
                .extracting(Route::getLatency)
                .isEqualTo(60.0F);
    }


    @Test
    void testDjkstraRouteSolverEdgeNode2() {

        edge_node_2.solveRoutes();
        System.out.println("Edge-Node 2 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_2);

        //CLOUD-SERVER
        Assertions.assertThat(edge_node_2.getRouteTo(cloud))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_2.getRouteTo(cloud))
                .extracting(Route::getLatency)
                .isEqualTo(45.0F);

        //Edge-NODE 1
        Assertions.assertThat(edge_node_2.getRouteTo(edge_node_1))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_2.getRouteTo(edge_node_1))
                .extracting(Route::getLatency)
                .isEqualTo(60.0F);

        //EDGE-NODE 3
        Assertions.assertThat(edge_node_2.getRouteTo(edge_node_3))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_2.getRouteTo(edge_node_3))
                .extracting(Route::getLatency)
                .isEqualTo(35.0F);

        //EDGE-NODE 4
        Assertions.assertThat(edge_node_2.getRouteTo(edge_node_4))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_2.getRouteTo(edge_node_4))
                .extracting(Route::getLatency)
                .isEqualTo(15.0F);

        //END-DEVICE 1
        Assertions.assertThat(edge_node_2.getRouteTo(end_device_1))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(edge_node_2.getRouteTo(end_device_1))
                .extracting(Route::getLatency)
                .isEqualTo(70.0F);

        //END-DEVICE 2
        Assertions.assertThat(edge_node_2.getRouteTo(end_device_2))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_2.getRouteTo(end_device_2))
                .extracting(Route::getLatency)
                .isEqualTo(20.0F);
    }

    @Test
    void testDjkstraRouteSolverEdgeNode3() {

        edge_node_3.solveRoutes();
        System.out.println("Edge-Node 3 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_3);

        //CLOUD-SERVER
        Assertions.assertThat(edge_node_3.getRouteTo(cloud))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_3.getRouteTo(cloud))
                .extracting(Route::getLatency)
                .isEqualTo(10.0F);

        //EDGE-NODE 1
        Assertions.assertThat(edge_node_3.getRouteTo(edge_node_1))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(edge_node_3.getRouteTo(edge_node_1))
                .extracting(Route::getLatency)
                .isEqualTo(75.0F);

        //EDGE-NODE 2
        Assertions.assertThat(edge_node_3.getRouteTo(edge_node_2))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_3.getRouteTo(edge_node_2))
                .extracting(Route::getLatency)
                .isEqualTo(15.0F);

        //EDGE-NODE 4
        Assertions.assertThat(edge_node_3.getRouteTo(edge_node_4))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(edge_node_3.getRouteTo(edge_node_4))
                .extracting(Route::getLatency)
                .isEqualTo(30.0F);

        //END-DEVICE 1
        Assertions.assertThat(edge_node_3.getRouteTo(end_device_1))
                .extracting(Route::getHops)
                .asList().hasSize(5);

        Assertions.assertThat(edge_node_3.getRouteTo(end_device_1))
                .extracting(Route::getLatency)
                .isEqualTo(85.0F);

        //END-DEVICE 2
        Assertions.assertThat(edge_node_3.getRouteTo(end_device_2))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(edge_node_3.getRouteTo(end_device_2))
                .extracting(Route::getLatency)
                .isEqualTo(35.0F);
    }

    @Test
    void testDjkstraRouteSolverEdgeNode4() {

        edge_node_4.solveRoutes();
        System.out.println("Edge-Node 4 Routes Solved");
        RoutePrinter.getInstance().printRoutes(edge_node_4);

        //CLOUD-SERVER
        Assertions.assertThat(edge_node_4.getRouteTo(cloud))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(edge_node_4.getRouteTo(cloud))
                .extracting(Route::getLatency)
                .isEqualTo(80.0F);

        //EDGE-NODE 1
        Assertions.assertThat(edge_node_4.getRouteTo(edge_node_1))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_4.getRouteTo(edge_node_1))
                .extracting(Route::getLatency)
                .isEqualTo(45.0F);

        //EDGE-NODE 2
        Assertions.assertThat(edge_node_4.getRouteTo(edge_node_2))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(edge_node_4.getRouteTo(edge_node_2))
                .extracting(Route::getLatency)
                .isEqualTo(85.0F);

        //EDGE-NODE 3
        Assertions.assertThat(edge_node_4.getRouteTo(edge_node_3))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_4.getRouteTo(edge_node_3))
                .extracting(Route::getLatency)
                .isEqualTo(70.0F);

        //END-DEVICE 1
        Assertions.assertThat(edge_node_4.getRouteTo(end_device_1))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(edge_node_4.getRouteTo(end_device_1))
                .extracting(Route::getLatency)
                .isEqualTo(55.0F);

        //END-DEVICE 2
        Assertions.assertThat(edge_node_4.getRouteTo(end_device_2))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(edge_node_4.getRouteTo(end_device_2))
                .extracting(Route::getLatency)
                .isEqualTo(5.0F);
    }

    @Test
    void testDjkstraRouteSolverEndDevice1() {

        end_device_1.solveRoutes();
        System.out.println("End-Device 1 Routes Solved");
        RoutePrinter.getInstance().printRoutes(end_device_1);

        //CLOUD-SERVER
        Assertions.assertThat(end_device_1.getRouteTo(cloud))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(end_device_1.getRouteTo(cloud))
                .extracting(Route::getLatency)
                .isEqualTo(45.0F);

        //EDGE-NODE 1
        Assertions.assertThat(end_device_1.getRouteTo(edge_node_1))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(end_device_1.getRouteTo(edge_node_1))
                .extracting(Route::getLatency)
                .isEqualTo(10.0F);

        //EDGE-NODE 2
        Assertions.assertThat(end_device_1.getRouteTo(edge_node_2))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(end_device_1.getRouteTo(edge_node_2))
                .extracting(Route::getLatency)
                .isEqualTo(50.0F);

        //EDGE-NODE 3
        Assertions.assertThat(end_device_1.getRouteTo(edge_node_3))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(end_device_1.getRouteTo(edge_node_3))
                .extracting(Route::getLatency)
                .isEqualTo(35.0F);

        //EDGE-NODE 4
        Assertions.assertThat(end_device_1.getRouteTo(edge_node_4))
                .extracting(Route::getHops)
                .asList().hasSize(5);

        Assertions.assertThat(end_device_1.getRouteTo(edge_node_4))
                .extracting(Route::getLatency)
                .isEqualTo(65.0F);

        //END-DEVICE 2
        Assertions.assertThat(end_device_1.getRouteTo(end_device_2))
                .extracting(Route::getHops)
                .asList().hasSize(6);

        Assertions.assertThat(end_device_1.getRouteTo(end_device_2))
                .extracting(Route::getLatency)
                .isEqualTo(70.0F);
    }

    @Test
    void testDjkstraRouteSolverEndDevice2() {

        end_device_2.solveRoutes();
        System.out.println("End-Device 2 Routes Solved");
        RoutePrinter.getInstance().printRoutes(end_device_2);

        //CLOUD-SERVER
        Assertions.assertThat(end_device_2.getRouteTo(cloud))
                .extracting(Route::getHops)
                .asList().hasSize(4);

        Assertions.assertThat(end_device_2.getRouteTo(cloud))
                .extracting(Route::getLatency)
                .isEqualTo(85.0F);

        //EDGE-NODE 1
        Assertions.assertThat(end_device_2.getRouteTo(edge_node_1))
                .extracting(Route::getHops)
                .asList().hasSize(2);

        Assertions.assertThat(end_device_2.getRouteTo(edge_node_1))
                .extracting(Route::getLatency)
                .isEqualTo(50.0F);

        //EDGE-NODE 2
        Assertions.assertThat(end_device_2.getRouteTo(edge_node_2))
                .extracting(Route::getHops)
                .asList().hasSize(5);

        Assertions.assertThat(end_device_2.getRouteTo(edge_node_2))
                .extracting(Route::getLatency)
                .isEqualTo(90.0F);

        //EDGE-NODE 3
        Assertions.assertThat(end_device_2.getRouteTo(edge_node_3))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(end_device_2.getRouteTo(edge_node_3))
                .extracting(Route::getLatency)
                .isEqualTo(75.0F);

        //EDGE-NODE 4
        Assertions.assertThat(end_device_2.getRouteTo(edge_node_4))
                .extracting(Route::getHops)
                .asList().hasSize(1);

        Assertions.assertThat(end_device_2.getRouteTo(edge_node_4))
                .extracting(Route::getLatency)
                .isEqualTo(5.0F);

        //END-DEVICE 1
        Assertions.assertThat(end_device_2.getRouteTo(end_device_1))
                .extracting(Route::getHops)
                .asList().hasSize(3);

        Assertions.assertThat(end_device_2.getRouteTo(end_device_1))
                .extracting(Route::getLatency)
                .isEqualTo(60.0F);
    }
}
