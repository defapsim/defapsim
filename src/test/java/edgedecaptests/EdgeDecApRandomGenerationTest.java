package edgedecaptests;

import com.defapsim.application.Application;
import com.defapsim.application.Component;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.examples.simulationconfiguration.literature.Fire_IoT_Application;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.misc.Tupel;
import com.defapsim.policies.domain.DomainPolicy;
import com.defapsim.policies.domain.GlobalDomainPolicy;
import com.defapsim.policies.initialplacement.InitialPlacementPolicy;
import com.defapsim.policies.initialplacement.RandomInitialPlacementPolicy;
import com.defapsim.simulations.EdgeDecApSimulation;
import com.defapsim.simulations.Simulation;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

/**
 * In this test case, different problem instances are generated using the infrastructure and application from
 * Brogi et al. (see com.defapsim.examples.simulationconfiguration.literature.Fire_IoT_Application).
 * The initial placement is random. Afterwards, the EdgeDecAp algorithm is executed for each problem
 * instance. It is tested whether the application latency has improved or remained the same due
 * to the placement, which was done by the EdgeDecAp algorithm.
 */

public class EdgeDecApRandomGenerationTest {

    public static Infrastructure infrastructure;
    public static Application application;
    public static DomainPolicy domainPolicy;
    public static InitialPlacementPolicy initialPlacementPolicy;
    public static Float applicationLatency;

    @BeforeAll
    static void initializeSimulationConfiguration() {
        Tupel tupel = Fire_IoT_Application.getSimulationConfigurationTupel();
        
        infrastructure = (Infrastructure) tupel.getAttributeA();
        application    = (Application) tupel.getAttributeB();
        domainPolicy   = new GlobalDomainPolicy();
        initialPlacementPolicy = new RandomInitialPlacementPolicy();
    }

    @AfterEach
    void resetSimulationConfiguration() {
        Tupel tupel = Fire_IoT_Application.getSimulationConfigurationTupel();

        infrastructure = (Infrastructure) tupel.getAttributeA();
        application    = (Application) tupel.getAttributeB();
    }

    @RepeatedTest(200)
    void testOutputApplicationLatencyAlwaysHigherInputApplicationLatency() {

        Simulation edgeDecApSimulation = new EdgeDecApSimulation()
                .isBeingDebugged(true)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withEvaluation(new Evaluation())
                .withInitialPlacementPolicy(initialPlacementPolicy);

        edgeDecApSimulation.prepareSimulation();
        applicationLatency = application.getApplicationLatency();

        edgeDecApSimulation.startSimulation();
        assertThat(application.getApplicationLatency(), lessThanOrEqualTo(applicationLatency));

        if(EdgeDecApSimulation.results.size() > 0) {
            Float current = EdgeDecApSimulation.results.get(0);
            for(int i = 1; i < EdgeDecApSimulation.results.size(); i++) {
                assertThat(EdgeDecApSimulation.results.get(i), lessThanOrEqualTo(current + 0.1F));
                current = EdgeDecApSimulation.results.get(i);
            }
        }

        for(Component component : application.getComponents()) {
            assertThat(component.getHostBlacklist(), not(hasItem(component.getHostDevice())));

            for(Component componentsOnHostDevice : component.getHostDevice().getComponents()) {
                assertThat(component.getComponentBlacklist(), not(hasItem(componentsOnHostDevice)));
            }
        }
    }
}
