package com.defapsim.application.migration;

import com.defapsim.application.Component;
import com.defapsim.exceptions.AlreadyDeployedException;
import com.defapsim.exceptions.DeviceCantHostComponentsException;
import com.defapsim.exceptions.MigrationUnrealizableException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.Device;

/**
 * The MigrationRequest is used to migrate a component to another device.
 */
public class MigrationRequest {

    /**
     * The component which should be migrated.
     */
    protected Component component;

    /**
     * The device to which the component should be migrate.
     */
    protected ApplicationHostDevice possibleTarget;

    /**
     * Constructor
     * @param component     The component which should be migrated.
     */
    public MigrationRequest(Component component) {
        this.component = component;
    }

    /**
     * Set the device to which the component should be migrated
     * @param possibleDevice        the device to which the component should be migrated
     * @return                      this object
     */
    public MigrationRequest to(Device possibleDevice) {
        ApplicationHostDevice applicationHostDevice;
        try {
            applicationHostDevice = (ApplicationHostDevice) possibleDevice;
        } catch (ClassCastException e) {
            throw new DeviceCantHostComponentsException("Device " + possibleDevice.getIdentifier() + " cant host components.");
        }
        this.possibleTarget = applicationHostDevice;
        return this;
    }

    /**
     * Perform a migration of component "this.component" from the device on which it is placed to device "this.possibleTarget"
     */
    public void perform() {
        if(this.component.getHostDevice().equals(this.possibleTarget))
            throw new AlreadyDeployedException("Component " + this.component.getIdentifier() + " already deployed on host " + this.possibleTarget.getIdentifier());

        if(this.component.preDeployCheckFor(this.possibleTarget)) {
            this.component.getHostDevice().removeComponent(this.component);
            this.component.setHostDevice(this.possibleTarget);
            this.possibleTarget.getComponents().add(this.component);
            return;
        }
        throw new MigrationUnrealizableException("Can't migrate " + this.component.getIdentifier() +
                " from " + this.component.getHostDevice().getIdentifier() +
                " to " + this.possibleTarget.getIdentifier());
    }
}
