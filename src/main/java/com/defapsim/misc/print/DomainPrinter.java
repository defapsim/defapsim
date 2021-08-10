package com.defapsim.misc.print;

import com.defapsim.infrastructure.devices.Device;

import java.util.List;

/**
 * Print method, which allow to get information about the devices in the domain
 */
public class DomainPrinter implements Printer {

    /**
     * Private static Singleton instance
     */
    private static final DomainPrinter instance = new DomainPrinter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private DomainPrinter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static DomainPrinter getInstance() {
        return instance;
    }

    @Override
    public void printHeadline() {
    }

    /**
     * List of devices whose domain is to be printed out
     * @param deviceList        List of devices whose domain is printed out
     */
    public void printDevicesDomains(List<Device> deviceList) {
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
        System.out.println("|\t" +
                ConsoleFormatter.center("Identifier", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("Devices in domain", ((this.columnSize * 3) + 3 * this.columns)) +
                "\t|\t");
        System.out.println("| " + ConsoleFormatter.line((this.columnSize * this.columns) - 4) + " |");

        int lastIndex;

        for (Device device : deviceList) {
            if(device.getDevicesInDomainIdentifier().length() <  ((this.columnSize * 3) + 3 * this.columns)) {
                lastIndex = device.getDevicesInDomainIdentifier().length() ;
            } else {
                lastIndex = ((this.columnSize * 3) + 3 * this.columns);
            }

            String sub = device.getDevicesInDomainIdentifier().substring(0, lastIndex);

            System.out.println("|\t" +
                    ConsoleFormatter.center(device.getIdentifier(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(sub, ((this.columnSize * 3) + 3 * this.columns)) +
                    "\t|");

            for(int i = 0; i < (device.getDevicesInDomainIdentifier().length()) / ((this.columnSize * 3) + 3 * this.columns); i++) {

                if(lastIndex > device.getDevicesInDomainIdentifier().length())
                    break;

                int end = lastIndex + ((this.columnSize * 3) + 3 * this.columns);

                if(end > device.getDevicesInDomainIdentifier().length()) {
                    end = device.getDevicesInDomainIdentifier().length();
                }

                sub = device.getDevicesInDomainIdentifier().substring(lastIndex, end);
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
