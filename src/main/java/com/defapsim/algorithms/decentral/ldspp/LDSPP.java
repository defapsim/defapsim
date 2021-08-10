package com.defapsim.algorithms.decentral.ldspp;

import com.defapsim.algorithms.PlacementAlgorithm;
import com.defapsim.application.Component;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.exceptions.InvalidAlgorithmParameterException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The LDSPP was developed by Guerrero et al. in their work:
 *
 * Carlos Guerrero, Isaac Lera, and Carlos Juiz. A lightweight decentralized service placement policy
 * for performance optimization in fog computing. Journal of Ambient Intelligence and Humanized
 * Computing, 10(6):2435–2452, 2019.
 */

public class LDSPP extends PlacementAlgorithm {

    private Component requestedComponent;

    private ApplicationHostDevice host;

    private Evaluation evaluation;

    @Override
    protected void castAlgorithmParameters(Object... algorithmParameters) {
        if(algorithmParameters == null ) throw new InvalidAlgorithmParameterException("algorithmParameters cannot be null");

        if(algorithmParameters[0] == null) throw new InvalidAlgorithmParameterException("algorithmParameters[0] cannot be null");

        if(!(algorithmParameters[0] instanceof Component)) throw new InvalidAlgorithmParameterException("algorithmParameters[0] must be a component");

        if(algorithmParameters[1] == null) {
            this.evaluation = new Evaluation();
        } else {
            if(!(algorithmParameters[1] instanceof Evaluation)) throw new InvalidAlgorithmParameterException("algorithmParameters[1] must be an evaluation");

            this.evaluation = (Evaluation) algorithmParameters[1];
        }
        this.requestedComponent = (Component) algorithmParameters[0];
    }

    @Override
    public void start(Object... algorithmParameters) {
        this.castAlgorithmParameters(algorithmParameters);

        this.host = (ApplicationHostDevice) this.algorithmInitDevice;

        if(!this.host.getComponents().contains(this.requestedComponent)) {

            if(this.host instanceof CloudServer) {
                this.host.hostComponent(this.requestedComponent);

                if(this.evaluation != null)
                    this.evaluation.withAmountOfMigrations(this.evaluation.getAmountOfMigrations() + 1);
            } else {
                //if(this.host.getFreeRAM() >= this.requestedComponent.getRAMDemand() && this.host.getFreeComputingPower() >= this.requestedComponent.getComputingPowerDemand()) {
                if(Float.compare(this.host.getFreeMemory(), this.requestedComponent.getMemoryDemand()) >= 0
                        && Float.compare(this.host.getFreeComputingPower(), this.requestedComponent.getComputingPowerDemand()) >= 0) {
                    this.requestedComponent.getHostDevice().removeComponent(this.requestedComponent);
                    this.host.hostComponent(this.requestedComponent);

                    if(this.evaluation != null)
                        this.evaluation.withAmountOfMigrations(this.evaluation.getAmountOfMigrations() + 1);

                } else {
                    ApplicationHostDevice father = LDSPPFunctions.getFather(this.host);
                    //if(this.host.getRAM() < this.requestedComponent.getRAMDemand() || this.host.getComputingPower() < this.requestedComponent.getComputingPowerDemand()) {
                    if(Float.compare(this.host.getMemory(), this.requestedComponent.getMemoryDemand()) < 0
                            || Float.compare(this.host.getComputingPower(), this.requestedComponent.getComputingPowerDemand()) < 0) {

                        father.algorithm(new LDSPP()).start(this.requestedComponent, this.evaluation);
                    } else {
                        Float RAM_to_free = this.requestedComponent.getMemoryDemand() - this.host.getFreeMemory();
                        Float ComputingPower_to_free = this.requestedComponent.getComputingPowerDemand() - this.host.getFreeComputingPower();
                        List<Component> M_deallocate = new LinkedList<>();
                        List<Component> S_allocated = this.host.getComponents();

                        List<List<Component>> interoperatedComponents = LDSPPFunctions.getInteroperatedComponents(this.host);
                        LDSPPFunctions.orderAscBy(interoperatedComponents, this.host);

                        for (List<Component> subset : interoperatedComponents) {
                            Float min = LDSPPFunctions.getRequestRateForSubset(subset, this.host);

                            if (Float.compare(LDSPPFunctions.getRequestRateForComponent(this.requestedComponent, this.host) + min, min) > 0) {
                                M_deallocate.addAll(subset.stream().filter(entry -> !M_deallocate.contains(entry)).collect(Collectors.toList()));

                                for (Component c : subset) {
                                    if(S_allocated.contains(c)) {
                                        RAM_to_free = RAM_to_free - c.getMemoryDemand();
                                        ComputingPower_to_free = ComputingPower_to_free - c.getComputingPowerDemand();
                                    }
                                }

                                S_allocated.removeAll(subset.stream().filter(S_allocated::contains).collect(Collectors.toList()));

                                } else {
                                    break;
                                }
                        }
                        if(Float.compare(RAM_to_free, 0.F) <= 0 && Float.compare(ComputingPower_to_free, 0.F) <= 0) {
                            for(Component c : M_deallocate) {
                                father.algorithm(new LDSPP()).start(c, this.evaluation);
                            }
                            this.requestedComponent.getHostDevice().removeComponent(this.requestedComponent);
                            this.host.hostComponent(this.requestedComponent);

                            if(this.evaluation != null)
                                this.evaluation.withAmountOfMigrations(this.evaluation.getAmountOfMigrations() + 1);
                        } else {
                            father.algorithm(new LDSPP()).start(this.requestedComponent, this.evaluation);
                        }
                    }
                }
            }
        }
    }
}
