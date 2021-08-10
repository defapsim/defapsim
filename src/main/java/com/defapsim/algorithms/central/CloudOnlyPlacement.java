package com.defapsim.algorithms.central;

import com.defapsim.algorithms.PlacementAlgorithm;
import com.defapsim.application.Application;
import com.defapsim.application.Component;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.exceptions.InvalidAlgorithmParameterException;
import com.defapsim.exceptions.NoHostAvailableException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This algorithm places all algorithms on the cloud.
 * The algorithm requires only at a list of applications as parameter.
 * Can be used for the initial placement of the applications: Yes
 */
public class CloudOnlyPlacement extends PlacementAlgorithm {

    /**
     * The applications that should be placed on the cloud
     */
    private List<Application> applicationList = new LinkedList<>();
    private Evaluation evaluation;

    /**
     * Cast the arbitrary parameters to a list of applications
     * @param algorithmParameters       Arbitrary parameters which have to be casted
     */
    @Override
    protected void castAlgorithmParameters(Object ... algorithmParameters) {
        if(algorithmParameters == null ) throw new InvalidAlgorithmParameterException("algorithmParameters cannot be null");

        if(algorithmParameters[0] == null) throw new InvalidAlgorithmParameterException("algorithmParameters[0] cannot be null");

        if(!(algorithmParameters[0] instanceof List<?>)) throw new InvalidAlgorithmParameterException("algorithmParameters[0] must be a List<Application>");

        ((List)(algorithmParameters[0])).forEach(object -> {
            if (!(object instanceof Application))
                throw new InvalidAlgorithmParameterException("algorithmParameters[0] must be a List<Application>");
        });

        if(algorithmParameters[1] == null) {
            this.evaluation = new Evaluation();
        } else {
            if(!(algorithmParameters[1] instanceof Evaluation)) throw new InvalidAlgorithmParameterException("algorithmParameters[1] must be an evaluation");

            this.evaluation = (Evaluation) algorithmParameters[1];
        }
        this.applicationList = (List<Application>) algorithmParameters[0];
    }

    /**
     * Start the CloudOnlyPlacement algorithm
     * @param algorithmParameters       Arbitrary parameters which have to be casted
     */
    public void start(Object ... algorithmParameters) {
        this.castAlgorithmParameters(algorithmParameters);

        List<ApplicationHostDevice> cloudServers = this.algorithmInitDevice.getInfrastructure().getDevices().stream()
                .filter(CloudServer.class::isInstance)
                .map(ApplicationHostDevice.class::cast)
                .collect(Collectors.toList());

        List<Component> components = new ArrayList<>();
        this.applicationList.forEach(application -> components.addAll(application.getComponents()));
        int componentsToBeDeployed = components.size();

        boolean foundPlacement = false;

        for(Component component: components.stream().filter(component -> !(component.getHostDevice() instanceof CloudServer)).collect(Collectors.toList())) {
            if(componentsToBeDeployed == 0) break;
            foundPlacement = false;
            for(ApplicationHostDevice cloudServer: cloudServers) {

                if(!component.preDeployCheckFor(cloudServer))
                    continue;

                component.getHostDevice().removeComponent(component);
                cloudServer.hostComponent(component);
                evaluation.withAmountOfMigrations(evaluation.getAmountOfMigrations() + 1);
                componentsToBeDeployed--;
                foundPlacement = true;
                break;
            }
            if(!foundPlacement)
                throw new NoHostAvailableException("There is no cloud server in the infrastructure on which the component " + component.getIdentifier()
                        + " can be placed on.");
        }
    }
}
