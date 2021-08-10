package com.defapsim.misc.print;

import com.defapsim.infrastructure.Infrastructure;

/**
 * Print methods, which allow to get information about the devices of the infrastructure.
 */
public class InfrastructurePrinter implements Printer {

    /**
     * Private static Singleton instance
     */
    private static final InfrastructurePrinter instance = new InfrastructurePrinter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private InfrastructurePrinter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static InfrastructurePrinter getInstance() {
        return instance;
    }

    @Override
    public void printHeadline() {
    }

    /**
     * Shows for each device of the infrastructure what links it has to other devices,
     * what its capacities are and what components are placed on it.
     * @param infrastructure        the infrastructur
     */
    public void printInfrastructure(Infrastructure infrastructure) {
        printInfrastructureDevicesConfiguration(infrastructure);
        printInfrastructureLinks(infrastructure);
        printInfrastructureDevicesComponents(infrastructure);
    }

    /**
     * Shows for each device of the infrastructure which links it has to other devices
     * @param infrastructure        the infrastructur
     */
    public void printInfrastructureLinks(Infrastructure infrastructure) {
        LinkPrinter.getInstance().printLinks(infrastructure.getDevices());
    }

    /**
     * Displays for each device of the infrastructure what capacities it has
     * @param infrastructure        the infrastructur
     */
    public void printInfrastructureDevicesConfiguration(Infrastructure infrastructure) {
        DevicePrinter.getInstance().printDeviceConfigurationList(infrastructure.getDevices());
    }

    /**
     * Displays for each device of the infrastructure which component is placed on the device
     * @param infrastructure        the infrastructur
     */
    public void printInfrastructureDevicesComponents(Infrastructure infrastructure) {
        DevicePrinter.getInstance().printDeviceComponentsList(infrastructure.getDevices());
    }
}
