package com.defapsim.policies.domain;

import com.defapsim.infrastructure.Infrastructure;

/**
 * The DomainPolicy specifies how the domains (limited awareness) of the devices are set within the infrastructure
 */
public interface DomainPolicy {

    /**
     * Creates the domains for the devices from the infrastructure
     * @param infrastructure        The infrastructure in which the devices are located
     */
    void createDomain(Infrastructure infrastructure);
}
