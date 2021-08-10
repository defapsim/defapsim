package com.defapsim.infrastructure.links;

import com.defapsim.infrastructure.devices.Device;

import java.util.List;

/**
 * A route in the form of network hops from one device to another.
 */
public class Route {

    /**
     * The list of Links that will be skipped (hopped) to reach the target device.
     */
    private List<Link> hops;

    /**
     * The target device to which the route leads.
     */
    private Device target;

    /**
     * Route constructor.
     * @param target    The target device to which the route leads.
     * @param hops      Already known hops on the way to the target device (is needed for the RouteSolver)
     */
    public Route(Device target, List<Link> hops) {
        this.target = target;
        this.hops = hops;
    }

    /**
     * Getter & Setter
     */

    public List<Link> getHops() {
        return this.hops;
    }

    public Device getTarget() {
        return this.target;
    }

    public void setHops(List<Link> hops) {
        this.hops = hops;
    }

    /**
     * Receive the data transmission latency of the route.
     * @return      The data transmission latency of the route
     */
    public Float getLatency() {
        return this.hops.stream().map(Link::getLatency).reduce(0.f, Float::sum);
    }
}
