package com.defapsim.application;

import com.defapsim.exceptions.InvalidDeviceConfigurationException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.links.Route;

import java.util.LinkedList;
import java.util.List;

/**
 * A component for an application
 */
public class Component implements Connectable {

    /**
     * Identifier is specified as a string to identify the component
     */
    private String identifier;

    /**
     * The application to which the component belongs
     */
    private Application componentApplication;

    /**
     * The device on which the component is placed
     */
    private ApplicationHostDevice applicationHostDevice;

    /**
     * memoryDemand is specified as a positive floating point value.
     */
    private Float memoryDemand = 0.F;

    /**
     * storageDemand is specified as a positive floating point value.
     */
    private Float storageDemand = 0.F;

    /**
     * computingPowerDemand is specified as a positive floating point value.
     */
    private Float computingPowerDemand = 0.F;

    /**
     * The worst-case execution time of the component is specified as milliseconds.
     */
    private Float worstCaseExecutionTime = 0.F;

    /**
     * The list of all conectors, starting from the component.
     * A connector can be an "EndDeviceConnector" or a "ComponentConnector".
     * A "EndDeviceConnector" is used for the connection between a component and a end device.
     * A "ComponentConnector" is used for the connection between a component and another component.
     */
    private List<Connector> connectors = new LinkedList<>();

    /**
     * The set of components with which the component is not allowed to be on the same device.
     */
    private List<Component> componentBlacklist = new LinkedList<>();

    /**
     * The set of devices on which the component is not allowed to be placed.
     */
    private List<Device> hostBlacklist = new LinkedList<>();

    /**
     * All "Connectables" (i.e. all components and end devices) that point to the component with a connector.
     * This is used so that the component knows which "Connectable" points to it.
     */
    private List<Connectable> beeingTarget = new LinkedList<>();

    private Component(ComponentConfiguration componentConfiguration) {
        if(componentConfiguration.getMemoryDemand() == null ||
                componentConfiguration.getWorstCaseExecutionTime() == null ||
                componentConfiguration.getComputingPowerDemand() == null)
            throw new InvalidDeviceConfigurationException("Configuration invalid for " + componentConfiguration.getIdentifier());

        this.setIdentifier(componentConfiguration.getIdentifier());
        this.setMemoryDemand(componentConfiguration.getMemoryDemand());
        this.setComputingPowerDemand(componentConfiguration.getComputingPowerDemand());
        this.setWorstCaseExecutionTime(componentConfiguration.getWorstCaseExecutionTime());
    }

    public static Component createFromConfiguration(ComponentConfiguration componentConfiguration){
        return new Component(componentConfiguration);
    }

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public Application getComponentApplication() {
        return this.componentApplication;
    }

    public ApplicationHostDevice getHostDevice() {
        return this.applicationHostDevice;
    }

    public Float getMemoryDemand() {
        return this.memoryDemand;
    }

    public Float getStorageDemand() {
        return this.storageDemand;
    }

    public Float getComputingPowerDemand() {
        return this.computingPowerDemand;
    }

    public Float getWorstCaseExecutionTime() {
        return this.worstCaseExecutionTime;
    }

    public List<Connector> getConnectors() {
        return this.connectors;
    }

    public List<Component> getComponentBlacklist() {
        return this.componentBlacklist;
    }

    public List<Device> getHostBlacklist() {
        return this.hostBlacklist;
    }

    public List<Connectable> getBeeingTarget() {
        return this.beeingTarget;
    }

    /**
     * Setter
     */

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setComponentApplication(Application componentApplication) {
        this.componentApplication = componentApplication;
    }

    public void setHostDevice(ApplicationHostDevice applicationHostDevice) {
        this.applicationHostDevice = applicationHostDevice;
    }

    public void setMemoryDemand(Float memoryDemand) {
        this.memoryDemand = memoryDemand;
    }

    public void setStorageDemand(Float storageDemand) {
        this.storageDemand = storageDemand;
    }

    public void setComputingPowerDemand(Float computingPowerDemand) {
        this.computingPowerDemand = computingPowerDemand;
    }

    public void setWorstCaseExecutionTime(Float worstCaseExecutionTime) {
        this.worstCaseExecutionTime = worstCaseExecutionTime;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public void setComponentBlacklist(List<Component> componentBlacklist) {
        this.componentBlacklist = componentBlacklist;
    }

    public void setHostBlacklist(List<Device> componentBlacklist) {
        this.hostBlacklist = componentBlacklist;
    }

    /**
     * Functions to manipulate the lists of the application.
     */

    public void addConnector(Connector connector) {
        this.connectors.add(connector);
    }

    public void addComponentToBlacklist(Component component) {
        this.componentBlacklist.add(component);
        component.getComponentBlacklist().add(this);
    }

    public void addHostToBlacklist(ApplicationHostDevice device) {
        this.hostBlacklist.add(device);
    }

    /**
     * Function that checks if all restrictions are fulfilled so that the component may be placed on the "device"
     * @param device      The device on which the component is to be placed
     * @return boolean     which specifies whether the component may be placed on the device
     */
    public boolean preDeployCheckFor(ApplicationHostDevice device) {

        // if ( device.getFreeMemory() < this.getMemoryDemand() )
        if(Float.compare(device.getFreeMemory(), this.getMemoryDemand()) < 0) {
            return false;
        }

        // if ( device.getFreeComputingPower() < this.getComputingPowerDemand() )
        if(Float.compare(device.getFreeComputingPower(), this.getComputingPowerDemand()) < 0) {
            return false;
        }

        if(this.getHostBlacklist().contains(device)) {
            return false;
        }

        for(Component component: device.getComponents()) {
            if(component.getComponentBlacklist().contains(this) || this.getComponentBlacklist().contains(component)) {
                return false;
            }
        }

        for(Component component: this.getComponentApplication().getComponents()) {

            if(component.getHostDevice() != null) {
                for(Connector connector: component.getConnectors()) {

                    if(!connector.getTarget().equals(this))
                        continue;

                    if(connector.getTarget() instanceof EndDevice) {
                        if(connector.getTarget() == null) {
                            continue;
                        }
                    } else if(connector.getTarget() instanceof  Component && ((Component)connector.getTarget()).getHostDevice() == null) {
                            continue;
                    }

                    boolean found = false;
                    for(Route route: component.getHostDevice().getRoutes()) {
                        if(route.getTarget().equals(device)) {
                            found = true;
                            break;
                        }
                    }
                    if(component.getHostDevice().equals(device)) found = true;
                    if(!found) {
                        return false;
                    }
                }
            }
        }

        for(Connector connector: this.getConnectors()) {
            boolean found = false;

            if(connector.getTarget() instanceof EndDevice) {
                if(connector.getTarget() != null) {

                    EndDevice target = (EndDevice) connector.getTarget();

                    for(Route route: device.getRoutes()) {
                        if(route.getTarget().equals(target)) {
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        return false;
                    }
                }
            } else if(connector.getTarget() instanceof  Component) {
                Component target = (Component) connector.getTarget();
                if(target.getHostDevice() != null) {
                    for(Route route: device.getRoutes()) {
                        if(route.getTarget().equals(target.getHostDevice())) {
                            found = true;
                            break;
                        }
                    }
                    if(target.getHostDevice().equals(device)) found = true;
                    if(!found) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
