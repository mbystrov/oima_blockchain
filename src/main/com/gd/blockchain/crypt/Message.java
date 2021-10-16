package com.gd.blockchain.crypt;

public class Message {

    private int identifier;
    private int minerId;
    private String text;
    private byte[] signedMsg;

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
