package fogdecaptests;

import com.defapsim.application.Application;
import com.defapsim.evaluation.problemInstancegenerator.ApplicationGenerator;
import com.defapsim.evaluation.problemInstancegenerator.InfrastructureGenerator;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.policies.domain.DomainPolicy;
import com.defapsim.policies.domain.GlobalDomainPolicy;
import com.defapsim.policies.domain.HopDomainPolicy;
import com.defapsim.policies.initialplacement.InitialPlacementPolicy;
import com.defapsim.policies.initialplacement.RandomInitialPlacementPolicy;
import com.defapsim.simulations.FogDecApSimulation;
import com.defapsim.simulations.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * In this test case, the FogDecAp algorithm was run with different domain policies in the same problem instance each time.
 * The problem instance is the problem instance from experiment 1. The following FogDecAp variants were tested:
 * FogDecAp with a 1-hop domain policy, 2-hop domain policy, 3-hop domain policy, global domain policy. It is tested
 * whether the application latency has improved or remained the same due to the placement, which was done by
 * the FogDecAp algorithm.
 */

public class FogDecApEvaluationTest {

    public static Infrastructure infrastructure;
    public static Application application;
    public static InitialPlacementPolicy initialPlacementPolicy;

    @BeforeEach
    void initSimulationEnvironment() {
        InfrastructureGenerator infrastructureGenerator = new InfrastructureGenerator()
                .withMinimumMemory(4.F).withMaximumMemory(16.F)
                .withMinimumComputingPower(2.F).withMaximumComputingPower(10.F)
                .withMinimumProcessingSpeed(1.9F).withMaximumProcessingSpeed(3.1F)
                .withMinimumCloudPoPLatency(30.F).withMaximumCloudPoPLatency(100.F)
                .withMinimumPoPPoPLatency(3.F).withMaximumPoPPoPLatency(7.F)
                .withMinimumBoxPoPLatency(1.F).withMaximumBoxPoPLatency(20.F)
                .withMinimumBoxEndDevicesLatency(1.F).withMaximumBoxEndDevicesLatency(2.F);

        infrastructure = infrastructureGenerator.createPhase2(true);
        application = new ApplicationGenerator().createApplication(infrastructure, 12);
        initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(361);
    }

    @Test
    void testFogDecApWith1HopDomainPolicy() {

        // SIMULATION OF THE FogDecAp ALGORITHM 1 HOP DOMAIN
        DomainPolicy domainPolicy = new HopDomainPolicy().withHops(1);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .isBeingDebugged(false)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        fogDecApSimulation.prepareSimulation();
        fogDecApSimulation.startSimulation();

        if(FogDecApSimulation.results.size() > 0) {
            Float current = FogDecApSimulation.results.get(0);
            for(int i = 1; i < FogDecApSimulation.results.size(); i++) {
                assertThat(FogDecApSimulation.results.get(i), lessThanOrEqualTo(current + 0.1F));
                current = FogDecApSimulation.results.get(i);
            }
        }
    }

    @Test
    void testFogDecApWith2HopDomainPolicy() {

        // SIMULATION OF THE FogDecAp ALGORITHM 2 HOP DOMAIN
        DomainPolicy domainPolicy = new HopDomainPolicy().withHops(2);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .isBeingDebugged(false)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        fogDecApSimulation.prepareSimulation();
        fogDecApSimulation.startSimulation();

        if(FogDecApSimulation.results.size() > 0) {
            Float current = FogDecApSimulation.results.get(0);
            for(int i = 1; i < FogDecApSimulation.results.size(); i++) {
                assertThat(FogDecApSimulation.results.get(i), lessThanOrEqualTo(current + 0.1F));
                current = FogDecApSimulation.results.get(i);
            }
        }
    }

    @Test
    void testFogDecApWith3HopDomainPolicy() {

        // SIMULATION OF THE FogDecAp ALGORITHM 3 HOP DOMAIN
        DomainPolicy domainPolicy = new HopDomainPolicy().withHops(3);
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .isBeingDebugged(false)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        fogDecApSimulation.prepareSimulation();
        fogDecApSimulation.startSimulation();

        if(FogDecApSimulation.results.size() > 0) {
            Float current = FogDecApSimulation.results.get(0);
            for(int i = 1; i < FogDecApSimulation.results.size(); i++) {
                assertThat(FogDecApSimulation.results.get(i), lessThanOrEqualTo(current + 0.1F));
                current = FogDecApSimulation.results.get(i);
            }
        }
    }

    @Test
    void testFogDecApWithGlobalHopDomainPolicy() {

        // SIMULATION OF THE FogDecAp ALGORITHM GLOBAL DOMAIN
        DomainPolicy domainPolicy = new GlobalDomainPolicy();
        Simulation fogDecApSimulation = new FogDecApSimulation()
                .isBeingDebugged(false)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        fogDecApSimulation.prepareSimulation();
        fogDecApSimulation.startSimulation();

        if(FogDecApSimulation.results.size() > 0) {
            Float current = FogDecApSimulation.results.get(0);
            for(int i = 1; i < FogDecApSimulation.results.size(); i++) {
                assertThat(FogDecApSimulation.results.get(i), lessThanOrEqualTo(current + 0.1F));
                current = FogDecApSimulation.results.get(i);
            }
        }
    }
}
