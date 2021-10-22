package com.gd.blockchain.multithreading;

import com.gd.blockchain.Block;
import com.gd.blockchain.Blockchain;
import com.gd.blockchain.Messages;
import com.gd.blockchain.crypt.KeyPairCreator;
import com.gd.blockchain.crypt.Message;
import com.gd.blockchain.crypt.Transaction;

import java.security.*;
import java.util.Random;

public class Miner implements Runnable {
    private boolean newBlockGenerated = false;
    private final int minerId;
    private final BlockManager blockManager;
    private final Blockchain blockchain;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private int messageId = 0;
    private int transactionId = 0;

    public Miner(int minerId, MinerManager minerManager, BlockManager blockManager, Blockchain blockchain) {
        this.minerId = minerId;
        minerManager.addMiner(this);
        this.blockManager = blockManager;
        this.blockchain = blockchain;
        try {
            KeyPair keyPair = KeyPairCreator.createKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        blockManager.initializeMiner(minerId, publicKey);
    }

    public void mineBlocks() {
        while (blockManager.areAllBlocksLeft()) {
            Block block = createBlock(); // The call of a blocking thread which creates a new block
            synchronized (blockManager) { // The first miner who created a new block locks this code part
                if (newBlockGenerated) { // If a miner is late they send a transaction, otherwise add the new block
                    blockManager.sendTransaction(createTransaction());
                } else {
                    blockManager.addBlock(block, minerId);
                }
            }
            newBlockGenerated = false;
        }
    }

    public Message createMessage() {
        String msgText = Messages.getRandomMessage();
        byte[] signedMsd = new byte[0];
        try {
            signedMsd = KeyPairCreator.sign(msgText, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Message(minerId, messageId++, msgText, signedMsd);
    }

    public Transaction createTransaction() {
        int amount = new Random().nextInt(100);
        int receiverMinerId = new Random().nextInt(blockManager.getMinersCount());
        Transaction transaction = new Transaction(transactionId++, minerId, receiverMinerId, amount);
        String transactionString = transaction.toString();
        try {
            byte[] signedTransaction = KeyPairCreator.sign(transactionString, privateKey);
            transaction.setSignedTransaction(signedTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public Block createBlock() {
        String hashPrev = blockchain.getHashOneBeforeLast();
        String prefix = new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
        return new Block(blockchain.generateNewId(), hashPrev, prefix, minerId);
    }

    public void update() {
        newBlockGenerated = true;
    }

    @Override
    public void run() {
        mineBlocks();
    }
}
