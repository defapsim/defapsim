package com.defapsim.infrastructure.links;

import com.defapsim.exceptions.InvalidLinkConfigurationException;
import com.defapsim.exceptions.LinkAlreadyExistsException;
import com.defapsim.infrastructure.devices.Device;

/**
 * A link is logically created by the following notation: from ---> to (where from <--> to is also possible)
 * "from" indicates the source device
 * "to" indicates the target device
 * It is possible to define directed links and undirected edges
 * A link is directed when from ---> to
 * A link is undirected when from ---> to AND from <--- to
 * Link properties can be bidirectionally different, for example:
 *                                                              from --(10ms)-> to
 *                                                              from <-(20ms)-- to
 */
public class Link {

    /**
     * Create a undirected link with the linkConfiguration: from --(linkConfiguration)-> to AND from <-(linkConfiguration)-- to
     * implies from <-(linkConfiguration)-> to.
     * If latencyA == latencyB then latencyB can be left out when defining the linkConfiguration
     * @param linkConfiguration         The link configuration, which contains the properties that the link should receive
     * @throws InvalidLinkConfigurationException    if deviceA, deviceB or latencyA are null
     * @throws LinkAlreadyExistsException           if a link from deviceA to deviceB OR from deviceB to deviceA already exists
     */
    public static void addUndirected(LinkConfiguration linkConfiguration) {
        if(linkConfiguration.getDeviceA() == null)
            throw new InvalidLinkConfigurationException("No SOURCE_DEVICE given for current link: ? -> " + (linkConfiguration.getDeviceB() == null ? " TARGET_DEVICE " : linkConfiguration.getDeviceB().getIdentifier()));

        if(linkConfiguration.getDeviceB() == null)
            throw new InvalidLinkConfigurationException("No TARGET_DEVICE given for current link: " + linkConfiguration.getDeviceA().getIdentifier() + " -> ?");

        //if(linkConfiguration.getLatencyA() == null || linkConfiguration.getLatencyA() == 0)
        //    throw new InvalidLinkConfigurationException("No LATENCY given for link " + linkConfiguration.getDeviceA().getIdentifier() + " -> " + linkConfiguration.getDeviceB().getIdentifier());

        if(linkConfiguration.getLatencyB() == null || linkConfiguration.getLatencyB() == 0)
            linkConfiguration.withLatencyToFrom(linkConfiguration.getLatencyA());

        if (linkConfiguration.getDeviceA().getLinks().stream().anyMatch(link -> link.target == linkConfiguration.getDeviceB()))
            throw new LinkAlreadyExistsException("Link: " + linkConfiguration.getDeviceA().getIdentifier() + " -> " + linkConfiguration.getDeviceB().getIdentifier() + " already exists");

        if (linkConfiguration.getDeviceB().getLinks().stream().anyMatch(link -> link.target == linkConfiguration.getDeviceA()))
            throw new LinkAlreadyExistsException("Link: " + linkConfiguration.getDeviceB().getIdentifier() + " -> " + linkConfiguration.getDeviceA().getIdentifier() + " already exists");

        linkConfiguration.getDeviceA().addLink(new Link(linkConfiguration.getIdentifierA(), linkConfiguration.getLatencyA(), linkConfiguration.getDeviceB()));
        linkConfiguration.getDeviceB().addLink(new Link(linkConfiguration.getIdentifierB(), linkConfiguration.getLatencyB(), linkConfiguration.getDeviceA()));
    }

    /**
     * Create a directed link with the linkConfiguration: from --(linkConfiguration)-> to
     * @param linkConfiguration         The link configuration, which contains the properties that the link should receive
     * @throws InvalidLinkConfigurationException    if deviceA, deviceB or latencyA are null
     * @throws LinkAlreadyExistsException           if a link from deviceA to deviceB already exists
     */
    public static void addDirected(LinkConfiguration linkConfiguration) {
        if(linkConfiguration.getDeviceA() == null)
            throw new InvalidLinkConfigurationException("No SOURCE_DEVICE given for current link: ? -> " + (linkConfiguration.getDeviceB() == null ? " TARGET_DEVICE " : linkConfiguration.getDeviceB().getIdentifier()));

        if(linkConfiguration.getDeviceB() == null)
            throw new InvalidLinkConfigurationException("No TARGET_DEVICE given for current link: " + linkConfiguration.getDeviceA().getIdentifier() + " -> ?");

        if(linkConfiguration.getLatencyA() == null)
            throw new InvalidLinkConfigurationException("No LATENCY given for link " + linkConfiguration.getDeviceA().getIdentifier() + " -> " + linkConfiguration.getDeviceB().getIdentifier());

        if (linkConfiguration.getDeviceA().getLinks().stream().anyMatch(link -> link.target == linkConfiguration.getDeviceB()))
            throw new LinkAlreadyExistsException("Link: " + linkConfiguration.getDeviceA().getIdentifier() + " -> " + linkConfiguration.getDeviceB().getIdentifier() + " already exists");

        linkConfiguration.getDeviceA().addLink(new Link(linkConfiguration.getIdentifierA(), linkConfiguration.getLatencyA(), linkConfiguration.getDeviceB()));
    }

    /**
     * Identifier is specified as a string to identify the link.
     */
    private String identifier;

    /**
     * latency is specified as a positive floating point value in MilliSeconds (ms)
     */
    private Float latency;

    /**
     * target is the target device of the link.
     */
    private Device target;

    /**
     * Link constructor
     * @param identifier        the identifier of the link
     * @param latency           the latency of the link
     * @param target            the target device of the link
     */
    public Link(String identifier, Float latency, Device target) {
        this.identifier = identifier;
        this.latency = latency;
        this.target = target;
    }

    /**
     * Getter
     */

    public String getIdentifier() {
        return this.identifier;
    }

    public Float getLatency() {
        return this.latency;
    }

    public Device getTarget() {
        return this.target;
    }
}
