package ldspptest;

import com.defapsim.application.Application;
import com.defapsim.application.Component;
import com.defapsim.evaluation.problemInstancegenerator.ApplicationGenerator;
import com.defapsim.evaluation.problemInstancegenerator.InfrastructureGenerator;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.policies.initialplacement.InitialPlacementPolicy;
import com.defapsim.policies.initialplacement.RandomInitialPlacementPolicy;
import com.defapsim.simulations.LDSPPSimulation;
import com.defapsim.simulations.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

/**
 * In this test, the LDSPP is tested for its functionality in the first problem instance of the evaluation.
 */

public class LDSPPTest {

    public static InfrastructureGenerator infrastructureGenerator;

    public static Infrastructure infrastructure;

    public static ApplicationHostDevice cloudServer;
    public static ApplicationHostDevice pop1;
    public static ApplicationHostDevice pop2;
    public static ApplicationHostDevice pop3;

    public static ApplicationHostDevice box1;
    public static ApplicationHostDevice box2;
    public static ApplicationHostDevice box3;

    public static ApplicationHostDevice mobile1;
    public static ApplicationHostDevice mobile2;
    public static ApplicationHostDevice mobile3;
    public static ApplicationHostDevice pc;


    @BeforeEach
    public void init() {
        infrastructureGenerator = new InfrastructureGenerator()
                .withMinimumMemory(4.F).withMaximumMemory(16.F)
                .withMinimumComputingPower(2.F).withMaximumComputingPower(10.F)
                .withMinimumProcessingSpeed(1.9F).withMaximumProcessingSpeed(3.1F)
                .withMinimumCloudPoPLatency(30.F).withMaximumCloudPoPLatency(100.F)
                .withMinimumPoPPoPLatency(3.F).withMaximumPoPPoPLatency(7.F)
                .withMinimumBoxPoPLatency(1.F).withMaximumBoxPoPLatency(20.F)
                .withMinimumBoxEndDevicesLatency(1.F).withMaximumBoxEndDevicesLatency(2.F)
                .withSeed(95);

        infrastructure = infrastructureGenerator.build(true);

        cloudServer = (ApplicationHostDevice) infrastructure.getDevices().get(0);
        pop1 = (ApplicationHostDevice) infrastructure.getDevices().get(1);
        pop2 = (ApplicationHostDevice) infrastructure.getDevices().get(2);
        pop3 = (ApplicationHostDevice) infrastructure.getDevices().get(3);

        box1 = (ApplicationHostDevice) infrastructure.getDevices().get(4);
        box2 = (ApplicationHostDevice) infrastructure.getDevices().get(7);
        box3 = (ApplicationHostDevice) infrastructure.getDevices().get(11);

        mobile1 = (ApplicationHostDevice) infrastructure.getDevices().get(8);
        mobile2 = (ApplicationHostDevice) infrastructure.getDevices().get(13);
        mobile3 = (ApplicationHostDevice) infrastructure.getDevices().get(14);
        pc = (ApplicationHostDevice) infrastructure.getDevices().get(12);

    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     Box 3
     *  INSIGHTS_BACKEND            is placed on on     Box 3
     *  FIRE_MANAGER                is placed on on     Cloud-Server
     */
    @Test
    void testSimulationInstanceLDSPP_B3B3CS() {

        InitialPlacementPolicy initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(543);

        Application application = new ApplicationGenerator()
                .withWorstCaseExcecutionTimeMultiplier(1.F)
                .withOldDevices(infrastructureGenerator.getOldDevices())
                .withSeed(5401)
                .createApplication(infrastructure, 1);

        Component fire_manager = application.getComponents().get(0);
        Component insights_backend = application.getComponents().get(1);
        Component machine_learning_engine = application.getComponents().get(2);

        fire_manager.setMemoryDemand(10.F);
        machine_learning_engine.setMemoryDemand(2.F);
        machine_learning_engine.setComputingPowerDemand(2.F);


        // SIMULATION OF THE LDSPP
        Simulation LDSPPSimulation = new LDSPPSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        LDSPPSimulation.prepareSimulation();
        LDSPPSimulation.startSimulation();

        assertThat(pop2.getComponents(), hasItem(machine_learning_engine));
        assertThat(pop2.getComponents(), hasItem(insights_backend));
        assertThat(box3.getComponents(), hasItem(fire_manager));
    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     Cloud-Server
     *  INSIGHTS_BACKEND            is placed on on     PoP 1
     *  FIRE_MANAGER                is placed on on     PoP 1
     */
    @Test
    void testSimulationInstanceLDSPP_CSP1P1() {
        InitialPlacementPolicy initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(5453);

        Application application = new ApplicationGenerator()
                .withWorstCaseExcecutionTimeMultiplier(1.F)
                .withOldDevices(infrastructureGenerator.getOldDevices())
                .withSeed(5401)
                .createApplication(infrastructure, 1);

        Component fire_manager = application.getComponents().get(0);
        Component insights_backend = application.getComponents().get(1);
        Component machine_learning_engine = application.getComponents().get(2);

        // SIMULATION OF THE LDSPP
        infrastructure.resetInfrastructure();
        Simulation LDSPPSimulation = new LDSPPSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        LDSPPSimulation.prepareSimulation();
        LDSPPSimulation.startSimulation();

        assertThat(cloudServer.getComponents(), hasItem(machine_learning_engine));
        assertThat(pop1.getComponents(), hasItem(insights_backend));
        assertThat(box3.getComponents(), hasItem(fire_manager));

    }

    /**
     * Test with the following initial placement for the components:
     *  MACHINE_LEARNING_ENGINE     is placed on on     Box 1
     *  INSIGHTS_BACKEND            is placed on on     Box 1
     *  FIRE_MANAGER                is placed on on     PoP 1
     */
    @Test
    void testSimulationInstanceLDSPP_B1B1P1() {
        InitialPlacementPolicy initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(75);

        Application application = new ApplicationGenerator()
                .withWorstCaseExcecutionTimeMultiplier(1.F)
                .withOldDevices(infrastructureGenerator.getOldDevices())
                .withSeed(5401)
                .createApplication(infrastructure, 1);

        Component fire_manager = application.getComponents().get(0);
        Component insights_backend = application.getComponents().get(1);
        Component machine_learning_engine = application.getComponents().get(2);

        fire_manager.setMemoryDemand(10.F);

        // SIMULATION OF THE LDSPP
        Simulation LDSPPSimulation = new LDSPPSimulation()
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        LDSPPSimulation.prepareSimulation();
        LDSPPSimulation.startSimulation();

        assertThat(pop1.getComponents(), hasItem(machine_learning_engine));
        assertThat(pop2.getComponents(), hasItem(insights_backend));
        assertThat(box3.getComponents(), hasItem(fire_manager));
    }
}
