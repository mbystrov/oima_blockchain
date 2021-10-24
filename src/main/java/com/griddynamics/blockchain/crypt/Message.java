package com.griddynamics.blockchain.crypt;

public class Message {

    private final int identifier;
    private final int minerId;
    private final String text;
    private final byte[] signedMsg;

    public Message(int minerId, int identifier, String text, byte[] signedMsg) {
        this.minerId = minerId;
        this.identifier = identifier;
        this.text = text;
        this.signedMsg = signedMsg;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getText() {
        return text;
    }

    public byte[] getSignedMsg() {
        return signedMsg;
    }

    public int getMinerId() {
        return minerId;
    }
}
