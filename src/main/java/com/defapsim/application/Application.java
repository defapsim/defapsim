package com.defapsim.application;

import com.defapsim.infrastructure.devices.enddevice.EndDevice;

import java.util.LinkedList;
import java.util.List;

/**
 * An application which consists of components
 */
public class Application {

    /**
     * Identifier is specified as a string to identify the application.
     */
    private String identifier = "Generic Application";

    /**
     * The set of all components of the application.
     */
    private List<Component> components = new LinkedList<>();

    /**
     * The set of all end devices to which the application is connected.
     */
    private List<EndDevice> endDevices = new LinkedList<>();

    public Application() {
    }

    public Application(String identifier) {
        this.identifier = identifier;
    }

    public Application(List<Component> components) {
        this.withComponents(components);
    }

    public Application(String identifier, List<Component> components) {
        this.identifier = identifier;
        this.withComponents(components);
    }

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public List<EndDevice> getEndDevices() {
        return this.endDevices;
    }

    /**
     * Setter (according to Expression Builder pattern)
     */

    public Application withComponent(Component component) {
        component.setComponentApplication(this);
        this.components.add(component);
        return this;
    }

    public void withComponents(List<Component> components) {
        components.forEach(this::withComponent);
    }

    /**
     * Functions to manipulate the lists of the application.
     */

    public void removeComponent(Component component) {
        this.components.remove(component);
    }

    public void removeComponents(List<Component> components) {
        this.components.removeAll(components);
    }

    public void removeAllComponents() {
        this.components.clear();
    }

    public void addConnectedDevice(EndDevice endDevice) {
        this.endDevices.add(endDevice);
    }

    /**
     * The application latency (which must be minimized)
     * It takes into account the execution time of components on the devices on which they are deployed
     * and the data transfer time of the connectors.
     * @return      The application latency
     */
    public Float getApplicationLatency() {
        Float overallLatency = 0.F;

        for(Component component: this.components) {
            overallLatency += component.getWorstCaseExecutionTime()  * (1 / component.getHostDevice().getProcessingSpeed());

            for(Connector connector: component.getConnectors()) {

                if(connector.getTarget() instanceof EndDevice) {
                    overallLatency += component.getHostDevice().getRouteTo((EndDevice)connector.getTarget()).getLatency();

                } else if(connector.getTarget() instanceof Component) {
                    Component target = (Component) connector.getTarget();

                    if(component.getHostDevice() == target.getHostDevice())
                        continue;

                    overallLatency += component.getHostDevice().getRouteTo(target.getHostDevice()).getLatency();
                }
            }
        }

        for (EndDevice endDevice: this.endDevices) {

            for(Connector connector: endDevice.getConnectors()) {
                overallLatency += endDevice.getRouteTo(((Component)connector.getTarget()).getHostDevice()).getLatency();
            }
        }
        return overallLatency;
    }
}
