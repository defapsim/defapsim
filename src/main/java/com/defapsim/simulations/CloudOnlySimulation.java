package com.defapsim.simulations;

import com.defapsim.algorithms.central.CloudOnlyPlacement;
import com.defapsim.exceptions.ApplicationNeededException;
import com.defapsim.exceptions.InfrastructureNeededException;
import com.defapsim.exceptions.InitialPlacementPolicyNeededException;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.devices.edgenode.EdgeNode;
import com.defapsim.misc.print.ComponentPrinter;
import com.defapsim.misc.print.ConsoleFormatter;
import com.defapsim.misc.print.InfrastructurePrinter;
import com.defapsim.misc.timer.Timer;

/**
 * The CloudOnly algorithm migrates all components to the cloud
 */
public class CloudOnlySimulation extends Simulation {

    /**
     * Preparation before a CloudOnly simulation can be performed
     * This requires specifying an Infrastructure, an Application and an InitialPlacementPolicy.
     */
    @Override
    public void prepareSimulation() {

        if(this.infrastructure == null || this.infrastructure.getDevices().size() == 0)
            throw new InfrastructureNeededException("To perform a CloudOnly simulation an Infrastructure with at least 1 Device must be defined");

        if(this.applications == null || this.applications.size() == 0)
            throw new ApplicationNeededException("To perform a CloudOnly simulation an application must be defined");

        if(this.initialPlacementPolicy == null)
            throw new InitialPlacementPolicyNeededException("To perform a CloudOnly simulation an initial placement policy must be defined");

        this.initialPlacementPolicy.placeApplication(this.infrastructure, this.applications.get(0));

    }

    /**
     * The implementation of a CloudOnly simulation.
     * In order to run a simulation, it must first be prepared ( see prepareSimulation() ).
     */
    @Override
    public void startSimulation() {

        System.out.println("\n" +
                ConsoleFormatter.line((145 / 2) - (" [CloudOnly Simulation: Start] ".length()) / 2)
                        + " [CloudOnly Simulation: Start] "
                        + ConsoleFormatter.line((145 / 2) - (" [CloudOnly Simulation: Start] ".length()) / 2)
        );
        System.out.println("Inital state:");
        System.out.println("\tInitial application latency: " + applications.get(0).getApplicationLatency());

        if(this.evaluation != null) {
            this.evaluation.withApplicationComponents(this.applications.get(0).getComponents().size());
            this.evaluation.withCloudServers((int) this.infrastructure.getDevices().stream().filter(o -> o instanceof CloudServer).count());
            this.evaluation.withEdgeNodes((int) this.infrastructure.getDevices().stream().filter(o -> o instanceof EdgeNode).count());
            this.evaluation.withEndDevices((int) this.infrastructure.getDevices().stream().filter(o -> o instanceof EndDevice).count());
            this.evaluation.withApplicationLatencyInitial(applications.get(0).getApplicationLatency());
        }

        InfrastructurePrinter.getInstance().printInfrastructureDevicesConfiguration(this.infrastructure);
        ComponentPrinter.getInstance().printComponentConfigurationList(applications.get(0).getComponents());
        //InfrastructurePrinter.getInstance().printInfrastructureDevicesComponents(this.infrastructure);

        System.out.println("\nStarting CloudOnly simulation...");

        Timer t = new Timer();
        t.start();

        this.infrastructure.getDevices().get(0).algorithm(new CloudOnlyPlacement()).start(this.applications, this.evaluation);

        t.stop();
        System.out.println("CloudOnly simulation ended");
        System.out.println("Simulation execution time: " + t.getTimeMS());
        System.out.println("\nFinal state:");
        System.out.println("\tApplication latency: " + applications.get(0).getApplicationLatency());

        if(this.evaluation != null) {
            this.evaluation.withApplicationLatencyAfterOptimization(applications.get(0).getApplicationLatency());
            this.evaluation.withExecutionTimeOfTheAlgorithm(t.getTimeInNanoseconds());
        }

        //InfrastructurePrinter.getInstance().printInfrastructureDevicesComponents(this.infrastructure);

        System.out.println("\n" +
                ConsoleFormatter.line((145 / 2) - (" [CloudOnly Simulation: End] ".length()) / 2)
                + " [CloudOnly Simulation: End] "
                + ConsoleFormatter.line((145 / 2) - (" [CloudOnly Simulation: End] ".length()) / 2)
        );
    }
}
