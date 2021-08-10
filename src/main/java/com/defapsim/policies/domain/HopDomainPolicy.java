package com.defapsim.policies.domain;

import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The HopDomainPolicy places each device in the domain of every other device that is "hops" network hops away.
 */
public class HopDomainPolicy implements DomainPolicy {

    private Integer hops;

    public HopDomainPolicy withHops(Integer hops) {
        this.hops = hops;
        return this;
    }

    @Override
    public void createDomain(Infrastructure infrastructure) {
        if(hops == 0) return;
        for(Device device : infrastructure.getDevices()) {
            get1HopLinkedDevices(device).forEach(
                    device1 -> {
                        if (!device.getDevicesInDomain().contains(device1) && !device1.equals(device)) {
                            device.addDeviceToDomain(device1);
                        }
                        addDependentDevice(device, device1, hops-1);
                    }
            );
        }
    }

    private void addDependentDevice(Device root, Device device, Integer i) {
        if (i != 0) {
            get1HopLinkedDevices(device).forEach(device2 -> {
                if (!root.getDevicesInDomain().contains(device2) && !device2.equals(root)) {
                    root.addDeviceToDomain(device2);
                }
                addDependentDevice(root, device2, i - 1);
            });
        }
    }

    private List<Device> get1HopLinkedDevices(Device device){
        return device.getLinks().stream().map(Link::getTarget).collect(Collectors.toList());
    }
}
