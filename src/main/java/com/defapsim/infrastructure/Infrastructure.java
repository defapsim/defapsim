package com.defapsim.infrastructure;

import com.defapsim.application.Component;
import com.defapsim.exceptions.AlreadyInDomainException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * All devices must be associated with an infrastructure.
 * Only devices within an infrastructure can communicate with each other
 * With the help of the method resolveRoutes() all routes within the infrastructure can be solved
 */
public class Infrastructure {

    /**
     * Identifier is specified as a string to identify the infrastructure.
     */
    private String identifier = "Generic Infrastructure";

    /**
     * List of all devices within the infrastructure.
     */
    private List<Device> devices = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Infrastructure() {
    }

    /**
     * Constructor in which the identifier is passed.
     * @param identifier       The identifier of the infrastructure
     */
    public Infrastructure(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public List<Device> getDevices() {
        return this.devices;
    }

    /**
     * Add a device to the infrastructure.
     * @param device    The device to be added to the infrastructure
     * @throws AlreadyInDomainException if the device is already in the infrastructure
     * @throws NullPointerException    if the device is null
     */
    public Infrastructure addDevice(Device device) {
        if(device == null) throw new NullPointerException("Can't add a Null Device to domain " + this.identifier + ".");
        if(this.devices.contains(device)) throw new AlreadyInDomainException("Can't add " + device.getIdentifier() + " to infrastructure " + this.identifier + " because it is already in the infrastructure.");
        this.devices.add(device);
        return this;
    }

    /**
     * Remove all components placed on the infrastructure devices
     * Remove the devices in the domain for each device
     */
    public Infrastructure resetInfrastructure() {
        for(Device device : this.devices) {
            device.getDevicesInDomain().clear();
            if(device instanceof ApplicationHostDevice) {
                for(Component component : ((ApplicationHostDevice) device).getComponents()) {
                    component.setHostDevice(null);
                }
                ((ApplicationHostDevice) device).getComponents().clear();
            }
        }
        return this;
    }

    /**
     * Resolves all routes of the devices within the infrastructure
     */
    public void resolveRoutes() {
        this.devices.forEach(Device::solveRoutes);
    }

}
