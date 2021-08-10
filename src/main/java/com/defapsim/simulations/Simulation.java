package com.defapsim.simulations;


import com.defapsim.application.Application;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.policies.domain.DomainPolicy;
import com.defapsim.policies.initialplacement.InitialPlacementPolicy;

import java.util.LinkedList;
import java.util.List;

public abstract class Simulation {

    /**
     * The infrastructure on which a simulation is performed
     */
    protected Infrastructure infrastructure = new Infrastructure();
    /**
     * The set of applications which are taken into account
     */
    protected List<Application> applications = new LinkedList<>();

    /**
     * The domain policy for the domain assignment
     */
    protected DomainPolicy domainPolicy;

    /**
     * The initial placement policy by which the components are to be placed in the infrastructure
     */
    protected InitialPlacementPolicy initialPlacementPolicy;

    /**
     * Object in which the data for the evaluation is stored
     */
    protected Evaluation evaluation;

    /**
     * Called to prepare a simulation
     */
    public abstract void prepareSimulation();

    /**
     * Performs a simulation. The simulation should already have been prepared ( see prepareSimulation() )
     */
    public abstract void startSimulation();

    /**
     * Setter (according to the Expression Builder pattern)
     */

    public Simulation withInfrastructure(Infrastructure infrastructure) {
        this.infrastructure = infrastructure;
        return this;
    }

    public Simulation withApplication(Application application) {
        this.applications.add(application);
        return this;
    }

    public Simulation withDomainPolicy(DomainPolicy domainPolicy) {
        this.domainPolicy = domainPolicy;
        return this;
    }

    public Simulation withInitialPlacementPolicy(InitialPlacementPolicy initialPlacementPolicy) {
        this.initialPlacementPolicy = initialPlacementPolicy;
        return this;
    }

    public Simulation withEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
        return this;
    }
}
