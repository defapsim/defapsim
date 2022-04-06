package com.defapsim.misc.print.algorithm;

import com.defapsim.algorithms.decentral.edgedecap.Bidder;
import com.defapsim.algorithms.decentral.edgedecap.Message;
import com.defapsim.application.Component;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.misc.Tupel;
import com.defapsim.misc.print.ConsoleFormatter;
import com.defapsim.misc.print.Printer;

import java.util.*;

/**
 * An individual printer for the EdgeDecAp algorithm
 */
public class EdgeDecApPrinter implements Printer {

    private int columns = 3 + 1;

    private Component componentToBeAuctioned;
    private ApplicationHostDevice host;
    private Float maxBid;
    private List<Message> answersAfterAuctionStart = new LinkedList<>();

    public Map <Bidder, Component> getBiddersBestTradableComponents() {
        return biddersBestTradableComponents;
    }

    private Map <Bidder, Component> biddersBestTradableComponents =  new LinkedHashMap<>();

    public Map<Bidder, Float> getAdjustedgBiddersBid() {
        return adjustedgBiddersBid;
    }

    private Map <Bidder, Float> adjustedgBiddersBid =  new LinkedHashMap<>();

    private Map <Bidder, Float> fixedBids;

    private Map<Bidder, Component> tradeCandidate;
    private Map.Entry<Bidder, Float> winner;

    public EdgeDecApPrinter withComponentToBeAuctioned(Component componentToBeAuctioned) {
        this.componentToBeAuctioned = componentToBeAuctioned;
        return this;
    }

    public EdgeDecApPrinter withHost(ApplicationHostDevice host) {
        this.host = host;
        return this;
    }

    public EdgeDecApPrinter withMaxbid(Float maxBid) {
        this.maxBid = maxBid;
        return this;
    }

    public EdgeDecApPrinter withAnswersAfterAuctionStart(List<Message> answersAfterAuctionStart) {
        this.answersAfterAuctionStart = answersAfterAuctionStart;
        return this;
    }

    public EdgeDecApPrinter withFixedBids(Map<Bidder, Float> fixedBids) {
        this.fixedBids = fixedBids;
        return this;
    }

    public EdgeDecApPrinter withTradeCandidate(Map<Bidder, Component> tradeCandidate) {
        this.tradeCandidate = tradeCandidate;
        return this;
    }

    public EdgeDecApPrinter withWinner(Map.Entry<Bidder, Float> winner) {
        this.winner = winner;
        return this;
    }

    public EdgeDecApPrinter print() {
        printHeadline();
        return this;
    }

    @Override
    public void printHeadline() {
        System.out.println(ConsoleFormatter.line((columnSize * columns) + 2 * columns + 1));
        System.out.println("|\t" +
                ConsoleFormatter.center("Auction for component " + this.componentToBeAuctioned.getIdentifier() + " on " + this.host.getIdentifier(), (columnSize * columns) + columns) +
                "|");
        System.out.println("| " + ConsoleFormatter.line((columnSize * columns) + columns + 1) + " |");
        System.out.println("|\t" + ConsoleFormatter.rightPad("maxBid = " + this.maxBid, (columnSize * columns) + columns) + "|");

        System.out.println("|\t" + ConsoleFormatter.rightPad("Bids from Bidders after AUCTION_START:", (columnSize * columns) + columns) + "|");
        this.answersAfterAuctionStart.forEach(answer ->
                System.out.println("|\t" + ConsoleFormatter.rightPad("    bid(" + ((Bidder)answer.getSender()).getHost().getIdentifier() +
                        ") = " + ((Tupel)answer.getBody()).getAttributeA(), (columnSize * columns) + columns) + "|"));

        if(this.biddersBestTradableComponents != null && this.biddersBestTradableComponents.size() > 0) {

            System.out.println("|\t" + ConsoleFormatter.rightPad("Select c_t for all devices that have sent component identifiers along for trade:", (columnSize * columns) + columns) + "|");

            this.biddersBestTradableComponents.forEach((k, v) -> {
                System.out.println("|\t" +  ConsoleFormatter.rightPad("    " + k.getHost().getIdentifier()
                        + " best tradable component is " + v.getIdentifier(), (columnSize * columns) + columns) + "|");

                if(this.adjustedgBiddersBid != null) {
                    this.adjustedgBiddersBid.forEach((k2,v2) -> {
                        if(k2.equals(k)) {
                            System.out.println("|\t" +  ConsoleFormatter.rightPad("    Adjusting bid for " +
                                    k2.getHost().getIdentifier() + ": " + v2, (columnSize * columns) + columns) + "|");
                        }
                    });
                }
            });
        }

        System.out.println("|\t" + ConsoleFormatter.rightPad("Final bids:", (columnSize * columns) + columns) + "|");
        this.fixedBids.forEach((k,v) -> {
            if(k != null)
                System.out.println("|\t" + ConsoleFormatter.rightPad("    bid(" + k.getHost().getIdentifier() + ") = " + v, (columnSize * columns) + columns) + "|");
                });

        if(winner != null && winner.getKey() != null) {
            System.out.println("|\t" + ConsoleFormatter.rightPad("Device with lowest bid: " + winner.getKey().getHost().getIdentifier(), (columnSize * columns) + columns) + "|");
            System.out.println("|\t" + ConsoleFormatter.rightPad("Compare: " + winner.getValue() + " < " + this.maxBid + "  => " + (winner.getValue() < this.maxBid), (columnSize * columns) + columns) + "|");

            if(winner.getValue() < maxBid) {

                if(tradeCandidate.get(winner.getKey()) == null) {
                    // MIGRATION
                    System.out.println("|\t" + ConsoleFormatter.rightPad("Migrating " + this.componentToBeAuctioned.getIdentifier() + " from " + this.host.getIdentifier() + " to " + winner.getKey().getHost().getIdentifier()
                            , (columnSize * columns) + columns) + "|");
                } else {
                    // TRADE
                    System.out.println("|\t" + ConsoleFormatter.rightPad("Trading " + this.componentToBeAuctioned.getIdentifier() + " from " + this.host.getIdentifier() + " to " + winner.getKey().getHost().getIdentifier()
                            , (columnSize * columns) + columns) + "|");
                    System.out.println("|\t" + ConsoleFormatter.rightPad("Trading " + tradeCandidate.get(winner.getKey()).getIdentifier() + " from " + winner.getKey().getHost().getIdentifier() + " to " + this.host.getIdentifier()
                            , (columnSize * columns) + columns) + "|");
                }
            }
        }
        System.out.println(ConsoleFormatter.line((columnSize * columns) + 2 * columns + 1));
    }
}
