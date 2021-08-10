package com.defapsim.application.migration;

import com.defapsim.application.Component;
import com.defapsim.exceptions.MigrationUnrealizableException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;

/**
 * The TradeRequest is used to trade two components
 * this.component is component 1 which should be migrated.
 * It was inherited from the super class "MigrationRequest".
 */
public class TradeRequest extends MigrationRequest {

    /**
     * The component 2 which should be migrated.
     */
    private Component toTradeComponent;

    /**
     *
     * @param toMigrateComponent        component 1 which should be migrated.
     * @param toTradeComponent          component 2 which should be migrated.
     */
    public TradeRequest(Component toMigrateComponent, Component toTradeComponent) {
        super(toMigrateComponent);
        this.to(toTradeComponent.getHostDevice());
        this.toTradeComponent = toTradeComponent;
    }

    /**
     * Perform a trade of component "this.component" and "this.toTradeComponent"
     */
    @Override
    public void perform() {
        this.toTradeComponent.getHostDevice().removeComponent(this.toTradeComponent);
        ApplicationHostDevice device = this.component.getHostDevice();
        super.perform();
        if(this.toTradeComponent.preDeployCheckFor(device)) {
            this.toTradeComponent.setHostDevice(device);
            device.getComponents().add(this.toTradeComponent);
            return;
        }
        throw new MigrationUnrealizableException("Can't migrate " + this.component.getIdentifier() +
                " from " + this.component.getHostDevice().getIdentifier() +
                " to " + this.possibleTarget.getIdentifier());
    }
}
