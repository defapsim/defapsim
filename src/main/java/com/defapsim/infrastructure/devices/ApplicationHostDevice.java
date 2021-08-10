package com.defapsim.infrastructure.devices;

import com.defapsim.application.Component;
import com.defapsim.exceptions.HostUnsuitableException;

import java.util.LinkedList;
import java.util.List;
/**
 *
 * The abstract class ApplicationHostDevice specifies the properties common to all
 * devices within the infrastructure on which the placement of components is allowed.
 */
public abstract class ApplicationHostDevice extends Device {

    /**
     * The list of all components placed on the device.
     */
    private List<Component> components = new LinkedList<>();

    public List<Component> getComponents() {
        return components;
    }

    /**
     * Get the available free memory of the device.
     * @return                  free memory
     */
    public Float getFreeMemory() {
        return this.components.stream().map(Component::getMemoryDemand).reduce(this.getMemory(), (a, b) -> a - b);
    }

    /**
     * Get the available free storage of the device.
     * @return                  free storage
     */
    public Float getFreeStorage() {
        return this.components.stream().map(Component::getStorageDemand).reduce(this.getStorage(), (a,b) -> a - b);
    }

    /**
     * Get the available computing power of the device.
     * @return                  available computing power
     */
    public Float getFreeComputingPower() {
        return this.components.stream().map(Component::getComputingPowerDemand).reduce(this.getComputingPower(), (a, b) -> a - b);
    }

    /**
     * Place a component on the device.
     * @param component         The component to be placed on the device
     * @throws HostUnsuitableException  if there is a route missing to one of the connected components' hosts
     */
    public void hostComponent(Component component) {

        // IF ROUTE EXISTS TO ALL ALREADY DEPLOYED COMPONENTS WHICH THIS COMPONENT HAS A CONNECTOR TO
        if(component.preDeployCheckFor(this)) {
            component.setHostDevice(this);
            this.components.add(component);
        } else {
            throw new HostUnsuitableException("Host " + this.getIdentifier() + " can't host component " + component.getIdentifier() +
                    ", because there is a route missing to one of the connected components' hosts.");
        }
    }

    /**
     * Place a list of components on the device.
     * @param components        The list of components to be placed on the device
     */
    public void hostComponents(List<Component> components) {
        components.forEach(this::hostComponent);
    }

    /**
     * Remove a component from the device.
     * @param component         The component to be removed from the device
     */
    public void removeComponent(Component component) {
        component.setHostDevice(null);
        this.components.remove(component);
    }

    /**
     * Remove a list of components from the device.
     * @param components        The list of components to be removed from the device
     */
    public void removeComponents(List<Component> components) {
        components.forEach(component -> component.setHostDevice(null));
        this.components.removeAll(components);
    }

    /**
     * Remove all components that have been placed on the device.
     */
    public void removeAllComponents() {
        this.components.forEach(component -> component.setHostDevice(null));
        this.components.clear();
    }

    /**
     * Get all identifiers of the components placed on the ApplicationHostDevice
     * @return      Identifiers of the components placed on the ApplicationHostDevice
     */
    @Override
    public String getHostedComponentsIdentifier() {
        String tmp = "";
        for(Component component: this.components) {
            tmp += component.getIdentifier() + ", ";
        }
        if(tmp.length() < 2)
            return "";
        return tmp.substring(0, tmp.length() - 2);
    }
}
