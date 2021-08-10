package com.defapsim.policies.initialplacement;

import com.defapsim.application.Application;
import com.defapsim.application.Component;
import com.defapsim.exceptions.NoHostAvailableException;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The RandomInitialPlacementPolicy places each component on a random node in the infrastructure that satisfies the placement constraints.
 * In order to make the placement deterministic for several repetition runs, a seed can be specified.
 * However, this seed is only optional.
 */
public class RandomInitialPlacementPolicy implements InitialPlacementPolicy {

    private Integer seed;

    @Override
    public void placeApplication(Infrastructure infrastructure, Application application) {

        Random random;

        for(Component component : application.getComponents()) {

            if(this.seed != null)
                random = new Random(this.seed);
            else
                random = new Random();

            List<ApplicationHostDevice> applicationHostDevicesList = infrastructure.getDevices().stream()
                    .filter(o -> o instanceof ApplicationHostDevice)
                    .map(ApplicationHostDevice.class::cast)
                    .filter(component::preDeployCheckFor)
                    .collect(Collectors.toList());

            if(applicationHostDevicesList.size() == 0) {
                throw new NoHostAvailableException("There is no device in the infrastructure on which the component " + component.getIdentifier()
                        + " can be placed on.");
            }

            ApplicationHostDevice applicationHostDevice = applicationHostDevicesList.get(random.nextInt(applicationHostDevicesList.size()));
            applicationHostDevice.hostComponent(component);
        }
    }

    public RandomInitialPlacementPolicy withSeed(Integer seed) {
        this.seed = seed;
        return this;
    }
}
