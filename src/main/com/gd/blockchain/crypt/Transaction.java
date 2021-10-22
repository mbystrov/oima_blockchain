package com.gd.blockchain.crypt;

public class Transaction {

    private int identifier;
    private int senderMinerId;
    private int receiverMinerId;
    private int amount;
    private byte[] signedTransaction;

    public Transaction(int identifier, int senderMinerId, int receiverMinerId, int amount) {
        this.identifier = identifier;
        this.senderMinerId = senderMinerId;
        this.receiverMinerId = receiverMinerId;
        this.amount = amount;
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getSenderMinerId() {
        return senderMinerId;
    }

    public int getReceiverMinerId() {
        return receiverMinerId;
    }

    public int getAmount() {
        return amount;
    }

    public byte[] getSignedTransaction() {
        return signedTransaction;
    }

    @Override
    public String toString() {
        return "miner" + senderMinerId
                + " send " + amount
                + "VC to miner " + receiverMinerId;
    }

    public void setSignedTransaction(byte[] signedTransaction) {
        this.signedTransaction = signedTransaction;
    }
}
