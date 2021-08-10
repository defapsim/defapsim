package com.defapsim.misc.print;

import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.Route;

import java.util.List;

/**
 * Print methods, which allow to get information about the route of devices
 */
public class RoutePrinter implements Printer {

    /**
     * Private static Singleton instance
     */
    private static final RoutePrinter instance = new RoutePrinter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private RoutePrinter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static RoutePrinter getInstance() {
        return instance;
    }

    @Override
    public void printHeadline() {
    }

    static String s = "";

    /**
     * Shows the routes of a device
     * @param device        The device whose routes should be displayed
     */
    public void printRoutes(Device device) {
        for (Route route : device.getRoutes()) {
            printRoute(route);
        }
    }

    /**
     * Shows the routes of a list of devices
     * @param deviceList        The devices whose routes should be displayed
     */
    public void printRoutes(List<Device> deviceList) {
        for(Device device : deviceList) {
            for (Route route : device.getRoutes()) {
                printRoute(route);
            }
        }
    }

    /**
     * Print method for a route
     * @param route         The route which should be displayed
     */
    private void printRoute(Route route) {
        s += ConsoleFormatter.rightPad("Route to " + route.getTarget().getIdentifier() + " ("+ route.getLatency() +"): ", 35);
        for (Link hop : route.getHops()) {
            s += ConsoleFormatter.rightPad("--" + hop.getIdentifier() + " (" + hop.getLatency() + ")--> " + hop.getTarget().getIdentifier(), 40);
        }
        System.out.println(s);
        s = "";
    }
}
