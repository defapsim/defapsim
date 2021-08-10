package com.defapsim.infrastructure.devices;

import com.defapsim.exceptions.InvalidDeviceConfigurationException;

/**
 * The abstract creator of the Factory Method Pattern.
 */
public abstract class DeviceCreator {

    /**
     * Factory Method, which is implemented in the concrete Creators.
     * @param deviceConfiguration   The device configuration from which the device properties are taken
     * @return                      The corresponding device of the used Creator
     */
    protected abstract Device createDevice(DeviceConfiguration deviceConfiguration);

    /**
     * Create a concrete device, where the type of device depends on the Creator you are using, and set the device properties using the deviceConfiguration.
     * @param deviceConfiguration   The device configuration from which the device properties are taken
     * @return                      The corresponding device of the used Creator
     * @throws InvalidDeviceConfigurationException  if the memory or computingPower in the deviceConfiguration are null.
     */
    public Device register(DeviceConfiguration deviceConfiguration) {

        if(deviceConfiguration.getInfrastructure() == null || deviceConfiguration.getMemory() == null || deviceConfiguration.getComputingPower() == null)
            throw new InvalidDeviceConfigurationException("Configuration invalid for " + deviceConfiguration.getIdentifier() + ".");

        Device device = this.createDevice(deviceConfiguration);

        device.setIdentifier(deviceConfiguration.getIdentifier());

        device.setInfrastructure(deviceConfiguration.getInfrastructure());
        deviceConfiguration.getInfrastructure().addDevice(device);

        device.setMemory(deviceConfiguration.getMemory());
        device.setStorage(deviceConfiguration.getStorage());
        device.setComputingPower(deviceConfiguration.getComputingPower());
        device.setProcessingSpeed(deviceConfiguration.getProcessingSpeed());

        return device;
    }
}
