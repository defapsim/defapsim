package com.defapsim.infrastructure.devices;

import com.defapsim.algorithms.PlacementAlgorithm;
import com.defapsim.algorithms.decentral.RouteSolver;
import com.defapsim.exceptions.AlreadyInDomainException;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.Route;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The abstract class Device specifies the properties common to all devices within the infrastructure.
 */
public abstract class Device {

    /**
     * Identifier is specified as a string to identify the device.
     */
    private String identifier = "Generic Device";

    /**
     * The infrastructure to which the device belongs
     */
    private Infrastructure infrastructure;

    /**
     * ram is specified as a positive floating point value.
     */
    private Float memory = 0.F;

    /**
     * storage is specified as a positive floating point value.
     */
    private Float storage = 0.F;

    /**
     * processingPower is specified as a positive floating point value.
     */
    private Float computingPower = 0.F;

    /**
     * processingSpeed is specified as a positive floating point value.
     */
    private Float processingSpeed = 0.F;

    /**
     * The routes to the other devices in the network (if they can be reached by a link).
     */
    private List<Route> routes = new LinkedList<>();

    /**
     * The links to the connected devices
     */
    private List<Link> links = new LinkedList<>();

    /**
     * List of all devices that are in the domain
     */
    private List<Device> domainDevices = new ArrayList<>();

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public Infrastructure getInfrastructure() {
        return this.infrastructure;
    }

    public Float getMemory() {
        return this.memory;
    }

    public Float getStorage() {
        return this.storage;
    }

    public Float getComputingPower() {
        return this.computingPower;
    }

    public Float getProcessingSpeed() {
        return processingSpeed;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<Device> getDevicesInDomain() {
        return this.domainDevices;
    }

    /**
     * Setter
     */

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setInfrastructure(Infrastructure infrastructure) {
        this.infrastructure = infrastructure;
    }

    public void setMemory(Float memory) {
        this.memory = memory;
    }

    public void setStorage(Float storage) {
        this.storage = storage;
    }

    public void setComputingPower(Float computingPower) {
        this.computingPower = computingPower;
    }

    public void setProcessingSpeed(Float processingSpeed) {
        this.processingSpeed = processingSpeed;
    }


    /**
     * Functions to manipulate the lists of the device.
     */

    public void addLink(Link e) {
        this.links.add(e);
    }

    public void addDeviceToDomain(Device device) {

        if(device == null) throw new NullPointerException("Can't add a Null Device to domain of device " + this.identifier);

        if(this.domainDevices.contains(device)) throw new AlreadyInDomainException("Can't add " + device.getIdentifier() + " to domain of device " + this.identifier + " because it is already in the domain.");
        this.domainDevices.add(device);
    }

    public void addDevicesToDomain(List<Device> devices) {
        devices.forEach(this::addDeviceToDomain);
    }

    /**
     * Run the RouteSolver to resolve all best routes to reachable nodes within the infrastructure
     */
    public void solveRoutes() {
        RouteSolver routeSolver = new RouteSolver(this);
        this.routes = routeSolver.getRoutes();
    }

    /**
     * Initialize an algorithm on a device
     * @param placementAlgorithm        One instance of a placement algorithm
     * @return                          The instance of the placement algorithm
     */
    public PlacementAlgorithm algorithm(PlacementAlgorithm placementAlgorithm) {
        placementAlgorithm.setAlgorithmInitDevice(this);
        return placementAlgorithm;
    }

    /**
     * Find the best route to the target device
     * @param device        The target device
     * @return               Best route to target device
     */
    public Route getRouteTo(Device device) {
        return this.routes.stream().filter(route -> route.getTarget().equals(device)).collect(Collectors.toList()).get(0);
    }

    public String getDevicesInDomainIdentifier() {
        String tmp = "";
        for(Device device: this.domainDevices) {
            tmp += device.getIdentifier() + ", ";
        }
        if(tmp.length() < 2)
            return "";
        return tmp.substring(0, tmp.length() - 2);
    }

    /**
     * Return the identifier of the components on the device.
     * @return      Since a Device cannot host components (only an ApplicationHostDevice can do that) "-" is returned.
     */
    public String getHostedComponentsIdentifier() {
        return "-";
    }
}
