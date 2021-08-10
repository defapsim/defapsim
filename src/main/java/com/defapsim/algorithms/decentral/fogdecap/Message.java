package com.defapsim.algorithms.decentral.fogdecap;

/**
 * A message which is sent between the auctioneer and the bidder on different devices
 */
public class Message {

    /**
     * The message type ( see MessageType class )
     */
    private MessageType messageType;

    /**
     * The sender of the message ( Auctioneer or Bidder )
     */
    private Sender sender;

    /**
     * The body of a message
     */
    private Object body;

    public Message(MessageType messageType, Sender sender) {
        this.messageType = messageType;
        this.sender = sender;
    }

    public Message(MessageType messageType, Sender sender, Object body) {
        this.messageType = messageType;
        this.body = body;
        this.sender = sender;
    }

    /**
     * Getter
     */

    public MessageType getMessageType() {
        return this.messageType;
    }

    public Sender getSender() {
        return this.sender;
    }

    public Object getBody() {
        return body;
    }

}
