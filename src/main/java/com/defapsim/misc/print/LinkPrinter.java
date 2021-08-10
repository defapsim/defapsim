package com.defapsim.misc.print;

import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.links.Link;

import java.util.List;

/**
 * Print methods, which allow to get information about the devices of the infrastructure.
 */
public class LinkPrinter implements Printer {

    /**
     * Private static Singleton instance
     */
    private static final LinkPrinter instance = new LinkPrinter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private LinkPrinter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static LinkPrinter getInstance() {
        return instance;
    }

    @Override
    public void printHeadline() {
    }

    /**
     * Displays the links for each device in a list
     * @param deviceList        The list in which those devices are located, whose links are printed out
     */
    public void printLinks(List<Device> deviceList) {
        for(Device device : deviceList) {
            System.out.println(device.getIdentifier());
            for(Link link : device.getLinks()) {
                System.out.println(ConsoleFormatter.rightPad("\t--" + link.getIdentifier() +
                        " (" + link.getLatency() + ")--> " + link.getTarget().getIdentifier(), 40));
            }
        }
    }
}
