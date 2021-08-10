package com.defapsim.application;

import com.defapsim.exceptions.ConnectorAlreadyExistsException;
import com.defapsim.exceptions.InvalidConnectorConfigurationException;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;

/**
 * A connector can exist between components or components and end devices.
 */
public class Connector {

    /**
     * Static method for creating an undirected connector
     * @param connectorConfiguration    The configuration of the undirected connector
     * @param <T>                       The source of the connector ("Component" or "Enddevice")
     * @param <S>                       The target of the connector ("Component" or "Enddevice")
     * @param <C>                       The type of the connector ("ComponentConnector" or "EndDeviceConnector")
     * @throws InvalidConnectorConfigurationException       if the connector configuration is invalid
     */
    public static <T extends Connectable<C>, S extends  Connectable<C>, C extends Connector> void addUndirected(ConnectorConfiguration<S,T> connectorConfiguration) throws InvalidConnectorConfigurationException {
        if(connectorConfiguration.getSource() == null) throw new InvalidConnectorConfigurationException("No SOURCE_COMPONENT given for current connector: ? -> " +
                (connectorConfiguration.getTarget() == null ? " TARGET_COMPONENT " : connectorConfiguration.getTarget().getIdentifier()));

        if(connectorConfiguration.getTarget() == null) throw new InvalidConnectorConfigurationException("No TARGET_COMPONENT given for current connector: " + connectorConfiguration.getSource().getIdentifier() + " -> ?");

        if (connectorConfiguration.getSource().getConnectors().stream().anyMatch(connector -> connector.getTarget() == connectorConfiguration.getTarget()))
            throw new ConnectorAlreadyExistsException("Connector: " + connectorConfiguration.getSource().getIdentifier() + " -> " + connectorConfiguration.getTarget().getIdentifier() + " already exists");

        if (connectorConfiguration.getTarget().getConnectors().stream().anyMatch(connector -> connector.getTarget() == connectorConfiguration.getSource()))
            throw new ConnectorAlreadyExistsException("Connector: " + connectorConfiguration.getTarget().getIdentifier() + " -> " + connectorConfiguration.getSource().getIdentifier() + " already exists");

        // CREATE UNDIRECTED CONNECTORS BETWEEN COMPONENT_A AND COMPONENT_B
        if(connectorConfiguration.getSource() instanceof EndDevice) {

            EndDevice source = ((EndDevice) connectorConfiguration.getSource());
            Component target = ((Component) connectorConfiguration.getTarget());

            source.addEndDeviceConnector(new EndDeviceConnector(connectorConfiguration.getIdentifierA(), target));
            target.getBeeingTarget().add(source);
            target.addConnector(new EndDeviceConnector(connectorConfiguration.getIdentifierB(), source));
            target.getComponentApplication().addConnectedDevice(source);
        }
        else if(connectorConfiguration.getSource() instanceof Component) {

            Component source = ((Component) connectorConfiguration.getSource());

            if(connectorConfiguration.getTarget() instanceof  Component) {
                Component target = ((Component) connectorConfiguration.getTarget());
                source.addConnector(new ComponentConnector(connectorConfiguration.getIdentifierA(), target));
                target.addConnector(new ComponentConnector(connectorConfiguration.getIdentifierB(), source));
                source.getBeeingTarget().add(target);
                target.getBeeingTarget().add(source);
            }

            if(connectorConfiguration.getTarget() instanceof EndDevice) {
                EndDevice target = ((EndDevice) connectorConfiguration.getTarget());
                source.addConnector(new EndDeviceConnector(connectorConfiguration.getIdentifierA(), target));
                source.getBeeingTarget().add(target);
                target.addEndDeviceConnector(new EndDeviceConnector(connectorConfiguration.getIdentifierB(), source));
            }
        }
    }

    /**
     * Static method for creating an directed connector
     * @param connectorConfiguration    The configuration of the directed connector
     * @param <T>                       The source of the connector ("Component" or "Enddevice")
     * @param <S>                       The target of the connector ("Component" or "Enddevice")
     * @param <C>                       The type of the connector ("ComponentConnector" or "EndDeviceConnector")
     * @throws InvalidConnectorConfigurationException       if the connector configuration is invalid
     */
    public static <T extends Connectable<C>, S extends  Connectable<C>, C extends Connector> void addDirected(ConnectorConfiguration<S,T> connectorConfiguration) throws InvalidConnectorConfigurationException {
        if(connectorConfiguration.getSource() == null) throw new InvalidConnectorConfigurationException("No SOURCE_COMPONENT given for current connector: ? -> " +
                (connectorConfiguration.getTarget() == null ? " TARGET_COMPONENT " : connectorConfiguration.getTarget().getIdentifier()));

        if(connectorConfiguration.getTarget() == null) throw new InvalidConnectorConfigurationException("No TARGET_COMPONENT given for current connector: " + connectorConfiguration.getSource().getIdentifier() + " -> ?");

        if (connectorConfiguration.getSource().getConnectors().stream().anyMatch(connector -> connector.getTarget() == connectorConfiguration.getTarget()))
            throw new ConnectorAlreadyExistsException("Connector: " + connectorConfiguration.getSource().getIdentifier() + " -> " + connectorConfiguration.getTarget().getIdentifier() + " already exists");

        if(connectorConfiguration.getSource() instanceof EndDevice) {

            EndDevice source = ((EndDevice) connectorConfiguration.getSource());
            Component target = ((Component) connectorConfiguration.getTarget());

            EndDeviceConnector e = new EndDeviceConnector(connectorConfiguration.getIdentifierA(), target);
            source.addEndDeviceConnector(e);
            target.getBeeingTarget().add(source);
            target.getComponentApplication().addConnectedDevice(source);

        } else if(connectorConfiguration.getSource() instanceof Component) {

            Component source = ((Component) connectorConfiguration.getSource());

            if(connectorConfiguration.getTarget() instanceof  Component) {
                Component target = ((Component) connectorConfiguration.getTarget());
                source.addConnector(new ComponentConnector(connectorConfiguration.getIdentifierA(), target));
                target.getBeeingTarget().add(source);
            }

            if(connectorConfiguration.getTarget() instanceof EndDevice) {
                EndDevice target = ((EndDevice) connectorConfiguration.getTarget());
                source.addConnector(new EndDeviceConnector(connectorConfiguration.getIdentifierA(), target));
            }
        }
    }

    public Connector(String identifier, Connectable target) {
        this.identifier = identifier;
        this.target = target;
    }

    /**
     * Identifier is specified as a string to identify the connector.
     */
    private String identifier;

    /**
     * The target of a connector
     */
    private Connectable target;

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public Connectable getTarget() {
        return this.target;
    }
}
