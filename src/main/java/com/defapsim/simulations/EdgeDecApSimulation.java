package com.defapsim.simulations;


import com.defapsim.algorithms.decentral.edgedecap.Auctioneer;
import com.defapsim.algorithms.decentral.edgedecap.Bidder;
import com.defapsim.algorithms.decentral.edgedecap.Status;
import com.defapsim.application.Component;
import com.defapsim.exceptions.ApplicationNeededException;
import com.defapsim.exceptions.DomainPolicyNeededException;
import com.defapsim.exceptions.InfrastructureNeededException;
import com.defapsim.exceptions.InitialPlacementPolicyNeededException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.devices.edgenode.EdgeNode;
import com.defapsim.misc.print.ComponentPrinter;
import com.defapsim.misc.print.ConsoleFormatter;
import com.defapsim.misc.print.InfrastructurePrinter;
import com.defapsim.misc.timer.Timer;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simulation of the EdgeDecAp algorithm
 */
public class EdgeDecApSimulation extends Simulation {

    // Devices in simulator do not hold information about the bidder since it is only for EdgeDecAp algorithm
    public static Map<ApplicationHostDevice, Bidder> bidderMap          = new LinkedHashMap<>();
    // Devices in simulator do not have a status field since it is only for EdgeDecAp algorithm
    public static Map<ApplicationHostDevice, Status> statusMap          = new LinkedHashMap<>();
    // Components in simulator do not have a Sweet-Spot field since it is only for EdgeDecAp algorithm
    public static Map<Component, Boolean> sweetSpotsDetermination       = new LinkedHashMap<>();
    // Devices in simulator do not hold information about the auctioneer since it is only for EdgeDecAp algorithm
    private Map<ApplicationHostDevice, Auctioneer> auctioneerMap        = new LinkedHashMap<>();
    private List<ApplicationHostDevice> applicationHostDevicesList      = new LinkedList<>();

    public static List<Float> results = new LinkedList<>();

    public boolean isBeingDebugged() {
        return isBeingDebugged;
    }

    private boolean isBeingDebugged;

    public EdgeDecApSimulation isBeingDebugged(boolean isBeingDebugged) {
        this.isBeingDebugged = isBeingDebugged;
        return this;
    }

    /**
     * Preparation before a EdgeDecAp simulation can be performed
     * This requires specifying an Infrastructure, an Application, a DomainPolicy, and an InitialPlacementPolicy.
     */
    @Override
    public void prepareSimulation() {

        bidderMap                   = new LinkedHashMap<>();
        auctioneerMap               = new LinkedHashMap<>();
        statusMap                   = new LinkedHashMap<>();
        sweetSpotsDetermination     = new LinkedHashMap<>();

        results                     = new LinkedList<>();

        this.applicationHostDevicesList = this.infrastructure.getDevices().stream()
                .filter(o -> o instanceof CloudServer || o instanceof EdgeNode)
                .map(ApplicationHostDevice.class::cast)
                .collect(Collectors.toList());

        if(this.infrastructure == null || this.infrastructure.getDevices().size() == 0)
            throw new InfrastructureNeededException("To perform a EdgeDecAp simulation an Infrastructure with at least 1 Device must be defined");

        if(this.applications == null || this.applications.size() == 0)
            throw new ApplicationNeededException("To perform a EdgeDecAp simulation an application must be defined");

        if(this.domainPolicy == null)
            throw new DomainPolicyNeededException("To perform a EdgeDecAp simulation a domain policy must be defined");

        if(this.initialPlacementPolicy == null)
            throw new InitialPlacementPolicyNeededException("To perform a EdgeDecAp simulation an initial placement policy must be defined");

        this.domainPolicy.createDomain(this.infrastructure);
        this.initialPlacementPolicy.placeApplication(this.infrastructure, this.applications.get(0));

        applicationHostDevicesList.forEach(device -> bidderMap.put(device, new Bidder()));
        applicationHostDevicesList.forEach(device -> auctioneerMap.put(device, new Auctioneer()));
        applicationHostDevicesList.forEach(device -> statusMap.put(device, Status.FREE));
        this.applications.get(0).getComponents().forEach(component -> sweetSpotsDetermination.put(component, false));

    }

    /**
     * The implementation of a EdgeDecAp simulation.
     * In order to run a simulation, it must first be prepared ( see prepareSimulation() ).
     */
    @Override
    public void startSimulation() {
        System.out.println("\n" +
                ConsoleFormatter.line((145 / 2) - (" [EdgeDecAp Simulation: Start] ".length()) / 2)
                + " [EdgeDecAp Simulation: Start] "
                + ConsoleFormatter.line((145 / 2) - (" [EdgeDecAp Simulation: Start] ".length()) / 2)
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
        InfrastructurePrinter.getInstance().printInfrastructureDevicesComponents(this.infrastructure);

        System.out.println("\nStarting EdgeDecAp simulation...");

        Timer t = new Timer();
        t.start();

        while(sweetSpotsDetermination.containsValue(false)) {
            for(ApplicationHostDevice applicationHostDevice : this.applicationHostDevicesList) {
                applicationHostDevice.algorithm(auctioneerMap.get(applicationHostDevice)).start(this, this.evaluation);
            }
        }

        t.stop();
        System.out.println("EdgeDecAp simulation ended");
        System.out.println("Simulation execution time: " + t.getTimeMS());
        System.out.println("\nFinal state:");
        System.out.println("\tApplication latency: " + applications.get(0).getApplicationLatency());

        if(this.evaluation != null) {
            this.evaluation.withApplicationLatencyAfterOptimization(applications.get(0).getApplicationLatency());
            this.evaluation.withExecutionTimeOfTheAlgorithm(t.getTimeInNanoseconds());
        }

        InfrastructurePrinter.getInstance().printInfrastructureDevicesComponents(this.infrastructure);

        System.out.println("\n" +
                ConsoleFormatter.line((145 / 2) - (" [EdgeDecAp Simulation: End] ".length()) / 2)
                + " [EdgeDecAp Simulation: End] "
                + ConsoleFormatter.line((145 / 2) - (" [EdgeDecAp Simulation: End] ".length()) / 2)
        );
    }
}
