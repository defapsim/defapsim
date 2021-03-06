package com.defapsim.algorithms.decentral.fogdecap;

import com.defapsim.algorithms.PlacementAlgorithm;
import com.defapsim.application.Component;
import com.defapsim.application.migration.MigrationRequest;
import com.defapsim.application.migration.TradeRequest;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.exceptions.InvalidAlgorithmParameterException;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.clouddevice.CloudServer;
import com.defapsim.infrastructure.devices.fognode.FogNode;
import com.defapsim.misc.print.algorithm.FogDecApPrinter;
import com.defapsim.misc.Tupel;
import com.defapsim.simulations.FogDecApSimulation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The Auctioneer is a part of the FogDecAp algorithm.
 * It starts auctions for each individual component, which is placed on the auctioneer device,
 * whereby bidders are invited to the auctions.
 */

public class Auctioneer extends PlacementAlgorithm implements Sender {

    private FogDecApPrinter fogDecApPrinter = new FogDecApPrinter();

    private Component componentToBeAuctioned;
    private ApplicationHostDevice host;

    private FogDecApSimulation fogDecApSimulation;
    private Evaluation evaluation;

    public ApplicationHostDevice getHost(){
        return host;
    }

    @Override
    protected void castAlgorithmParameters(Object... algorithmParameters) {
        if(algorithmParameters == null ) throw new InvalidAlgorithmParameterException("algorithmParameters cannot be null");

        if(!(algorithmParameters[0] instanceof FogDecApSimulation)) throw new InvalidAlgorithmParameterException("");

        if(algorithmParameters.length == 2) {
            if(!(algorithmParameters[1] instanceof Evaluation)) {
                this.evaluation = new Evaluation();
            } else {
                this.evaluation = (Evaluation) algorithmParameters[1];
            }
        }
        this.fogDecApSimulation = (FogDecApSimulation) algorithmParameters[0];
    }

    @Override
    public void start(Object... algorithmParameters) {
        this.castAlgorithmParameters(algorithmParameters);

        this.host = (ApplicationHostDevice) this.algorithmInitDevice;
        List<ApplicationHostDevice> bidderDevice = this.host.getDevicesInDomain().stream()
                .filter(o -> o instanceof CloudServer || o instanceof FogNode)
                .filter(o -> !o.equals(this.host))
                .map(ApplicationHostDevice.class::cast)
                .collect(Collectors.toList());

        if(bidderDevice.isEmpty())
            return;

        // If the algorithm is intended to be executed in parallel, the "dormant time" has to be waited at this place.
        /*try {
             Thread.sleep(dormant_time);
        } catch (InterruptedException e) {
             e.printStackTrace();
        } */


        if(this.fogDecApSimulation == null)
            throw new RuntimeException("FogDecApSimulation cant be null");

        List<Component> componentList = new LinkedList<>(this.host.getComponents());

        for(int i = 0; i < componentList.size(); i++) {
            this.componentToBeAuctioned = componentList.get(i);

            if(this.evaluation != null) {
                this.evaluation.withAmountOfAuctions(this.evaluation.getAmountOfAuctions() + 1);
            }

            if(this.fogDecApSimulation.isBeingDebugged()) {
                fogDecApPrinter.withComponentToBeAuctioned(this.componentToBeAuctioned).withHost(this.host);
            }

            bidderDevice = bidderDevice.stream()
                    .filter(device -> !this.componentToBeAuctioned.getHostBlacklist().contains(device))
                    .filter(device -> device.getComponents().stream().noneMatch(
                            component -> this.componentToBeAuctioned.getComponentBlacklist().contains(component)))
                    .collect(Collectors.toList());


            // Needed for testing purposes
            FogDecApSimulation.results.add(this.componentToBeAuctioned.getComponentApplication().getApplicationLatency());

            Float maxBid = FogDecApFunctions.contribution(this.componentToBeAuctioned, this.host);

            if(this.fogDecApSimulation.isBeingDebugged()) {
                fogDecApPrinter.withMaxbid(maxBid);
            }

            // If the algorithm is intended to be executed in parallel, the "time1" has to be waited at this place.
            // After the time time1 has expired, the auction for the component "componentToBeAuctioned" is restarted.
            /*if(DecApSimulation.statusMap.get(this.host) != Status.FREE) {
                try {
                    Thread.sleep(time1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
                continue;
            }*/
            FogDecApSimulation.statusMap.put(this.host, Status.AUCTIONING);
            bidderDevice.forEach(o -> o.algorithm(FogDecApSimulation.bidderMap.get(o)).start(new Message(MessageType.AUCTION_INTENT, this)));

            // If the algorithm is intended to be executed in parallel, the "time2" has to be waited at this place.
            // This means that the time in which the auctioneer is waiting for the bidders' AUCTION_ACCEPT answers has expired.
            List<Message> answers = new LinkedList<>();
            bidderDevice.forEach(o -> answers.add(FogDecApSimulation.bidderMap.get(o).getAnswer()));

            // This step checks if an AUCTION_CANCEL was sent by the bidder. This is not possible in a deterministic execution of the algorithm.
            /*if(answers.stream().filter(answer -> answer.getMessageType() == MessageType.AUCTION_ACCEPT).count() < bidderDevice.size()) {
                bidderDevice.forEach(o -> o.algorithm(DecApSimulation.bidderMap.get(o)).start(new Message(MessageType.AUCTION_CANCEL, this)));
            }*/

            bidderDevice.forEach(o -> o.algorithm(FogDecApSimulation.bidderMap.get(o)).start(new Message(MessageType.AUCTION_START, this, maxBid)));

            // If the algorithm is intended to be executed in parallel, the "time3" has to be waited at this place.
            // This means that the time in which the auctioneer is waiting for the bidders' answers has expired.
            answers.clear();
            bidderDevice.forEach(o -> answers.add(FogDecApSimulation.bidderMap.get(o).getAnswer()));

            if(fogDecApSimulation.isBeingDebugged()) {
                fogDecApPrinter.withAnswersAfterAuctionStart(answers);
            }

            Map<Bidder, Float> fixedBids = new LinkedHashMap<>();
            Map<Bidder, Component> tradeCandidate = new LinkedHashMap<>();

            answers.forEach(answer -> {
                Map<Component,Float> tradeComponents = (Map<Component, Float>)((Tupel) answer.getBody()).getAttributeB();
                if(tradeComponents.size() > 0) {
                    // adjust bids
                    Map<Component,Float> bids = new LinkedHashMap<>();
                    tradeComponents.keySet().forEach(c_x -> {
                        Float dif = FogDecApFunctions.calcDif(c_x, this.host, this.componentToBeAuctioned);
                        if(this.host.getComponents().stream().filter(o -> !o.equals(this.componentToBeAuctioned))
                                .noneMatch(component -> component.getComponentBlacklist().contains(c_x)) && !c_x.getHostBlacklist().contains(this.host))
                            bids.put(c_x, dif);
                        else
                            bids.put(c_x, maxBid);
                    });
                    Map.Entry<Component, Float> min = minimum(bids);

                    if(fogDecApSimulation.isBeingDebugged()) {
                        fogDecApPrinter.getBiddersBestTradableComponents().put(((Bidder)answer.getSender()), min.getKey());
                    }

                    Float fixedBid = (Float)((Tupel) answer.getBody()).getAttributeA() + min.getValue();

                    if(fogDecApSimulation.isBeingDebugged())
                        fogDecApPrinter.getAdjustedgBiddersBid().put(((Bidder)answer.getSender()), fixedBid);

                    fixedBids.put((Bidder) answer.getSender(), fixedBid);
                    tradeCandidate.put((Bidder) answer.getSender(), min.getKey());

                } else {
                    fixedBids.put((Bidder) answer.getSender(), (Float)((Tupel) answer.getBody()).getAttributeA());
                }
            });

            if(fixedBids.isEmpty()) fixedBids.put(null, maxBid);
            Map.Entry<Bidder, Float> winner = minimum(fixedBids);


            if(fogDecApSimulation.isBeingDebugged()) {
                fogDecApPrinter.withFixedBids(fixedBids);
                fogDecApPrinter.withTradeCandidate(tradeCandidate);
                fogDecApPrinter.withWinner(winner);
            }


            if(Float.compare(winner.getValue(), maxBid.intValue()) < 0) {
                if(tradeCandidate.get(winner.getKey()) == null) {
                    // MIGRATION
                    if(this.evaluation != null) {
                        this.evaluation.withAmountOfMigrations(this.evaluation.getAmountOfMigrations() + 1);
                    }
                    MigrationRequest migrationRequest = new MigrationRequest(this.componentToBeAuctioned).to(winner.getKey().getHost());
                    migrationRequest.perform();
                } else {
                    // TRADE
                    if(this.evaluation != null) {
                        this.evaluation.withAmountOfTrades(this.evaluation.getAmountOfTrades() + 1);
                    }
                    TradeRequest tradeRequest = new TradeRequest(componentToBeAuctioned, tradeCandidate.get(winner.getKey()));
                    tradeRequest.perform();
                }
                FogDecApSimulation.sweetSpotsDetermination.forEach((entry, value) -> FogDecApSimulation.sweetSpotsDetermination.put(entry, false));
            } else {
                FogDecApSimulation.sweetSpotsDetermination.put(componentToBeAuctioned, true);
            }

            FogDecApSimulation.results.add(this.componentToBeAuctioned.getComponentApplication().getApplicationLatency());
            bidderDevice.forEach(o -> FogDecApSimulation.bidderMap.get(o).start(new Message(MessageType.AUCTION_TERMINATION, this)));
            FogDecApSimulation.statusMap.put(this.host, Status.FREE);

            if(fogDecApSimulation.isBeingDebugged())
                fogDecApPrinter.print();
        }
    }

    public Component getComponentToBeAuctioned() {
        return this.componentToBeAuctioned;
    }

    public static <K> Map.Entry<K, Float> minimum(Map<K, Float> map) {
        Map.Entry<K, Float> entry = new ArrayList<>(map.entrySet()).get(0);
        for(Map.Entry<K, Float> comp: map.entrySet()) {
            if(Float.compare(comp.getValue(),entry.getValue()) < 0) entry = comp;
        }
        return entry;
    }
}
