package com.defapsim.infrastructure.links;

import com.defapsim.infrastructure.devices.Device;

/**
 * An object of the LinkConfiguration class defines the properties that a link receives.
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
public class LinkConfiguration {

    /**
     * identifierA is specified as a string to identify the link in direction: from --(identifierA)-> to.
     */
    private String identifierA = "Generic Link";

    /**
     * identifierB is specified as a string to identify the link in direction: from <-(identifierB)-- to.
     */
    private String identifierB = "Generic Link";

    /**
     * latencyA is specified as a positive floating point value in MilliSeconds (ms) in direction: from --(latencyA)-> to.
     */
    private Float latencyA = 0.F;

    /**
     * latencyB is specified as a positive floating point value in MilliSeconds (ms) in direction: from <-(latencyB)-- to.
     */
    private Float latencyB = 0.F;

    /**
     * deviceA is the source device (from = deviceA) of the link.
     */
    private Device deviceA;

    /**
     * deviceA is the target device (to = deviceB) of the link.
     */
    private Device deviceB;

    /**
     * Getter
     */

    public String getIdentifierA() {
        return this.identifierA;
    }

    public String getIdentifierB() {
        return this.identifierB;
    }

    public Float getLatencyA() {
        return this.latencyA;
    }

    public Float getLatencyB() {
        return this.latencyB;
    }

    public Device getDeviceA() {
        return this.deviceA;
    }

    public Device getDeviceB() {
        return this.deviceB;
    }

    /**
     * Setter (according to Expression Builder pattern)
     */

    public LinkConfiguration withNameFromTo(String identifierA) {
        this.identifierA = identifierA;
        return this;
    }

    public LinkConfiguration withNameToFrom(String identifierB) {
        this.identifierB = identifierB;
        return this;
    }

    public LinkConfiguration withLatencyFromTo(Float latencyA) {
        this.latencyA = latencyA;
        return this;
    }

    public LinkConfiguration withLatencyToFrom(Float latencyB) {
        this.latencyB = latencyB;
        return this;
    }

    public LinkConfiguration from(Device sourceDevice) {
        this.deviceA = sourceDevice;
        return this;
    }

    public LinkConfiguration to(Device targetDevice) {
        this.deviceB = targetDevice;
        return this;
    }
}
