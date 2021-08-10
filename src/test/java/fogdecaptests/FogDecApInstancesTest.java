package fogdecaptests;

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
import com.defapsim.simulations.FogDecApSimulation;
import com.defapsim.simulations.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

/**
 * In the following, six test cases are run through.
 * The FogDecAp algorithm is always executed on the same infrastructure.
 * The infrastructure and application was taken from Brogi et al (see
 * com.defapsim.examples.simulationconfiguration.literature.Fire_IoT_Application).
 * Only the initial placement of the components differs in each of the six tests.
 */

public class FogDecApInstancesTest {

    public static DomainPolicy domainPolicy = new HopDomainPolicy().withHops(2);

    public static Infrastructure infrastructure;
    public static Application application;
    public static InitialPlacementPolicy initialPlacementPolicy;
    public static Float applicationLatency;

    public static ApplicationHostDevice fogNode1;
    public static ApplicationHostDevice fogNode2;
    public static ApplicationHostDevice fogNode3;
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

        fogNode1                = (ApplicationHostDevice) infrastructure.getDevices().get(4);
        fogNode2                = (ApplicationHostDevice) infrastructure.getDevices().get(5);
        fogNode3                = (ApplicationHostDevice) infrastructure.getDevices().get(6);
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
    void testSimulationInstanceFogDecAp_C2C2C2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(424);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .isBeingDebugged(true)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        fogDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        fogDecApSimulation.startSimulation();

        assertThat(fogNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     fog node 3
     *  FIRE_MANAGER                is placed on on     cloud server 2
     */
    @Test
    void testSimulationInstanceFogDecAp_C1F3C2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(72696568);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        fogDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        fogDecApSimulation.startSimulation();

        assertThat(fogNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     fog node 3
     *  FIRE_MANAGER                is placed on on     fog node 2
     */
    @Test
    void testSimulationInstanceFogDecAp_C1F3F2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(10611798);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        fogDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        fogDecApSimulation.startSimulation();

        assertThat(fogNode1.getComponents(), hasItem(fire_manager));
        assertThat(fogNode3.getComponents(), hasItem(insights_backend));
        assertThat(cloudServer1.getComponents(), hasItem(machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     fog node 3
     *  INSIGHTS_BACKEND            is placed on on     cloud server 2
     *  FIRE_MANAGER                is placed on on     fog node 1
     */
    @Test
    void testSimulationInstanceFogDecAp_F3C2F1() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(98106);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        fogDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        fogDecApSimulation.startSimulation();

        assertThat(fogNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     fog node 3
     *  FIRE_MANAGER                is placed on on     fog node 1
     */
    @Test
    void testSimulationInstanceFogDecAp_C1F3F1() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(708268);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        fogDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        fogDecApSimulation.startSimulation();

        assertThat(fogNode1.getComponents(), hasItem(fire_manager));
        assertThat(fogNode3.getComponents(), hasItem(insights_backend));
        assertThat(cloudServer1.getComponents(), hasItem(machine_learning_engine));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     cloud server 1
     *  INSIGHTS_BACKEND            is placed on on     cloud server 2
     *  FIRE_MANAGER                is placed on on     fog node 2
     */
    @Test
    void testSimulationInstanceFogDecAp_C1C2F2() {
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(535);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);

        fogDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        fogDecApSimulation.startSimulation();

        assertThat(fogNode1.getComponents(), hasItem(fire_manager));
        assertThat(cloudServer2.getComponents(), hasItems(insights_backend, machine_learning_engine));
    }
}
