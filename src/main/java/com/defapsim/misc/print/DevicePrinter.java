package com.defapsim.misc.print;

import com.defapsim.infrastructure.devices.Device;

import java.util.List;

/**
 * Print method, which allow to get information about the devices
 */
public class DevicePrinter implements Printer {

    /**
     * Private static Singleton instance
     */
    private static final DevicePrinter instance = new DevicePrinter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private DevicePrinter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static DevicePrinter getInstance() {
        return instance;
    }

    @Override
    public void printHeadline() {
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
        System.out.println("|\t" +
                ConsoleFormatter.center("Identifier", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("RAM (GB)", this.columnSize) +
                /*"\t|\t" +
                ConsoleFormatter.center("Storage (GB)", 25) +*/
                "\t|\t" +
                ConsoleFormatter.center("ComputingPower (vCPU)", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("ProcessingSpeed", this.columnSize) +
                "\t|");
        System.out.println("| " + ConsoleFormatter.line((this.columnSize * this.columns) - 4) + " |");
    }

    /**
     * Display the device configuration for a device
     * @param device        The device whose device configuration should be displayed
     */
    public void printDevice(Device device) {
        this.printHeadline();
        System.out.println("|\t" +
                    ConsoleFormatter.center(device.getIdentifier(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(device.getMemory().toString(), this.columnSize) +
                    /*"\t|\t" +
                    ConsoleFormatter.center(device.getStorage().toString(), 25) +*/
                    "\t|\t" +
                    ConsoleFormatter.center(device.getComputingPower().toString(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(device.getProcessingSpeed().toString(), this.columnSize) +
                    "\t|");

        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
    }

    /**
     * Display the device configuration for a set of devices
     * @param deviceList        The set of devices whose device configuration should be displayed
     */
    public void printDeviceConfigurationList(List<Device> deviceList) {
        this.printHeadline();
        for (Device device : deviceList) {
            System.out.println("|\t" +
                    ConsoleFormatter.center(device.getIdentifier(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(device.getMemory().toString(), this.columnSize) +
                    /*"\t|\t" +
                    ConsoleFormatter.center(device.getStorage().toString(), 25) +*/
                    "\t|\t" +
                    ConsoleFormatter.center(device.getComputingPower().toString(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(device.getProcessingSpeed().toString(), this.columnSize) +
                    "\t|");
        }
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
    }

    /**
     * Display the components identifier for a set of devices
     * @param deviceList        The set of devices whose components identifier should be displayed
     */
    public void printDeviceComponentsList(List<Device> deviceList) {
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
        System.out.println("|\t" +
                ConsoleFormatter.center("Identifier", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("Components", ((this.columnSize * 3) + 3 * this.columns)) +
                "\t|\t");
        System.out.println("| " + ConsoleFormatter.line((this.columnSize * this.columns) - 4) + " |");

        int lastIndex;

        for (Device device : deviceList) {
            if(device.getHostedComponentsIdentifier().length() <  ((this.columnSize * 3) + 3 * this.columns)) {
                lastIndex = device.getHostedComponentsIdentifier().length();
            } else {
                lastIndex = ((this.columnSize * 3) + 3 * this.columns);
            }

            String sub = device.getHostedComponentsIdentifier().substring(0, lastIndex);

            System.out.println("|\t" +
                    ConsoleFormatter.center(device.getIdentifier(), columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(sub, ((this.columnSize * 3) + 3 * this.columns)) +
                    "\t|");

            for(int i = 0; i < (device.getHostedComponentsIdentifier().length()) / ((this.columnSize * 3) + 3 * this.columns); i++) {

                if(lastIndex > device.getHostedComponentsIdentifier().length())
                    break;

                int end = lastIndex + (this.columnSize * 3) + 3 * this.columns;

                if(end > device.getHostedComponentsIdentifier().length()) {
                    end = device.getHostedComponentsIdentifier().length();
                }

                sub = device.getHostedComponentsIdentifier().substring(lastIndex, end);
                System.out.println("|\t" +
                        ConsoleFormatter.center("", this.columnSize) +
                        "\t|\t" +
                        ConsoleFormatter.center(sub, ((this.columnSize * 3) + 3 * this.columns)) +
                        "\t|");
                lastIndex = end;
            }
        }
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
    }
}

