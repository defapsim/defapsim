package edgedecaptests;

import com.defapsim.application.Application;
import com.defapsim.application.Component;
import com.defapsim.examples.simulationconfiguration.literature.Fire_IoT_Application;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.misc.Tupel;
import com.defapsim.policies.domain.DomainPolicy;
import com.defapsim.policies.domain.HopDomainPolicy;
import com.defapsim.policies.initialplacement.InitialPlacementPolicy;
import com.defapsim.policies.initialplacement.RandomInitialPlacementPolicy;
import com.defapsim.simulations.EdgeDecApSimulation;
import com.defapsim.simulations.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

/**
 * In the following, six test cases are run through.
 * The EdgeDecAp algorithm is always executed on the same infrastructure.
 * The infrastructure and application was taken from Brogi et al (see
 * com.defapsim.examples.simulationconfiguration.literature.Fire_IoT_Application).
 * Only the initial placement of the components differs in each of the six tests.
 */

public class EdgeDecApInstancesTest {

    public static DomainPolicy domainPolicy = new HopDomainPolicy().withHops(2);

    public static Infrastructure infrastructure;
    public static Application application;
    public static InitialPlacementPolicy initialPlacementPolicy;
    public static Float applicationLatency;

    public static ApplicationHostDevice edgeNode1;
    public static ApplicationHostDevice edgeNode2;
    public static ApplicationHostDevice edgeNode3;
    public static ApplicationHostDevice cloudServer1;
    public static ApplicationHostDevice cloudServer2;

    public static Component fire_manager;
    public static Component insights_backend;
    public static Component machine_learning_engine;

    @BeforeEach
    void initDevicesAndComponents() {
        Tupel tupel             = Fire_IoT_Application.getSimulationConfigurationTupel();

        infrastructure          = (Infrastructure) tupel.getAttributeA();
        application             = (Application) tupel.getAttributeB();

        edgeNode1 = (ApplicationHostDevice) infrastructure.getDevices().get(4);
        edgeNode2 = (ApplicationHostDevice) infrastructure.getDevices().get(5);
        edgeNode3 = (ApplicationHostDevice) infrastructure.getDevices().get(6);
        cloudServer1            = (ApplicationHostDevice) infrastructure.getDevices().get(7);
        cloudServer2            = (ApplicationHostDevice) infrastructure.getDevices().get(8);

        fire_manager            = application.getComponents().get(0);
        insights_backend        = application.getComponents().get(1);
        machine_learning_engine = application.getComponents().get(2);

    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 2
     *  INSIGHTS_BACKEND            is placed on on     cloud server 2
     *  FIRE_MANAGER                is placed on on     cloud server 2
     */
    @Test
    void testSimulationInstanceEdgeDecAp_C2C2C2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(424);
        Simulation EdgeDecApSimulation = new EdgeDecApSimulation()
                .isBeingDebugged(true)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        EdgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        EdgeDecApSimulation.startSimulation();

        assertThat(edgeNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     edge node 3
     *  FIRE_MANAGER                is placed on on     cloud server 2
     */
    @Test
    void testSimulationInstanceEdgeDecAp_C1F3C2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(72696568);
        Simulation EdgeDecApSimulation = new EdgeDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        EdgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        EdgeDecApSimulation.startSimulation();

        assertThat(edgeNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     edge node 3
     *  FIRE_MANAGER                is placed on on     edge node 2
     */
    @Test
    void testSimulationInstanceEdgeDecAp_C1F3F2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(10611798);
        Simulation EdgeDecApSimulation = new EdgeDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        EdgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        EdgeDecApSimulation.startSimulation();

        assertThat(edgeNode1.getComponents(), hasItem(fire_manager));
        assertThat(edgeNode3.getComponents(), hasItem(insights_backend));
        assertThat(cloudServer1.getComponents(), hasItem(machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     edge node 3
     *  INSIGHTS_BACKEND            is placed on on     cloud server 2
     *  FIRE_MANAGER                is placed on on     edge node 1
     */
    @Test
    void testSimulationInstanceEdgeDecAp_F3C2F1() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(98106);
        Simulation EdgeDecApSimulation = new EdgeDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        EdgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        EdgeDecApSimulation.startSimulation();

        assertThat(edgeNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     edge node 3
     *  FIRE_MANAGER                is placed on on     edge node 1
     */
    @Test
    void testSimulationInstanceEdgeDecAp_C1F3F1() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(708268);
        Simulation EdgeDecApSimulation = new EdgeDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        EdgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        EdgeDecApSimulation.startSimulation();

        assertThat(edgeNode1.getComponents(), hasItem(fire_manager));
        assertThat(edgeNode3.getComponents(), hasItem(insights_backend));
        assertThat(cloudServer1.getComponents(), hasItem(machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     cloud server 2
     *  FIRE_MANAGER                is placed on on     edge node 2
     */
    @Test
    void testSimulationInstanceEdgeDecAp_C1C2F2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(535);
        Simulation EdgeDecApSimulation = new EdgeDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        EdgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        EdgeDecApSimulation.startSimulation();

        assertThat(edgeNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }
}
