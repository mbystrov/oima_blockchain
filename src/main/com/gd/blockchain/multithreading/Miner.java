package com.gd.blockchain.multithreading;

import com.gd.blockchain.Block;
import com.gd.blockchain.Blockchain;
import com.gd.blockchain.Messages;
import com.gd.blockchain.crypt.KeyPairCreator;
import com.gd.blockchain.crypt.Message;

import java.security.*;

public class Miner implements Runnable {
    private boolean blockGenerated = false;
    private final int minerId;
    private final BlockManager blockManager;
    private final Blockchain blockchain;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private int messageId = 0;

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
    }

    public void mineBlock() {
        blockManager.sendPublicKey(minerId, publicKey);
        while (blockManager.isContinues()) {
            int blockNumber = blockManager.getBlockNumber();
//            System.out.printf("Miner %d is working on block %d\n", minerId, blockNumber);
            String hashPrev = blockchain.getHashOneBeforeLast();
            String prefix = new String(new char[blockchain.getZerosNumber()]).replace('\0', '0');
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            Block block = new Block(blockchain.generateNewId(), hashPrev, prefix, minerId);
            synchronized (blockManager) {
                if (blockGenerated) {
//                    System.out.printf("Miner %d is late to create %d block\n", minerId, blockNumber);
                    String msgText = Messages.getRandomMessage();
                    byte[] signedMsd = new byte[0];
                    try {
                        signedMsd = KeyPairCreator.sign(msgText, privateKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message(minerId, messageId++, msgText, signedMsd);
//                    if (minerId == 4 && messageId == 2) {
//                        blockManager.sendMessage(msg);
//                    }
                    System.out.println(minerId + " has sent a message " + msg.getText() + " with msg Id " + (messageId - 1) + " block number " + blockManager.getBlockNumber());
                    blockManager.sendMessage(msg);
                } else {
                    System.out.printf("Miner %d generated block %d\n", minerId, blockNumber);
                    blockManager.addBlock(block);
                }
            }
            blockGenerated = false;
        }
    }

    public void update() {
        blockGenerated = true;
    }

    @Override
    public void run() {
        mineBlock();
    }
}
