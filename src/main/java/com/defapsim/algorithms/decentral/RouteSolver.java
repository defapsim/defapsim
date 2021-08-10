package com.defapsim.algorithms.decentral;

import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.Route;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class RouteSolver is used to automatically generate routes to the other devices within the infrastructure
 */
public class RouteSolver {

    // New metrics in route calculation can be added by changing the following Map
    private Map<Device, Float> knowntargets = new LinkedHashMap<>();
    
    private List<Route> routes = new LinkedList<>();
    private List<Device> tested = new LinkedList<>();


    public RouteSolver(Device device) {
        this.tested.add(device);
        knowntargets.put(device, 0.f);
        for(Link link: device.getLinks()) {
            Route route = new Route(link.getTarget(), new LinkedList<>() {{ this.add(link); }});
            knowntargets.put(link.getTarget(), route.getLatency());
            routes.add(route);
        }

        for(Link link: device.getLinks()) {
            List<Link> hops = new LinkedList<>();
            hops.add(link);
            checkChildDevice(link.getTarget(), hops);
        }
    }

    private void checkChildDevice(Device device, List<Link> previousHops) {
        this.tested.add(device);
        for(Link link: device.getLinks()) {
            List<Link> hops = new LinkedList<>(List.copyOf(previousHops));
            hops.add(link);
            if (knowntargets.containsKey(link.getTarget())) {
                Route route = new Route(link.getTarget(), hops);
                // New metrics in route calculation can be added by changing the following instruction
                if(Float.compare(knowntargets.get(link.getTarget()), route.getLatency()) > 0) {

                    boolean equal = false;
                    for(Route routeCopy : this.routes) {
                        routeCopy.setHops(changeRoutes(link.getTarget(), routeCopy.getHops(), route));
                        knowntargets.put(routeCopy.getTarget(), routeCopy.getLatency());
                        if(routeCopy.getTarget().equals(route.getTarget())) equal = true;
                    }
                    for(Route routeCopy : this.routes) {
                        replaceSubRoutes(routeCopy, routeCopy.getHops());
                    }
                    for(Route routeCopy : this.routes) {
                        replaceRoutesForUncheckedDevices(routeCopy);
                    }

                    if(!equal) {
                        this.routes.add(route);
                    }
                    knowntargets.put(link.getTarget(), route.getLatency());
                }

            } else {
                Route route = new Route(link.getTarget(), hops);
                this.routes.add(route);
                knowntargets.put(link.getTarget(), route.getLatency());
            }
        }
        for(Link link: device.getLinks()) {
            List<Link> hops = new LinkedList<>(List.copyOf(previousHops));
            hops.add(link);
            if(!tested.contains(link.getTarget())) {
                checkChildDevice(link.getTarget(), hops);
            }
        }
    }

    private void replaceRoutesForUncheckedDevices(Route route) {
        for(Route route1 : this.routes) {
            if(route1.equals(route)) continue;

            for(Link link: route1.getTarget().getLinks()) {
                if(link.getTarget().equals(route.getTarget())) {
                    List<Link> hops = new LinkedList<>();
                    hops.addAll(route1.getHops());
                    hops.add(link);

                    Float latency = route1.getLatency() + link.getLatency();

                    Route route2 = this.routes.stream().filter(r -> r.getTarget().equals(route.getTarget())).
                            collect(Collectors.toList()).get(0);
                    // New metrics in route calculation can be added by changing the following instruction
                    if(Float.compare(latency, route2.getLatency()) < 0) {
                        route.setHops(hops);

                        for(Link link2 : route2.getHops()) {
                            Route childs = this.routes.stream().filter(r -> r.getTarget().equals(link2.getTarget())).
                                    collect(Collectors.toList()).get(0);
                            boolean isComingAfterwards = false;
                            int tmp = 0;
                            for(int i = 0; i < this.routes.size(); i++) {
                                if(this.routes.get(i).equals(route2)) {
                                    tmp = i;
                                }
                                if(i > tmp && this.routes.equals(childs))
                                    isComingAfterwards = true;
                            }
                            if(!isComingAfterwards)
                                replaceRoutesForUncheckedDevices(childs);
                        }
                    }
                }
            }
        }
    }

    void replaceSubRoutes(Route allroute, List<Link> hops) {
        int latency = 0;
        for(Link link: hops) {
            latency += link.getLatency();
            Route route = this.routes.stream().filter(r -> r.getTarget().equals(link.getTarget())).
                    collect(Collectors.toList()).get(0);
            // New metrics in route calculation can be added by changing the following instruction
            if(Float.compare(latency, route.getLatency()) < 0) {
                allroute.setHops(changeRoutes(route.getTarget(), hops, route));
            }
        }
    }

    private List<Link> changeRoutes(Device target, List<Link> hops, Route currentRoute) {
        hops = reverseLinkedList(hops);
        List<Link> currentRouteLists = reverseLinkedList(currentRoute.getHops());
        int i = 0;
        for(Link link: hops) {
            if(link.getTarget().equals(target)) {
                List<Link> tmp = hops.subList(0, i);
                tmp.addAll(currentRouteLists);
                return reverseLinkedList(tmp);
            }
            i++;
        }
        return reverseLinkedList(hops);
    }

    private List<Link> reverseLinkedList(List<Link> list) {
        List<Link> revLinkedList = new LinkedList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            revLinkedList.add(list.get(i));
        }
        return revLinkedList;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }
}
