package com.defapsim.policies.domain;

import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;

/**
 * The GlobalDomainPolicy places each device in the domain of every other device
 */
public class GlobalDomainPolicy implements DomainPolicy {

    @Override
    public void createDomain(Infrastructure infrastructure) {
        for(Device device : infrastructure.getDevices()) {
            infrastructure.getDevices().stream()
                    .filter(o -> !o.equals(device))
                    .forEach(device::addDeviceToDomain);
        }
    }
}
