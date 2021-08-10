package com.defapsim.policies.initialplacement;

import com.defapsim.application.Application;
import com.defapsim.infrastructure.Infrastructure;

/**
 * The initial placement specifies how the components are initially placed in the infrastructure.
 */
public interface InitialPlacementPolicy {

    /**
     * Place an application initially into the infrastructure
     * @param infrastructure        The infrastructure in which the application is to be placed
     * @param application           The application to be initially placed in the infrastructure
     */
    void placeApplication(Infrastructure infrastructure, Application application);
}
