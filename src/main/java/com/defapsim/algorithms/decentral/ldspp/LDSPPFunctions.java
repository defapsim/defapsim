package com.defapsim.algorithms.decentral.ldspp;

import com.defapsim.application.Component;
import com.defapsim.application.Connector;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;
import com.defapsim.infrastructure.links.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper functions for the LDSPP algorithm
 */
public class LDSPPFunctions {

    /**
     * Get the first device in the shortest path to the cloud for the applicationHostDevice
     * @param applicationHostDevice     the device from which the first device should be found in the shortest path to the cloud
     * @return                          the first device in the shortest path to the cloud for the applicationHostDevice
     */
    public static ApplicationHostDevice getFather(ApplicationHostDevice applicationHostDevice) {
        ApplicationHostDevice father = null;

        for(Route route : applicationHostDevice.getRoutes()) {
            if(route.getTarget() instanceof CloudServer) {
                father = (ApplicationHostDevice) route.getHops().get(0).getTarget();
                break;
            }
        }
        return father;
    }

    /**
     * Get A list of component lists. In the component lists are the components which are connected to each other on the device by connectors
     * @param device    the device on which the components connected by connectors are to be determined
     * @return          a list of component lists. In the component lists are the components which are connected to each other on the device by connectors
     */
    public static List<List<Component>> getInteroperatedComponents(ApplicationHostDevice device) {
        List<List<Component>> interoperatedComponents = new LinkedList<>();

        for(Component component : device.getComponents()) {
            List<Component> subset = new LinkedList<>();
            subset = getInteroperatedComponents(component);
            if(!subset.isEmpty()) interoperatedComponents.add(subset);
        }

        return interoperatedComponents;
    }

    public static List<Component> getInteroperatedComponents(Component component) {
        List<Component> tmp = new LinkedList<>();
        List<Component> interoperatedComponents = getInteroperatedComponents(component, tmp);
        interoperatedComponents.add(component);
        return interoperatedComponents;
    }

    public static List<Component> getInteroperatedComponents(Component component, List<Component> alreadyVisitedComponents) {

        if(!alreadyVisitedComponents.contains(component))
            alreadyVisitedComponents.add(component);

        List<Component> interoperatedComponents = new LinkedList<>();

        ApplicationHostDevice host = component.getHostDevice();
        for(Connector connector : component.getConnectors()) {
            if(connector.getTarget() instanceof Component) {
                Component target = (Component) connector.getTarget();
                if(target.getHostDevice().equals(host) && !alreadyVisitedComponents.contains(target))
                    interoperatedComponents.add(target);
            }
        }

        List<Component> tmp = new LinkedList<>(interoperatedComponents).stream().filter(c -> !c.equals(component)).collect(Collectors.toList());
        for(Component c : tmp) {
            interoperatedComponents.addAll(getInteroperatedComponents(c, alreadyVisitedComponents));
        }
        return interoperatedComponents;
    }

    public static List<List<Component>> orderAscBy(List<List<Component>> interoperatedComponents, ApplicationHostDevice host) {

        interoperatedComponents.sort((o1, o2) -> Float.compare(o1.stream().map(component -> LDSPPFunctions.getRequestRateForComponent(component, host)).reduce(Float::sum).get(),
                o2.stream().map(component -> LDSPPFunctions.getRequestRateForComponent(component, host)).reduce(Float::sum).get()));

        return interoperatedComponents;
    }

    public static Float getRequestRateForComponent(Component component, ApplicationHostDevice device) {
        return 1.F;
    }

    public static Float getRequestRateForSubset(List<Component> subset, ApplicationHostDevice device) {
        return subset.stream().map(component -> getRequestRateForComponent(component, device)).reduce(Float::sum).get();
    }

}
