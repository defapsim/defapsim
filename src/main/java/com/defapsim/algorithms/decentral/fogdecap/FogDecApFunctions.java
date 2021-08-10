package com.defapsim.algorithms.decentral.fogdecap;

import com.defapsim.application.Component;
import com.defapsim.application.Connectable;
import com.defapsim.application.Connector;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;

public class FogDecApFunctions {

    /**
     * The function that determines the contribution of a component on a device
     * @param component     The component whose contribution should be calculated
     * @param device        The device on which it is assumed that the component is placed there
     * @return
     */
    public static Float contribution(Component component, ApplicationHostDevice device) {
        Float contribution = 0.0F;
        // Consider routes for the following connectors: (Component | EndDvice) ----> Component "component"
        for(Connectable connectable : component.getBeeingTarget()) {

            if(connectable instanceof Component) {
                ApplicationHostDevice applicationHostDevice = ((Component) connectable).getHostDevice();
                if(!device.equals(applicationHostDevice)) {
                    contribution += applicationHostDevice.getRouteTo(device).getLatency();
                }
            }

            if(connectable instanceof EndDevice) {
                EndDevice endDevice = (EndDevice) connectable;
                contribution += endDevice.getRouteTo(device).getLatency();
            }
        }

        // Consider routes for the following connectors: Component "component" ----> (Component | EndDvice)
        for(Connector connector : component.getConnectors()) {

            if(connector.getTarget() instanceof Component) {
                Component target = (Component)connector.getTarget();
                if(!device.equals(target.getHostDevice())) {
                    contribution += device.getRouteTo(target.getHostDevice()).getLatency();
                }
                continue;
            }

            // Execution time on the "component" on the "device"
            if(connector.getTarget() instanceof EndDevice) {
                EndDevice target = (EndDevice)connector.getTarget();
                contribution += device.getRouteTo(target).getLatency();
            }
        }

        contribution += component.getWorstCaseExecutionTime() * (1 / device.getProcessingSpeed());

        return contribution;
    }


    /**
     * The function that calculates the deterioration that happens due to a trade under the assumption that the auctioned component was migrated
     * @param c_x           The Trade Candidate for which the deterioration should be calculated
     * @param device        The bidder Device
     * @param c_a           The initial auctioned Component
     * @return
     */
    public static Float calcDif(Component c_x, ApplicationHostDevice device,
                                Component c_a) {

        ApplicationHostDevice componentRealHost_c_x = c_x.getHostDevice();
        ApplicationHostDevice componentRealHost_c_a = c_a.getHostDevice();

        c_a.setHostDevice(componentRealHost_c_x);
        componentRealHost_c_x.getComponents().add(c_a);
        componentRealHost_c_a.getComponents().remove(c_a);

        Float contribution_1 = FogDecApFunctions.contribution(c_x, device);
        Float contribution_2 = FogDecApFunctions.contribution(c_x, c_x.getHostDevice());

        c_a.setHostDevice(componentRealHost_c_a);
        componentRealHost_c_x.getComponents().remove(c_a);
        componentRealHost_c_a.getComponents().add(c_a);


        return contribution_1 - contribution_2;
    }
}
