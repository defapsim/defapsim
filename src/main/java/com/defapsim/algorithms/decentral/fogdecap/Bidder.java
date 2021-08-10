package com.defapsim.algorithms.decentral.fogdecap;

import com.defapsim.algorithms.PlacementAlgorithm;
import com.defapsim.application.Component;
import com.defapsim.exceptions.InvalidAlgorithmParameterException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.misc.Tupel;
import com.defapsim.simulations.FogDecApSimulation;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Bidder is a part of the FogDecAp algorithm.
 * He is invited by an auctioneer and places a bid for the component that is up for auction
 */

public class Bidder extends PlacementAlgorithm implements Sender {

    private ApplicationHostDevice host;

    private Message answer;
    private Message receiveMessage;
    private Auctioneer currentAuctioneer;

    public ApplicationHostDevice getHost() {
        return this.host;
    }

    public Message getAnswer() {
        Message a = this.answer;
        this.answer = null;
        return a;
    }

    @Override
    protected void castAlgorithmParameters(Object... algorithmParameters) {
        if(algorithmParameters == null ) throw new InvalidAlgorithmParameterException("algorithmParameters cannot be null");

        if(algorithmParameters[0] == null) throw new InvalidAlgorithmParameterException("algorithmParameters[0] cannot be null");

        if(!(algorithmParameters[0] instanceof Message)) throw new InvalidAlgorithmParameterException("algorithmParameters[0] must be from type Message");

        this.receiveMessage = (Message) algorithmParameters[0];
    }

    @Override
    public void start(Object... algorithmParameters) {
        this.castAlgorithmParameters(algorithmParameters);

        this.host = (ApplicationHostDevice) this.algorithmInitDevice;

        switch(this.receiveMessage.getMessageType())
        {
            case AUCTION_INTENT:
                if(FogDecApSimulation.statusMap.get(this.host).equals(Status.FREE)) {
                    FogDecApSimulation.statusMap.put(this.host, Status.BIDDING);
                    this.currentAuctioneer = (Auctioneer) this.receiveMessage.getSender();
                    this.answer = new Message(MessageType.AUCTION_ACCEPT, this);
                } else {
                    this.answer = new Message(MessageType.AUCTION_REJECT, this);
                }
                break;

            case AUCTION_CANCEL:
            case AUCTION_TERMINATION:
                if(this.receiveMessage.getSender() == this.currentAuctioneer) {
                    this.currentAuctioneer = null;
                    FogDecApSimulation.statusMap.put(this.host, Status.FREE);
                }
                break;

            case AUCTION_START:
                Component toBeAuctioned = currentAuctioneer.getComponentToBeAuctioned();
                Float bid = FogDecApFunctions.contribution(toBeAuctioned, this.host);
                Float maxBid = (Float) this.receiveMessage.getBody();

                Map<Component, Float> tradeableComponents = new LinkedHashMap<>();
                Tupel R = new Tupel(bid, tradeableComponents);

                // if(bid < maxBid)
                if(Float.compare(bid, maxBid) < 0) {

                    if(Float.compare(this.host.getFreeMemory(), currentAuctioneer.getComponentToBeAuctioned().getMemoryDemand()) < 0
                            || Float.compare(this.host.getFreeComputingPower(), currentAuctioneer.getComponentToBeAuctioned().getComputingPowerDemand()) < 0) {

                        for(Component component : this.host.getComponents()) {
                            if(Float.compare(toBeAuctioned.getMemoryDemand(),(component.getMemoryDemand() + this.host.getFreeMemory())) <= 0
                                    && Float.compare(component.getMemoryDemand(), (toBeAuctioned.getMemoryDemand() + currentAuctioneer.getHost().getFreeMemory())) <= 0
                                    && Float.compare(toBeAuctioned.getComputingPowerDemand(), (component.getComputingPowerDemand() + this.host.getFreeComputingPower())) <= 0
                                    && Float.compare(component.getComputingPowerDemand(), (toBeAuctioned.getComputingPowerDemand() + currentAuctioneer.getHost().getFreeComputingPower())) <= 0) {

                                Float tmpbid = FogDecApFunctions.contribution(component, this.getHost());
                                tradeableComponents.put(component, tmpbid);
                            }
                        }
                        if(tradeableComponents.size() == 0) {
                            R.setAttributeA(maxBid);
                        }
                    }
                }
                this.answer = new Message(MessageType.AUCTION_BID, this, R);
        }
    }
}
