package com.defapsim.simulations;

import com.defapsim.algorithms.decentral.ldspp.LDSPP;
import com.defapsim.application.Component;
import com.defapsim.application.Connector;
import com.defapsim.exceptions.ApplicationNeededException;
import com.defapsim.exceptions.InfrastructureNeededException;
import com.defapsim.exceptions.InitialPlacementPolicyNeededException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.devices.fognode.FogNode;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.misc.print.ConsoleFormatter;
import com.defapsim.misc.print.InfrastructurePrinter;
import com.defapsim.misc.timer.Timer;

import java.util.LinkedList;
import java.util.List;

/**
 * A simulation of the "Lightweight Decentralized Service Placement Policy" (LDSPP) from Guerrero et al.
 * Carlos Guerrero, Isaac Lera, and Carlos Juiz. A lightweight decentralized service placement policy
 * for performance optimization in fog computing. Journal of Ambient Intelligence and Humanized
 * Computing, 10(6):2435–2452, 2019.
 */
public class LDSPPSimulation extends Simulation {

    public static List<ApplicationHostDevice> boxes       = new LinkedList<>();

    /**
     * Preparation before a LDSPP simulation can be performed
     * This requires specifying an Infrastructure, an Application, and an InitialPlacementPolicy.
     */
    @Override
    public void prepareSimulation() {

        if(this.infrastructure == null || this.infrastructure.getDevices().size() == 0)
            throw new InfrastructureNeededException("To perform a LDSPP simulation an Infrastructure with at least 1 Device must be defined");

        if(this.applications == null || this.applications.size() == 0)
            throw new ApplicationNeededException("To perform a LDSPP simulation an application must be defined");

        if(this.initialPlacementPolicy == null)
            throw new InitialPlacementPolicyNeededException("To perform a LDSPP simulation an initial placement policy must be defined");
        this.initialPlacementPolicy.placeApplication(this.infrastructure, this.applications.get(0));

        LDSPPSimulation.boxes.clear();

        for(Device device : this.infrastructure.getDevices()) {
            if(device.getIdentifier().contains("Box")) {
                LDSPPSimulation.boxes.add((ApplicationHostDevice) device);
            }
        }
    }

    /**
     * The implementation of a LDSPP simulation.
     * In order to run a LDSPP simulation, it must first be prepared ( see prepareSimulation() ).
     */
    @Override
    public void startSimulation() {
        System.out.println("\n" +
                ConsoleFormatter.line((145 / 2) - (" [LDSPP Simulation: Start] ".length()) / 2)
                        + " [LDSPP Simulation: Start] "
                        + ConsoleFormatter.line((145 / 2) - (" [LDSPP Simulation: Start] ".length()) / 2)
        );
        System.out.println("Inital state:");
        System.out.println("\tInitial application latency: " + applications.get(0).getApplicationLatency());

        if(this.evaluation != null) {
            this.evaluation.withApplicationComponents(this.applications.get(0).getComponents().size());
            this.evaluation.withCloudServers((int) this.infrastructure.getDevices().stream().filter(o -> o instanceof CloudServer).count());
            this.evaluation.withFogNodes((int) this.infrastructure.getDevices().stream().filter(o -> o instanceof FogNode).count());
            this.evaluation.withEndDevices((int) this.infrastructure.getDevices().stream().filter(o -> o instanceof EndDevice).count());
            this.evaluation.withApplicationLatencyInitial(applications.get(0).getApplicationLatency());
        }

        InfrastructurePrinter.getInstance().printInfrastructureDevicesConfiguration(this.infrastructure);
        //ComponentPrinter.getInstance().printComponentConfigurationList(applications.get(0).getComponents());
        InfrastructurePrinter.getInstance().printInfrastructureDevicesComponents(this.infrastructure);

        System.out.println("\nStarting LDSPP simulation...");

        Timer t = new Timer();
        t.start();

        for(ApplicationHostDevice device : boxes) {
            for(Link link : device.getLinks()) {
                if(link.getTarget() instanceof EndDevice) {
                    EndDevice target = (EndDevice) link.getTarget();
                    for(Connector connector : target.getConnectors()) {
                        device.algorithm(new LDSPP()).start(((Component)connector.getTarget()), this.evaluation);
                    }
                }
            }
        }

        t.stop();
        System.out.println("LDSPP simulation ended");
        System.out.println("Simulation execution time: " + t.getTimeMS());
        System.out.println("\nFinal state:");
        System.out.println("\tApplication latency: " + applications.get(0).getApplicationLatency());

        if(this.evaluation != null) {
            this.evaluation.withApplicationLatencyAfterOptimization(applications.get(0).getApplicationLatency());
            this.evaluation.withExecutionTimeOfTheAlgorithm(t.getTimeInNanoseconds());
        }

        InfrastructurePrinter.getInstance().printInfrastructureDevicesComponents(this.infrastructure);

        System.out.println("\n" +
                ConsoleFormatter.line((145 / 2) - (" [LDSPP Simulation: End] ".length()) / 2)
                + " [LDSPP Simulation: End] "
                + ConsoleFormatter.line((145 / 2) - (" [LDSPP Simulation: End] ".length()) / 2)
        );
    }
}
